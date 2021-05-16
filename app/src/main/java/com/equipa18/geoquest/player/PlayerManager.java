package com.equipa18.geoquest.player;

import android.content.Context;
import android.os.Build;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.equipa18.geoquest.world.InterestPoint;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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

    public static void savePlayers(Context context) {
        File myFile = new File(context.getFilesDir(), "players.bin");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(myFile);
            ObjectOutputStream oout = new ObjectOutputStream(new BufferedOutputStream(out));
            oout.writeObject(players);

            oout.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void loadPlayers(Context context) {
        File myFile = new File(context.getFilesDir(), "players.bin");
        if(myFile.exists()){
            System.out.println("Loading players from storage");
            FileInputStream in = null;
            try {
                in = new FileInputStream(myFile);
                ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(in));
                players = (Map<String, Player>) oin.readObject();
                oin.close();
                return;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        System.out.println("No players in storage");
        players = new HashMap<>();
    }

    public static boolean login(String email, String password) {
        if(players.containsKey(email)){
            Player p = players.get(email);
            if(p.getPassword().equals(password)){
                setCurrentPlayer(p);
                return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getRanking(int interestPointId) {
        List<Pair<Player, Integer>> allScores = new ArrayList<>();

        for(Player player : players.values()){
            if(player.hasConquered(interestPointId)){
                Pair<Player, Integer> pair = new Pair<>(player, player.getConqueredScore(interestPointId));
                allScores.add(pair);
            }
        }

        allScores.sort(new Comparator<Pair<Player, Integer>>() {
            @Override
            public int compare(Pair<Player, Integer> o1, Pair<Player, Integer> o2) {
                return o2.second - o1.second ;
            }
        });

        String s = "";

        for(int i = 0; i < allScores.size(); i++){
            s += (i+1) + ":\t" + allScores.get(i).first.getUsername() + " (" + allScores.get(i).second + " pts.) \n";
        }

        return s;
    }
}
