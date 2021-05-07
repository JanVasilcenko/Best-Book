package com.example.bestbook.ui.activities.main;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bestbook.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private TextView email;

    private MainActivityViewModel mainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        Toolbar toolbar = findViewById(R.id.toolbar);
        email = (TextView) headerView.findViewById(R.id.email);

        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        setSupportActionBar(toolbar);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment ,R.id.favouriteFragment, R.id.settingsFragment)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        LogOutListener(navigationView);

        VerifiedEmailCheck();

        mainActivityViewModel.getLogoutData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loggedOut) {
                if (loggedOut)
                {
                    Toast.makeText(getApplicationContext(), "User Logged Out", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void LogOutListener(NavigationView navigationView)
    {
        MenuItem menuItem = navigationView.getMenu().getItem(3);

        menuItem.setOnMenuItemClickListener(b->{
            mainActivityViewModel.signOut();
            return true;
        });
    }

    private void VerifiedEmailCheck()
    {
        mainActivityViewModel.getUserData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                email.setText(firebaseUser.getEmail());
                if(!firebaseUser.isEmailVerified())
                {
                    AlertDialog.Builder emailVerification = new AlertDialog.Builder(MainActivity.this);
                    emailVerification.setTitle("Verify Email");
                    emailVerification.setMessage("This account is not verified, do you want to resend the activation email?");

                    emailVerification.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mainActivityViewModel.verifyingEmail();
                        }
                    });

                    emailVerification.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Close dialog
                        }
                    });

                    emailVerification.create().show();
                }
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}