package com.equipa18.geoquest.player;

import com.equipa18.geoquest.world.InterestPoint;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class Player {
    private boolean unitialized;

    private Set<Integer> conqueredPoints;
    private Set<Integer> unlockedPoints;

    public Player() {
        this.unitialized = true;
        conqueredPoints = new HashSet<>();
        unlockedPoints = new HashSet<>();
    }

    public void unlockPoint(int interestPointId){
        unlockedPoints.add(interestPointId);
    }

    public void conquerPoint(int interestPointId){
        conqueredPoints.add(interestPointId);
    }

    public boolean hasConquered(int interestPointId){
        return conqueredPoints.contains(interestPointId);
    }

    public boolean hasUnlocked(int interestPointId){
        return unlockedPoints.contains(interestPointId);
    }

    public boolean isUnitialized() {
        return unitialized;
    }

    public void initialized(){
        unitialized = false;
    }
}
