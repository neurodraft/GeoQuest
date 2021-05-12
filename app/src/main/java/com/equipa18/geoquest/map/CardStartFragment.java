package com.equipa18.geoquest.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.equipa18.geoquest.R;

import java.io.IOException;
import java.io.InputStream;


public class CardStartFragment extends Fragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card_start, container, false);
        Bundle b = getArguments();

        try {
            InputStream is = getContext().getAssets().open("images/" + b.getString("imageFile"));
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
                        .replace(R.id.card_content, LearnMoreFragment.class, getArguments())
                        .addToBackStack(null)
                        .commit();

            }
        });
        leaderboard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                getParentFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.card_content, LeaderboardFragment.class, getArguments())
                        .addToBackStack(null)
                        .commit();

            }
        });
        quizz_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MapsFragment mapsFragment = (MapsFragment)getParentFragmentManager().getFragments().get(0);

                mapsFragment.conqueredCurrentPoint();
            }
        });

    }
}