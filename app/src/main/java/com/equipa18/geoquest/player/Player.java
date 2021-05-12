package com.equipa18.geoquest.player;

import com.equipa18.geoquest.InterestPoint;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<InterestPoint> conqueredPoints;
    private List<InterestPoint> unlockedPoints;

    public Player() {
        conqueredPoints = new ArrayList<>();
        unlockedPoints = new ArrayList<>();
    }

    public void unlockPoint(InterestPoint interestPoint){
        unlockedPoints.add(interestPoint);
    }

    public void conquerPoint(InterestPoint interestPoint){
        conqueredPoints.add(interestPoint);
    }

    public boolean hasConquered(InterestPoint interestPoint){
        return conqueredPoints.contains(interestPoint);
    }

    public boolean hasUnlocked(InterestPoint interestPoint){
        return unlockedPoints.contains(interestPoint);
    }
}
