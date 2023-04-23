package com.example.chatappw6_doan;

import android.Manifest;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class fu {
    private FirebaseAuth firebaseAuth;
    private ChipNavigationBar chipNavigationBar;
    public void main(){

    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks verifyPhoneNumber = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
                @Override
                public void onItemSelected(int i) {

                }
            });
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            
        }
    };
}
