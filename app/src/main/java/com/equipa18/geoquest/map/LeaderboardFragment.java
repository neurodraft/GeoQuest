package com.equipa18.geoquest.map;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.equipa18.geoquest.player.PlayerManager;
import com.equipa18.geoquest.world.InterestPoint;
import com.equipa18.geoquest.R;
import com.equipa18.geoquest.world.WorldManager;


public class LeaderboardFragment extends Fragment {
    private InterestPoint interestPoint;

    public LeaderboardFragment(InterestPoint interestPoint) {
        this.interestPoint = interestPoint;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);


        setActions(view);

        String ranking = PlayerManager.getRanking(interestPoint.id);

        ((TextView)view.findViewById(R.id.leaderboard_textView)).setText(ranking);

        return view;
    }


    private void setActions(View view) {
        Button button_return = view.findViewById(R.id.button_return);

        button_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
    }
}