package com.example.bestbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    FirebaseAuth auth;
    FirebaseUser user;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        auth = FirebaseAuth.getInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new home_fragment());
        user = auth.getCurrentUser();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_draw_open, R.string.navigation_draw_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState==null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new home_fragment()).commit();
            navigationView.setCheckedItem(R.id.home);
        }

        onPrepareOptionsMenu(navigationView.getMenu());
    }

    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem verify = menu.findItem(R.id.verify_email);
        if(user.isEmailVerified())
        {
            verify.setVisible(false);
        }
        else
        {
            verify.setVisible(true);
            Toast.makeText(this, "Please verify your email", Toast.LENGTH_SHORT).show();
        }
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new home_fragment()).commit();
                break;
            case R.id.fragment_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_settings()).commit();
                break;
            case R.id.favorites:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new favourite_fragment()).commit();
                break;
            case R.id.log_out:
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(),Login.class));
                    finish();
                    break;
            case R.id.verify_email:
                if (!user.isEmailVerified())
                {
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this, "Verification email has been sent", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("tag", "onFailure: Email not sent"+e.getMessage());
                        }
                    });
                }
                else
                    {
                        Toast.makeText(this, "Email has been verified", Toast.LENGTH_SHORT).show();
                        MenuItem verify = navigationView.getMenu().findItem(R.id.verify_email).setVisible(false);
                    }
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
            {
                super.onBackPressed();
            }
    }

    public void logOut(View view)
    {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
}