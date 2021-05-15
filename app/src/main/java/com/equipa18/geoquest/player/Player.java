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
    private String name;
    private String email;
    private String password;
    private Set<Integer> conqueredPoints;
    private Set<Integer> unlockedPoints;

    public Player() {
        this("Default", "Default@Default.com", "default");
    }

    //Constructor for register
    public Player(String name, String email, String password) {
        this.unitialized = true;
        conqueredPoints = new HashSet<>();
        unlockedPoints = new HashSet<>();
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
