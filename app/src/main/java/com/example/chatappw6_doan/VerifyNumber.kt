package com.example.chatappw6_doan


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.chatappw6_doan.databinding.FragmentVerifyNumberBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VerifyNumber.newInstance] factory method to
 * create an instance of this fragment.
 */
class VerifyNumber : Fragment() {

    private var binding: FragmentVerifyNumberBinding? = null
    private var OTP: String? = null
    private var pin: String? = null
    private var firebaseAuth: FirebaseAuth? = null

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
        binding = FragmentVerifyNumberBinding.inflate(inflater, container, false)
        var view: View = binding!!.root

        firebaseAuth = FirebaseAuth.getInstance()
        var bundle = arguments
        if (bundle != null) {
            OTP = bundle.getString(AllConstants.VERIFICATION_CODE)

        }
        binding!!.btnVerify.setOnClickListener {
            checkOTP()
            if (checkOTP()) {
                verifyPine(pin.toString())
            }
        }
        return view
    }

    private fun checkOTP(): Boolean {
        pin = binding!!.otpTextView.text.toString()
        return if (pin!!.isEmpty()) {
            binding!!.otpTextView.error = "Field is required"
            false
        } else if (pin!!.length < 6) {
            binding!!.otpTextView.error = "Invalid code"
            false
        } else {
            binding!!.otpTextView.error = null
            true
        }
    }

    private fun verifyPine(pin: String) {
        val credential = PhoneAuthProvider.getCredential(OTP!!, pin)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        firebaseAuth!!.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                var fragment = UserData()
                parentFragmentManager.beginTransaction().replace(R.id.container, fragment)
                    .commit()
            } else Toast.makeText(context, "" + task.exception, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VerifyNumber.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VerifyNumber().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}