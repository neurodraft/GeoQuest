package com.equipa18.geoquest.player;

public class PlayerManager {

    private static Player currentPlayer;

    public static Player getCurrentPlayer(){
        if(currentPlayer == null){
            currentPlayer = new Player();
        }
        return currentPlayer;
    }
}
