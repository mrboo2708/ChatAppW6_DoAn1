package com.example.chatappw6_doan.fragment

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.chatappw6_doan.activity.DashBoard
import com.example.chatappw6_doan.databinding.FragmentUserDataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserDataFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserDataFragment : Fragment() {
    private var binding: FragmentUserDataBinding? = null
    private var storePath: String? = null
    private var name: String? = null
    private var status: String? = null
    private var firebaseAuth: FirebaseAuth? = null
    private var database: FirebaseFirestore? = null
    private var storageReference: StorageReference? = null
    private var imageUri: Uri? = null

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUserDataBinding.inflate(inflater, container, false)
        var view: View = binding!!.root
        firebaseAuth = FirebaseAuth.getInstance()
        database = Firebase.firestore
        val docref = database!!.collection("Users").document(firebaseAuth!!.uid.toString())
        docref.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null) {
                    if (document.exists()) {
                        Log.d("TAG", "Document already exists.")
                        val intent = Intent(context, DashBoard::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    } else {
                        Log.d("TAG", "Document doesn't exist.")
                        storageReference = FirebaseStorage.getInstance().getReference()
                        storePath = firebaseAuth!!.uid + "Media/Profile_Image/profile"
                        binding!!.imgPickImage.setOnClickListener {
                            if (isStoragePermissionOK()) {
                                pickImage()
                            }

                        }

                        binding!!.btnDataUser.setOnClickListener {
                            if (checkName() && checkStatus() && checkImage()) {
                                uploadData()
                            }
                        }
                    }
                }
            } else {
                Log.d("TAG", "Error: ", task.exception)
            }
        }


        return view
    }

    private fun uploadData() {
        Toast.makeText(context, "Uploading", Toast.LENGTH_SHORT).show()
        storageReference!!.child(storePath.toString()).putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot ->
                val task = taskSnapshot.storage.downloadUrl
                task.addOnCompleteListener { task ->
                    val url = task.result.toString()
                    var map = hashMapOf<String, Any>(
                        "name" to name.toString(),
                        "status" to status.toString(),
                        "image" to url,
                        "number" to "",
                        "online" to "",
                        "typing" to "",
                        "token" to ""
                    )

                    database!!.collection("Users").document(firebaseAuth!!.uid.toString()).set(map)
                        .addOnCompleteListener { task ->
                            Log.d("Check", "update")
                            if (task.isSuccessful) {
                                val intent = Intent(context, DashBoard::class.java)
                                startActivity(intent)
                                requireActivity().finish()
                                Log.d("Check", "sucess")
                            } else Toast.makeText(context, "Fail to upload", Toast.LENGTH_SHORT)
                                .show()
                            Log.d("Check", "not sucess")

                        }
                        .addOnFailureListener {
                            Log.d("Check", "Error adding document", it)
                        }
                }
            }

    }

    private fun isStoragePermissionOK(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            requireStoragePermission()
            false
        }
    }

    private fun requireStoragePermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), 100
        )
        Log.d("Check", "requireStorage")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImage()
                } else {
                    Toast.makeText(context, "permission denied", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun pickImage() {
        CropImage.activity()
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(requireContext(), this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                imageUri = result.uri
                binding!!.userImage.setImageURI(imageUri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    private fun checkName(): Boolean {
        Log.d("Check", "name")
        name = binding!!.editUserName.text.toString()
        return if (name!!.isEmpty()) {
            binding!!.editUserName.error = "Filed is required"
            false
        } else {
            binding!!.editUserName.error = null
            true
        }
    }

    private fun checkStatus(): Boolean {
        Log.d("Check", "status")
        status = binding!!.editUserStatus.text.toString()
        return if (status!!.isEmpty()) {
            binding!!.editUserStatus.error = "Filed is required"
            false
        } else {
            binding!!.editUserStatus.error = null
            true
        }
    }

    private fun checkImage(): Boolean {
        Log.d("Check", "image")
        return if (imageUri == null) {
            Toast.makeText(context, "image is required", Toast.LENGTH_SHORT).show()
            false
        } else true
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserData.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserDataFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}