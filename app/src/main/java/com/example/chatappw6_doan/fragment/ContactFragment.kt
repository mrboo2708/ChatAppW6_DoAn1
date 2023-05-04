package com.example.chatappw6_doan.fragment

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatappw6_doan.adapter.ContactAdapter
import com.example.chatappw6_doan.constants.AllConstants
import com.example.chatappw6_doan.databinding.FragmentContactBinding
import com.example.chatappw6_doan.model.UserModel
import com.example.chatappw6_doan.permissions.Permissions
import com.example.chatappw6_doan.utils.Util
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.android.awaitFrame

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ContactFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContactFragment : Fragment() {
    private var binding : FragmentContactBinding? = null
    private var database : FirebaseFirestore? = null
    private var permissions : Permissions? = null
    private var appContacts : ArrayList<UserModel>? = null
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
        binding = FragmentContactBinding.inflate(inflater,container,false)
        val view = binding!!.root
        permissions = Permissions()
        appContacts = arrayListOf()
        val util = Util()
        binding!!.recyclerViewContact.layoutManager = LinearLayoutManager(
            context
        )
        binding!!.recyclerViewContact.setHasFixedSize(true)
        getUserContacts(util.getUid())
        return view
    }

    private fun getUserContacts(uid: String) {
        database = Firebase.firestore
        val docRef = database!!.collection("Users")

        docRef.addSnapshotListener { document, firebaseException ->
            firebaseException?.let {
                   Toast.makeText(context,it.message.toString(),Toast.LENGTH_SHORT)
                    return@addSnapshotListener
                }
            document?.let {
                for (document in it){
                    if(document.id != uid){
                        var userModel = UserModel()
                        userModel = document.toObject(UserModel::class.java)
                        appContacts!!.add(userModel)
                    }
                }
            }
            val adapter = ContactAdapter(requireContext(), appContacts!!)
                    binding!!.recyclerViewContact.adapter = adapter
        }

    }

//    private fun getAppContacts(userContacts: ArrayList<UserModel>?) {
//        if(userContacts!!.size>0){
//            database = Firebase.firestore
//            val docRef = database!!.collection("Users").orderBy("number")
//            docRef.addSnapshotListener{
//                querySnapshot , firebaseException ->
//                firebaseException?.let {
//                    Toast.makeText(context,it.message.toString(),Toast.LENGTH_SHORT)
//                    return@addSnapshotListener
//                }
//                querySnapshot?.let {
//                    for (document in it){
//                        val number = document.getString("number")
//                        for( userModel in userContacts){
//                            if(userModel.number.equals(number)){
//                                val name = document.getString("name")
//                                val status = document.getString("status")
//                                val uId = document.id
//                                val image = document.getString("image")
//                                val user  = UserModel()
//                                user.name = userModel.name
//                                user.status = status
//                                user.uID = uId
//                                user.image = image
//                                appContacts!!.add(user)
//                            }
//                        }
//                    }
//                    val adapter = ContactAdapter(requireContext(), appContacts!!)
//                    binding!!.recyclerViewContact.adapter = adapter
//                }
//            }
//
//
//        }
//    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ContactFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ContactFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}