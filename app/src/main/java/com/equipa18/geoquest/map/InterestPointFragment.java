package com.equipa18.geoquest.map;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.equipa18.geoquest.world.InterestPoint;
import com.equipa18.geoquest.R;
import com.equipa18.geoquest.player.PlayerManager;

public class InterestPointFragment extends Fragment {

    private ImageView headerIcon;
    private InterestPoint interestPoint;

    public InterestPointFragment(InterestPoint interestPoint) {
        this.interestPoint = interestPoint;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interest_point, container, false);

        headerIcon = view.findViewById(R.id.header_icon);


        if(PlayerManager.getCurrentPlayer().hasConquered(interestPoint.id)){
            headerIcon.setColorFilter(Color.GREEN);
        } else if(PlayerManager.getCurrentPlayer().hasUnlocked(interestPoint.id)){
            headerIcon.setColorFilter(Color.RED);
        }

        ((TextView)view.findViewById(R.id.interest_point_title)).setText(interestPoint.name);

        getParentFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.card_content, new CardStartFragment(interestPoint))
                .commit();


        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }


    public void conquered() {
    }

    public void wasConquered() {
        headerIcon.setColorFilter(Color.GREEN);
    }
}