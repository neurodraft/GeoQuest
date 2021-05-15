package com.equipa18.geoquest.player;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PlayerManager {

    private static Player currentPlayer;
    private static Map<String, Player> players;

    public static Player getCurrentPlayer(){
        if(currentPlayer == null){
            currentPlayer = new Player();
        }
        return currentPlayer;
    }

    private static void startPlayerMap() {
        players = new HashMap<>();
    }

    public static boolean insertPlayer(Player p) {
        if(players == null) {
            startPlayerMap();
        }

        boolean success = true;
        if(checkIfPlayerExists(p.getEmail())) {
            success = false;
        } else {
        players.put(p.getEmail(), p);
        setCurrentPlayer(p);
        }

        return success;
    }

    private static void setCurrentPlayer(Player p) {
        if(p != null){
            currentPlayer = p;
        }
    }

    public static Player getPlayerFromMap(String key) {
        return players.get(key);
    }

    private static boolean checkIfPlayerExists(String email) {
        boolean exists = false;
        if(players.containsKey(email)) {
            exists = true;
        }
        return exists;
    }
}
