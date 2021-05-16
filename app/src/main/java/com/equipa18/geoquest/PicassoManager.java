package com.equipa18.geoquest;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.equipa18.geoquest.player.PlayerManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

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

    public static File getImagefile() {
        return imagefile;
    }

    public static boolean savePic(Intent data) {
        boolean success = false;
        if (data != null) {
            Bundle bundle = data.getExtras(); //i hate the fact that i have to do this... so much...
            if(bundle != null) {
                Bitmap bmap = (Bitmap) bundle.get("data");
                try {
                    FileOutputStream out = new FileOutputStream(PicassoManager.getImagefile());
                    bmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                    out.flush();
                    out.close();
                    success = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }
}
