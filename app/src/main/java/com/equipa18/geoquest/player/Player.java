package com.equipa18.geoquest.player;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.equipa18.geoquest.world.InterestPoint;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class Player {
    private boolean unitialized;

    private Map<Integer, Integer> conqueredPointsScoreMap;
    private Set<Integer> unlockedPoints;

    private Map<Integer, Long> failedAttempts;

    private int score;

    public Player() {
        this.unitialized = true;
        conqueredPointsScoreMap = new HashMap<>();
        unlockedPoints = new HashSet<>();
        failedAttempts = new HashMap<>();
        score = 0;
    }

    public void unlockPoint(int interestPointId){
        unlockedPoints.add(interestPointId);
    }

    public void conquerPoint(int interestPointId, int score){
        conqueredPointsScoreMap.put(interestPointId, score);
        this.score += score;
    }

    public boolean hasConquered(int interestPointId){
        return conqueredPointsScoreMap.containsKey(interestPointId);
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

    public void failedAttempt(int interestPointId){
        failedAttempts.put(interestPointId, System.currentTimeMillis());
    }

    public boolean hasCooldown(int interestPointId){
        if(failedAttempts.containsKey(interestPointId)){
            long timeSince = System.currentTimeMillis() -  failedAttempts.get(interestPointId);
            if(timeSince >= 60000){
                failedAttempts.remove(interestPointId);
                return false;
            }
            return true;
        }
        return false;
    }

    public long getCooldown(int interestPointId){
        if(failedAttempts.containsKey(interestPointId)){
            long timeSince = System.currentTimeMillis() -  failedAttempts.get(interestPointId);
            if(timeSince >= 60000){
                failedAttempts.remove(interestPointId);
                return 0;
            }
            return timeSince;
        }
        return 0;
    }
}
