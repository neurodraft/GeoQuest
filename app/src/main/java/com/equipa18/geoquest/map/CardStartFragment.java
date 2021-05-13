package com.equipa18.geoquest.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.equipa18.geoquest.world.InterestPoint;
import com.equipa18.geoquest.R;
import com.equipa18.geoquest.player.PlayerManager;

import java.io.IOException;
import java.io.InputStream;


public class CardStartFragment extends Fragment {
    private InterestPoint interestPoint;

    public CardStartFragment(InterestPoint interestPoint) {
        this.interestPoint = interestPoint;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card_start, container, false);

        try {
            InputStream is = getContext().getAssets().open("images/" + interestPoint.imageFile);
            Bitmap bmp = BitmapFactory.decodeStream(is);
            ((ImageView)view.findViewById(R.id.interest_point_image)).setImageBitmap(bmp);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setActions(view);

        return view;
    }

    private void setActions(View view){
        Button learnMore = view.findViewById(R.id.button_learn_more);
        Button leaderboard = view.findViewById(R.id.button_leaderboard);
        Button quizz_button = view.findViewById(R.id.button_quizz);

        learnMore.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                getParentFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.card_content, new LearnMoreFragment(interestPoint))
                        .addToBackStack(null)
                        .commit();

            }
        });
        leaderboard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                getParentFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.card_content, new LeaderboardFragment(interestPoint))
                        .addToBackStack(null)
                        .commit();

            }
        });

        if(PlayerManager.getCurrentPlayer().hasUnlocked(interestPoint.id)){
            if(PlayerManager.getCurrentPlayer().hasConquered(interestPoint.id)){
                quizz_button.setEnabled(false);
                quizz_button.setText("Quizz Concluído");
            } else {
                quizz_button.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
/*
                        MapsFragment mapsFragment = (MapsFragment)getParentFragmentManager().getFragments().get(0);
                        mapsFragment.conqueredCurrentPoint();

                        quizz_button.setEnabled(false);
                        quizz_button.setText("Quizz Concluído");
                        */
                        getParentFragmentManager().beginTransaction()
                                .setReorderingAllowed(true)
                                .replace(R.id.card_content, new QuizzFragment(interestPoint))
                                .addToBackStack(null)
                                .commit();
                    }
                });
            }

        }else {
            quizz_button.setEnabled(false);
            quizz_button.setText("Quizz Bloqueado");
        }


    }
}