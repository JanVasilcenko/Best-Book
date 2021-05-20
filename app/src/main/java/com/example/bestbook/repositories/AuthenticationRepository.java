package com.example.bestbook.repositories;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.bestbook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationRepository {
    private static AuthenticationRepository instance;
    private Application application;

    private FirebaseAuth firebaseAuth;
    private MutableLiveData<FirebaseUser> userData;
    private MutableLiveData<Boolean> loggedOutData;

    public AuthenticationRepository(Application application) {
        this.application = application;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.userData = new MutableLiveData<>();
        this.loggedOutData = new MutableLiveData<>();

        if(firebaseAuth.getCurrentUser() != null)
        {
            userData.postValue(firebaseAuth.getCurrentUser());
            loggedOutData.postValue(false);
        }
    }

    public static synchronized AuthenticationRepository getInstance(Application application)
    {
        if(instance == null)
        {
            instance = new AuthenticationRepository(application);
        }
        return instance;
    }

    public void login(String email, String password)
    {
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    userData.postValue(firebaseAuth.getCurrentUser());
                    loggedOutData.postValue(false);
                    Toast.makeText(application.getApplicationContext(), R.string.loginsuccess, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(application.getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void register(String email, String password)
    {
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    //Sending verification email
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(application.getApplicationContext(),R.string.verificationpositive, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(application.getApplicationContext(), R.string.verificationnegative + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    Toast.makeText(application.getApplicationContext(), R.string.userregisteredpositive, Toast.LENGTH_SHORT).show();
                    loggedOutData.postValue(false);
                }
                else
                {
                    Toast.makeText(application.getApplicationContext(), R.string.error + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void verifyingEmail()
    {
        firebaseAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(application.getApplicationContext(), R.string.verificationpositive, Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(application.getApplicationContext(), R.string.verificationnegative + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void signOut()
    {
        firebaseAuth.signOut();
        loggedOutData.postValue(true);
    }

    public void resetPassword(String email)
    {
        firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(application.getApplicationContext(), R.string.linkpositive, Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(application.getApplicationContext(), R.string.error + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public String getUserID(){return firebaseAuth.getCurrentUser().getUid();}

    public String getUserEmail(){return firebaseAuth.getCurrentUser().getEmail();}

    public MutableLiveData<FirebaseUser> getUserData() {
        return userData;
    }

    public MutableLiveData<Boolean> getLoggedOutData() {
        return loggedOutData;
    }
}
