package com.example.bestbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);

        getSupportActionBar().hide();
    }

    public void NavigateToLogin(View view)
    {
        Intent toLoginIntent = new Intent(this,LoginScreenActivity.class);
        startActivity(toLoginIntent);
    }

    public void Register()
    {

    }
}
