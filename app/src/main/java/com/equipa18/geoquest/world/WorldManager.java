package com.equipa18.geoquest.world;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WorldManager {
    private static List<InterestPoint> interestPoints;


    public static List<InterestPoint> getInterestPoints(){
        if(interestPoints == null){
            interestPoints = new ArrayList<>();
        }
        return interestPoints;
    }

    public static void loadInterestPoints(InputStream is) {
        InputStreamReader isr = new InputStreamReader(is);

        // use GSON library to construct a list of InterestPoint instances from the json file
        Gson gson = new Gson();
        Type interestPointListType = new TypeToken<ArrayList<InterestPoint>>(){}.getType();
        interestPoints = gson.fromJson(isr, interestPointListType);
    }
}
