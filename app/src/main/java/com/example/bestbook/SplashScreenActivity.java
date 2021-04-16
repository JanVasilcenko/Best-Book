package com.example.bestbook;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        getSupportActionBar().hide();

        NavigateToLogin();
    }

    private Runnable runnable = new Runnable()
    {
        @Override
        public void run() {
            Intent toNextActivity = new Intent(SplashScreenActivity.this, LoginScreenActivity.class);
            startActivity(toNextActivity);
        }
    };

    public void NavigateToLogin()
    {
        handler.postDelayed(runnable,3000);
    }
}
