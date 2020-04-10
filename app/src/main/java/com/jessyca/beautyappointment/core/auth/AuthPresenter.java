package com.jessyca.beautyappointment.core.auth;

import android.app.Activity;


public class AuthPresenter implements AuthContract.Presenter, AuthContract.onLoginListener {
    private AuthContract.View mLoginView;
    private AuthInteractor mLoginInteractor;

    public AuthPresenter(AuthContract.View mLoginView){
        this.mLoginView = mLoginView;
        mLoginInteractor = new AuthInteractor(this);
    }
    @Override
    public void login(Activity activity, String email, String password) {
        mLoginInteractor.performFirebaseLogin(activity, email, password);

    }

    @Override
    public void onSuccess(String message) {
        mLoginView.onLoginSuccess(message);

    }

    @Override
    public void onFailure(String message) {
        mLoginView.onLoginFailure(message);

    }
}
