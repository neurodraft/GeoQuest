package com.equipa18.geoquest;

import android.content.ContextWrapper;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.equipa18.geoquest.player.Player;
import com.equipa18.geoquest.player.PlayerManager;
import com.google.android.material.textfield.TextInputEditText;

public class StatisticsActivity extends AppCompatActivity {
    private ImageButton back;
    private TextView score, monumentsConq, remainingMonuments, failedAttempts;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_statistics);
        back = findViewById(R.id.backArrowImageButtonStatistics);
        populateStats();
        back.setOnClickListener(v ->
            onBackPressed()
        );
    }

    private void populateStats() {
        Player p = PlayerManager.getCurrentPlayer();
        score = findViewById(R.id.editTextTime2);
        monumentsConq = findViewById(R.id.monumentsConqueredTextNumber);
        remainingMonuments = findViewById(R.id.monumentsLeftTextNumber);
        failedAttempts = findViewById(R.id.failedAttemptsTextNumber);
        score.setText(String.valueOf(p.getScore()));
        monumentsConq.setText(String.valueOf(p.getConqueredMonuments()));
        remainingMonuments.setText(String.valueOf(p.getRemainingMonuments()));
        failedAttempts.setText(String.valueOf(p.getFailedAttempts()));
    }

}
