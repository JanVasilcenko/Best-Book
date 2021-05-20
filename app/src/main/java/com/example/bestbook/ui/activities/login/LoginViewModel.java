package com.example.bestbook.ui.activities.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.bestbook.repositories.AuthenticationRepository;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends AndroidViewModel {
    private AuthenticationRepository authenticationRepository;
    private MutableLiveData<FirebaseUser> userData;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        this.authenticationRepository = AuthenticationRepository.getInstance(application);
        userData = authenticationRepository.getUserData();
    }

    public void login(String email, String password)
    {
        authenticationRepository.login(email,password);
    }

    public void resetPassword(String email)
    {
        authenticationRepository.resetPassword(email);
    }

    public MutableLiveData<FirebaseUser> getUserData() {
        return userData;
    }
}
