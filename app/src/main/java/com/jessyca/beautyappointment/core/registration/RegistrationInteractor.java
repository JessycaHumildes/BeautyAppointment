package com.jessyca.beautyappointment.core.registration;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class RegistrationInteractor implements RegistrationContract.Intractor {
    private RegistrationContract.onRegistrationListener mOnRegistrationListener;

    public RegistrationInteractor(RegistrationContract.onRegistrationListener onRegistrationListener){
        this.mOnRegistrationListener = onRegistrationListener;
    }
    @Override
    public void performFirebaseRegistration(Activity activity, String email, String password) {
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(!task.isSuccessful()){
                        mOnRegistrationListener.onFailure(Objects.requireNonNull(task.getException()).getMessage());
                    }else{
                        mOnRegistrationListener.onSuccess(Objects.requireNonNull(task.getResult()).getUser());
                    }
                });
    }
}
