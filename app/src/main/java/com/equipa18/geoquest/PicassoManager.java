package com.equipa18.geoquest;

import android.content.Context;
import android.content.ContextWrapper;
import android.widget.ImageView;

import com.equipa18.geoquest.player.PlayerManager;
import com.squareup.picasso.Picasso;

import java.io.File;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class PicassoManager {
    private static File sd;
    private static File imagefile;
    private static String filename;

    public static void applyRoundPicture(ImageView imageview) {
        if(imagefile.exists()){
            Picasso.get().invalidate(imagefile);
            Picasso.get().load(imagefile).transform(new CropCircleTransformation()).into(imageview);
        }
    }

    public static void loadPaths(ContextWrapper context) {
        sd = context.getDir("storage", Context.MODE_PRIVATE); //This is needed if api 30 on android 11, i don't know why, but it is what it is
        filename = PlayerManager.getCurrentPlayer().getUsername();
        imagefile = new File(sd, filename + ".png");
    }
}
