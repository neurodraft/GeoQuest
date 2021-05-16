package com.equipa18.geoquest.player;

import android.annotation.TargetApi;
import android.os.Build;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

import java.util.Map;
import java.util.Set;


public class Player implements Serializable {
    private boolean unitialized;
    private Map<Integer, Integer> conqueredPointsScoreMap;
    private String name;
    private String email;
    private String password;
    private Set<Integer> unlockedPoints;

    private Map<Integer, Long> failedAttempts;

    private int score;

    public Player() {
        this("Default", "Default@Default.com", "default");
    }

    //Constructor for register
    public Player(String name, String email, String password) {
        this.unitialized = true;
        conqueredPointsScoreMap = new HashMap<>();
        unlockedPoints = new HashSet<>();
        failedAttempts = new HashMap<>();
        score = 0;
        validatePlayerStrings(name, email, password);
    }

    private void validatePlayerStrings(String name, String email, String password) {
        this.name = "Default";
        this.email = "Default@Default.com";
        this.password = "default";
        if(checkString(name)) {
            this.name = name;
        }
        if(checkString(email)) {
            this.email = email;
        }
        if(checkString(password)) {
            this.password = password;
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private boolean checkString(String s) {
        boolean valid = true;
        if(s == null || s.isEmpty()) {
            valid = false;
        }
        return valid;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public int getScore() {
        return score;
    }

    public int getRemainingMonuments() {
        return 22 - unlockedPoints.size();
    }

    public int getFailedAttempts() {
        int i = 0;
        for (Long l:failedAttempts.values()) {
            i += l;
        }
        return i;
    }

    public int getConqueredMonuments() {
        return conqueredPointsScoreMap.size();
    }
}
