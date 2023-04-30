package com.example.chatappw6_doan.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.chatappw6_doan.R
import com.example.chatappw6_doan.constants.AllConstants
import com.example.chatappw6_doan.databinding.FragmentGetNumberBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.concurrent.TimeUnit


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GetNumberFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GetNumberFragment : Fragment() {

    private var binding: FragmentGetNumberBinding? = null
    private var number: String? = null

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
        binding = FragmentGetNumberBinding.inflate(inflater, container, false)
        var view: View = binding!!.root
        initViews()
        binding!!.btnGenerateOTP.setOnClickListener {
            checkNumber()
            if (checkNumber()) {
                val phonenumber =
                    binding!!.countryCodePicker.selectedCountryCodeWithPlus + number

                sendOTP(phonenumber)
            }
        }


        return view

    }

    private fun initViews() {
        binding!!.countryCodePicker.registerCarrierNumberEditText(binding!!.edtNumber)
        binding!!.countryCodePicker.formattedFullNumber
        binding!!.countryCodePicker.isValidFullNumber


    }

    private fun checkNumber(): Boolean {
        number = binding!!.edtNumber.text.toString()
        return if (number!!.isEmpty()) {
            binding!!.edtNumber.error = "Filed is required"
            false
        } else if (number!!.length < 10) {
            binding!!.edtNumber.error = "Invalid number"
            false
        } else {
            binding!!.edtNumber.error = null
            true
        }

    }

    private fun sendOTP(phoneNumber: String) {
        val mAuth = FirebaseAuth.getInstance()
        val options: PhoneAuthOptions = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(verifyPhoneNumber)
            // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

     private var verifyPhoneNumber: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(e: FirebaseException) {
                if (e is FirebaseAuthInvalidCredentialsException)
                    Toast.makeText(
                        context,
                        e.message,
                        Toast.LENGTH_LONG
                    ).show()
                else if (e is FirebaseTooManyRequestsException)
                    Toast.makeText(
                        context,
                        "The sms quota for the project has been exceeded",
                        Toast.LENGTH_LONG
                    ).show()


            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                val fragment: Fragment = VerifyNumberFragment()
                val bundle = Bundle()
                bundle.putString(AllConstants.VERIFICATION_CODE, p0)
                fragment.arguments = bundle
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
            }
        }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GetNumber.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GetNumberFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }

            }
    }


}