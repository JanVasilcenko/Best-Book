package com.example.bestbook.ui.activities.register;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.bestbook.architecture.repositories.AuthenticationRepository;
import com.google.firebase.auth.FirebaseUser;

public class RegisterViewModel extends AndroidViewModel {
    private AuthenticationRepository authenticationRepository;
    private MutableLiveData<FirebaseUser> userData;

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        this.authenticationRepository = AuthenticationRepository.getInstance(application);
        this.userData = authenticationRepository.getUserData();
    }

    public void register(String email, String password)
    {
        authenticationRepository.register(email,password);
    }

    public MutableLiveData<FirebaseUser> getUserData() {
        return userData;
    }
}
