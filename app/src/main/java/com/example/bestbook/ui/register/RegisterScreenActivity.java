package com.example.bestbook.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bestbook.MainActivity;
import com.example.bestbook.R;
import com.example.bestbook.ui.login.LoginScreenActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterScreenActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    EditText confirmPassword;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);

        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        confirmPassword = findViewById(R.id.registerConfirmPassword);
        progressBar = findViewById(R.id.progressBar);

        firebaseAuth = FirebaseAuth.getInstance();

        getSupportActionBar().hide();

        //If the user is already logged in, send him to main activity
        if (firebaseAuth.getCurrentUser() != null)
        {
            Intent toMainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(toMainActivityIntent);
            finish();
        }
    }

    public void NavigateToLogin(View view)
    {
        Intent toLoginIntent = new Intent(this, LoginScreenActivity.class);
        startActivity(toLoginIntent);
    }

    public void Register(View view)
    {
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();
        String confirmPasswordText = confirmPassword.toString().trim();

        if(TextUtils.isEmpty(emailText))
        {
            email.setError("Please enter an Email");
            return;
        }

        if (TextUtils.isEmpty(passwordText))
        {
            password.setError("Please enter a Password");
            return;
        }

        if(passwordText.length() < 6)
        {
            password.setError("Password must be longer than six characters");
            return;
        }

        if(passwordText.equalsIgnoreCase(confirmPasswordText))
        {
            confirmPassword.setError("Passwords do not match");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        //Creating new account
        firebaseAuth.createUserWithEmailAndPassword(emailText,passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    //Sending verification email
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(RegisterScreenActivity.this, "Verification email has been sent", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterScreenActivity.this, "Email has NOT been sent " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });



                    Toast.makeText(RegisterScreenActivity.this, "User registered sucessfully", Toast.LENGTH_SHORT).show();
                    Intent toMainActivityIntent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(toMainActivityIntent);
                }
                else
                    {
                        Toast.makeText(RegisterScreenActivity.this, "Error !"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }
}
