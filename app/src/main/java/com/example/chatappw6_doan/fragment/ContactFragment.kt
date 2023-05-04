package com.example.chatappw6_doan.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatappw6_doan.adapter.ContactAdapter
import com.example.chatappw6_doan.databinding.FragmentContactBinding
import com.example.chatappw6_doan.model.UserModel
import com.example.chatappw6_doan.permissions.Permissions
import com.example.chatappw6_doan.utils.Util
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ContactFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContactFragment : Fragment(), SearchView.OnQueryTextListener,
    androidx.appcompat.widget.SearchView.OnQueryTextListener {
    private var binding : FragmentContactBinding? = null
    private var database : FirebaseFirestore? = null
    private var permissions : Permissions? = null
    private var appContacts : ArrayList<UserModel>? = null
    private var contactAdapter : ContactAdapter? = null
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
        binding!!.contactSearchView.setOnQueryTextListener(this)

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
                        userModel.uID = document.id
                        appContacts!!.add(userModel)
                    }
                }
            }
            contactAdapter = ContactAdapter(requireContext(), appContacts!!)
            binding!!.recyclerViewContact.adapter = contactAdapter
        }

    }




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

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        if (contactAdapter != null) contactAdapter!!.getFilter().filter(p0)
        return false
    }
}