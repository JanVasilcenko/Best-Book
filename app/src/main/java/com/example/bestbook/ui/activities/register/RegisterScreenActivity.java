package com.example.bestbook.ui.activities.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.bestbook.ui.activities.main.MainActivity;
import com.example.bestbook.R;
import com.example.bestbook.ui.activities.login.LoginScreenActivity;

import com.google.firebase.auth.FirebaseUser;

public class RegisterScreenActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private ProgressBar progressBar;

    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);

        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        confirmPassword = findViewById(R.id.registerConfirmPassword);
        progressBar = findViewById(R.id.progressBar);

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        getSupportActionBar().hide();

        registerViewModel.getUserData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null)
                {
                    Intent toMainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(toMainActivityIntent);
                }
            }
        });
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

        registerViewModel.register(emailText,passwordText);
    }
}
