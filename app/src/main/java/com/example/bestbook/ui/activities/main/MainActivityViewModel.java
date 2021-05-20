package com.example.bestbook.ui.activities.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.bestbook.repositories.AuthenticationRepository;
import com.google.firebase.auth.FirebaseUser;

public class MainActivityViewModel extends AndroidViewModel {
    private AuthenticationRepository authenticationRepository;
    private MutableLiveData<FirebaseUser> userData;
    private MutableLiveData<Boolean> logoutData;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        this.authenticationRepository = AuthenticationRepository.getInstance(application);
        this.userData = authenticationRepository.getUserData();
        this.logoutData = authenticationRepository.getLoggedOutData();
    }

    public void signOut()
    {
        authenticationRepository.signOut();
    }

    public void verifyingEmail()
    {
        authenticationRepository.verifyingEmail();
    }

    public MutableLiveData<Boolean> getLogoutData()
    {
        return logoutData;
    }

    public MutableLiveData<FirebaseUser> getUserData()
    {
        return userData;
    }
}
