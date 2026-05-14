package com.example.ksheerasagara.firebase

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

object PhoneAuthManager {

    private val auth = FirebaseAuth.getInstance()

    var verificationId: String = ""

    fun sendOTP(
        activity: Activity,
        phoneNumber: String,
        onCodeSent: () -> Unit,
        onError: (String) -> Unit
    ) {

        val options =
            PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(
                    object :
                        PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                        override fun onVerificationCompleted(
                            credential: PhoneAuthCredential
                        ) {
                        }

                        override fun onVerificationFailed(
                            e: FirebaseException
                        ) {
                            onError(
                                e.message ?: "OTP Verification Failed"
                            )
                        }

                        override fun onCodeSent(
                            verificationId: String,
                            token: PhoneAuthProvider.ForceResendingToken
                        ) {
                            super.onCodeSent(verificationId, token)

                            this@PhoneAuthManager.verificationId =
                                verificationId

                            onCodeSent()
                        }
                    }
                )
                .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOTP(
        otp: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        val credential =
            PhoneAuthProvider.getCredential(
                verificationId,
                otp
            )

        auth.signInWithCredential(credential)
            .addOnCompleteListener {

                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    onError(
                        it.exception?.message
                            ?: "OTP Verification Failed"
                    )
                }
            }
    }
}