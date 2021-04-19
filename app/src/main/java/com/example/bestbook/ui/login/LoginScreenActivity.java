package com.example.bestbook.ui.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bestbook.MainActivity;
import com.example.bestbook.R;
import com.example.bestbook.ui.register.RegisterScreenActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginScreenActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        //Set all elements of xml to code
        email = findViewById(R.id.loginEmailField);
        password = findViewById(R.id.loginPasswordField);
        progressBar = findViewById(R.id.progressBar2);
        forgotPassword = findViewById(R.id.fotgotPasswordtext);

        //Setup firebase authentication
        firebaseAuth = FirebaseAuth.getInstance();

        //Hide top action bar
        getSupportActionBar().hide();

        if (firebaseAuth.getCurrentUser() != null)
        {
            Intent toMainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(toMainActivityIntent);
            finish();
        }
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
            email.setError("Please enter an Email");
            return;
        }

        if (TextUtils.isEmpty(passwordText))
        {
            password.setError("Please enter a Password");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.signInWithEmailAndPassword(emailText,passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(LoginScreenActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                    Intent toMainActivityIntent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(toMainActivityIntent);
                }
                else
                    {
                        Toast.makeText(LoginScreenActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void ResetPassword(View view)
    {
        EditText resetEmail = new EditText(view.getContext());
        AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
        passwordResetDialog.setTitle("Reset Password");
        passwordResetDialog.setMessage("Enter your email to receive reset link");
        passwordResetDialog.setView(resetEmail);

        passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                
                String mail = resetEmail.getText().toString();
                firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(LoginScreenActivity.this, "Reset link sent to your Email", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginScreenActivity.this, "Error, Link has not been sent "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                     
            }
        });

        passwordResetDialog.create().show();
    }
}
