package com.example.chatappw6_doan.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.chatappw6_doan.R
import com.example.chatappw6_doan.activity.EditName
import com.example.chatappw6_doan.constants.AllConstants
import com.example.chatappw6_doan.databinding.FragmentProfileBinding
import com.example.chatappw6_doan.model.UserModel
import com.example.chatappw6_doan.permissions.Permissions
import com.example.chatappw6_doan.utils.Util
import com.example.chatappw6_doan.viewmodel.ProfileViewModel
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
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    private var binding: FragmentProfileBinding? = null
    private var profileViewModel: ProfileViewModel? = null
    private var imageUri: Uri? = null
    private var util: Util? = null
    private var permissions: Permissions? = null
    private lateinit var alertDialog: AlertDialog
    private var user: UserModel? = null

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
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding!!.lifecycleOwner = viewLifecycleOwner
        var view = binding!!.root
        profileViewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
                .create<ProfileViewModel>(
                    ProfileViewModel::class.java
                )
        Log.d("Check", "get user")
        profileViewModel!!.getResponseUser()!!.observe(viewLifecycleOwner, { it ->
            binding!!.userModel = it
            user = it
            val name: String = it!!.name.toString()
            if (name.contains(" ")) {
                val split = name.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                binding!!.txtProfileFName.text = split[0]
                binding!!.txtProfileLName.text = split[1]
            } else {
                binding!!.txtProfileFName.text = name
                binding!!.txtProfileLName.text = ""
            }
        })

        util = Util()
        permissions = Permissions()
        binding!!.imgPickImage.setOnClickListener {
            if (permissions!!.isStorageOk(context)) {
                pickImage()
            } else permissions!!.requestStorage(activity)

        }
        binding!!.imgEditStatus.setOnClickListener {
            val builder = AlertDialog.Builder(
                context
            )
            val view1: View = LayoutInflater.from(context).inflate(R.layout.dialog_layout, null)
            builder.setView(view1)

            val edtStatus = view1.findViewById<EditText>(R.id.edtUserStatus)
            val btnEditStatus = view1.findViewById<Button>(R.id.btnEditStatus)

            btnEditStatus.setOnClickListener {
                val status = edtStatus.text.toString().trim { it <= ' ' }
                if (!status.isEmpty()) {
                    profileViewModel!!.editStatus(status)
                    alertDialog!!.dismiss()
                }
            }

            alertDialog = builder.create()
            alertDialog.show()
        }
        binding!!.cardName.setOnClickListener {
            val intent = Intent(context, EditName::class.java)
            intent.putExtra("name", user!!.name)
            startActivityForResult(intent, AllConstants.USERNAME_CODE)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> if (data != null) {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    imageUri = result.uri
                    uploadImage(imageUri)
                } else {
                    Log.d("image", "onActivityResult: " + result.error)
                }
            }

            AllConstants.USERNAME_CODE -> {
                var name = data!!.getStringExtra("name")
                profileViewModel!!.editUserName(name)
            }


        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            AllConstants.STORAGE_REQUEST_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    pickImage()
                else
                    Toast.makeText(context, "Storage Permission rejected.", Toast.LENGTH_SHORT)
                        .show()

            }
        }
    }

    private fun pickImage() {
        CropImage.activity().setCropShape(CropImageView.CropShape.OVAL)
            .start(requireContext(), this)
    }

    private fun uploadImage(imageUri: Uri?) {
        val storageReference: StorageReference =
            FirebaseStorage.getInstance().getReference(util!!.getUid())
                .child(AllConstants.IMAGE_PATH)
        storageReference.putFile(imageUri!!).addOnSuccessListener { taskSnapshot ->
            val task = taskSnapshot.storage.downloadUrl
            task.addOnCompleteListener { task ->
                val uri = task.result.toString()
                profileViewModel!!.editImage(uri)
//                sharedPreferences.putString("userImage", uri).apply()
            }
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}