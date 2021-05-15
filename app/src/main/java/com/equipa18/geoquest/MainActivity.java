package com.equipa18.geoquest;

import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.equipa18.geoquest.player.PlayerManager;
import com.equipa18.geoquest.world.WorldManager;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ImageView picture;
    private TextView username;
    private TextView email;
    private File sd;
    private File imagefile;
    private String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
         **/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_account, R.id.nav_settings)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        //Read interest points from JSON in assets folder (database replacement)
        InputStream is = null;
        try {
            is = getAssets().open("interestPoints.json");
            WorldManager.loadInterestPoints(is);
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Calling the text fields and imageviews to populate based on login
        View view = navigationView.getHeaderView(0);
        picture = view.findViewById(R.id.profilepic);
        username = view.findViewById(R.id.username);
        email = view.findViewById(R.id.email);

        //Setting up profile picture
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        PicassoManager.loadPaths(cw); //we load the paths
        PicassoManager.applyRoundPicture(picture); //then boom, we apply the picture, simple


        //This is to populate the user information in the sidebar
        username.setText(PlayerManager.getCurrentPlayer().getUsername());
        email.setText(PlayerManager.getCurrentPlayer().getEmail());

        //Using camera sensor to take picture
        picture.setOnClickListener(v -> {
            Intent cam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cam, 0);
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) {
            Bitmap bmap = (Bitmap) data.getExtras().get("data");
            try {
                FileOutputStream out = new FileOutputStream(imagefile);
                bmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
                PicassoManager.applyRoundPicture(picture);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}