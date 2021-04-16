package com.example.bestbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoginScreenActivity extends AppCompatActivity {

    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        //Set all elements of xml to code
        email = findViewById(R.id.loginEmailField);
        password = findViewById(R.id.loginPasswordField);

        //Hide top action bar
        getSupportActionBar().hide();
    }

    public void NavigateToRegister(View view)
    {
        Intent toRegisterIntent = new Intent(this, RegisterScreenActivity.class);
        startActivity(toRegisterIntent);
    }

    public void Login(View view)
    {
        Intent toMainView = new Intent(this,MainActivity.class);
        startActivity(toMainView);
    }
}
