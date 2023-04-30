package com.example.chatappw6_doan.fragment

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.chatappw6_doan.databinding.FragmentProfileBinding
import com.example.chatappw6_doan.model.ResponseUser
import com.example.chatappw6_doan.model.UserModel
import com.example.chatappw6_doan.viewmodel.ProfileViewModel

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
    private var binding : FragmentProfileBinding? = null
    private var profileViewModel : ProfileViewModel? = null

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
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        var view = binding!!.root
        profileViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create<ProfileViewModel>(
                ProfileViewModel::class.java
            )

        Log.d("Check","get user")
        profileViewModel!!.getResponseUser().observe(viewLifecycleOwner,{
            binding!!.userModel = it.userModel
            val user = it.userModel
            val name: String = user!!.name!!
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

        return view
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