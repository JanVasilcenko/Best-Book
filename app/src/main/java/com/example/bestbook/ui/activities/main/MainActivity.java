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

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bestbook.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private TextView email;
    private ImageView profile;
    private MainActivityViewModel mainActivityViewModel;
    private final int GALLERY_REQUEST = 100;
    private Uri selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        Toolbar toolbar = findViewById(R.id.toolbar);
        email = (TextView) headerView.findViewById(R.id.email);
        profile = navigationView.getHeaderView(0).findViewById(R.id.imageView);

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
                    Toast.makeText(getApplicationContext(), R.string.logoutsuccess, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        LoadProfilePicture();
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
                    emailVerification.setTitle(R.string.verifyemail);
                    emailVerification.setMessage(R.string.thisaccountnotverified);

                    emailVerification.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mainActivityViewModel.verifyingEmail();
                        }
                    });

                    emailVerification.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
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

    public void setImage(View view) {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
            if(requestCode == GALLERY_REQUEST)
            {
                selectedImage = data.getData();

                    UCrop.of(selectedImage,Uri.fromFile(new File(getCacheDir(), "myProfile")))
                            .withAspectRatio(16, 9)
                            .withMaxResultSize(200, 200)
                            .start(this);
            }
            else if (requestCode == UCrop.REQUEST_CROP){
                final Uri resultUri = UCrop.getOutput(data);
        try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), resultUri);
                SaveProfileImage(bitmap);
                profile.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.i("TAG", "Some exception " + e);
        }
            }
            else if (resultCode == UCrop.RESULT_ERROR) {
                final Throwable cropError = UCrop.getError(data);
            }
    }

    private void LoadProfilePicture()
    {
        SharedPreferences prefs = getSharedPreferences("MyProfile", MODE_PRIVATE);
        Bitmap image = DecodeToBitmap(prefs.getString("profileImage","someName"));

        if(image != null) {
            try {
                profile.setImageBitmap(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void SaveProfileImage(Bitmap bitmap)
    {
        SharedPreferences prefs = getSharedPreferences("MyProfile", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("profileImage", EncodeToString(bitmap));
        editor.apply();
    }

    public static String EncodeToString(Bitmap image) {
        Bitmap bitmapImage = image;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }

    public static Bitmap DecodeToBitmap(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}