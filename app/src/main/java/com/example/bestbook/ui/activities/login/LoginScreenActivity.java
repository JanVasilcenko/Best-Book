package com.example.bestbook.ui.activities.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.bestbook.ui.activities.main.MainActivity;
import com.example.bestbook.R;
import com.example.bestbook.ui.activities.register.RegisterScreenActivity;
import com.google.firebase.auth.FirebaseUser;

public class LoginScreenActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private ProgressBar progressBar;

    private LoginViewModel loginViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        email = findViewById(R.id.loginEmailField);
        password = findViewById(R.id.loginPasswordField);
        progressBar = findViewById(R.id.progressBar2);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        getSupportActionBar().hide();

        loginViewModel.getUserData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if(firebaseUser != null)
                {
                    Intent toMainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(toMainActivityIntent);
                }
            }
        });
    }

    public void NavigateToRegister(View view)
    {
        Intent toRegisterIntent = new Intent(this, RegisterScreenActivity.class);
        startActivity(toRegisterIntent);
    }

    public void Login(View view)
    {
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();

        if (TextUtils.isEmpty((emailText)))
        {
            email.setError(Integer.toString(R.string.pleaseenteremail));
            return;
        }

        if (TextUtils.isEmpty(passwordText))
        {
            password.setError(Integer.toString(R.string.pleaseenterpassword));
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        loginViewModel.login(emailText,passwordText);
    }

    public void ResetPassword(View view)
    {
        EditText resetEmail = new EditText(view.getContext());
        AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());

        passwordResetDialog.setTitle(R.string.resetpassword);
        passwordResetDialog.setMessage(R.string.enteremail);
        passwordResetDialog.setView(resetEmail);

        passwordResetDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String mail = resetEmail.getText().toString();
                loginViewModel.resetPassword(mail);
            }
        });

        passwordResetDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Closing dialog
            }
        });

        passwordResetDialog.create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        email.setText("");
        password.setText("");
        progressBar.setVisibility(View.INVISIBLE);
    }
}
