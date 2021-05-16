package com.equipa18.geoquest;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.equipa18.geoquest.player.PlayerManager;
import com.equipa18.geoquest.world.WorldManager;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

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
        setTheme(R.style.Theme_GeoQuest_NoActionBar);
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_GeoQuest_PopupOverlay);
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
                R.id.nav_home, R.id.nav_gallery, R.id.nav_settings)
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
        sd = cw.getDir("storage", Context.MODE_PRIVATE); //This is needed if api 30 on android 11, i don't know why, but it is what it is
        filename = PlayerManager.getCurrentPlayer().getUsername();
        imagefile = new File(sd, filename + ".png");
        applyProfilePicture(imagefile, picture); //this only applies if file exists.


        //This is to populate the user information in the sidebar
        username.setText(PlayerManager.getCurrentPlayer().getUsername());
        email.setText(PlayerManager.getCurrentPlayer().getEmail());

        //Using camera sensor to take picture
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cam, 0);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bmap = (Bitmap) data.getExtras().get("data");
        try {
            FileOutputStream out = new FileOutputStream(imagefile);
            bmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            applyProfilePicture(imagefile, picture);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void applyProfilePicture(File file, ImageView imageview) {
        if(file.exists()){
            Picasso.get().invalidate(file);
            Picasso.get().load(file).transform(new CropCircleTransformation()).into(imageview);
        }
    }

}