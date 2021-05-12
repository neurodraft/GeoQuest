package com.equipa18.geoquest.map;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.equipa18.geoquest.R;

import java.io.IOException;
import java.io.InputStream;

public class InterestPointFragment extends Fragment {

    private ImageView headerIcon;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interest_point, container, false);

        headerIcon = view.findViewById(R.id.header_icon);

        Bundle b = getArguments();

        if(b.getBoolean("isConquered")){
            headerIcon.setColorFilter(Color.GREEN);
        } else if(b.getBoolean("isUnlocked")){
            headerIcon.setColorFilter(Color.RED);
        }

        ((TextView)view.findViewById(R.id.interest_point_title)).setText(b.getString("title"));

        getParentFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.card_content, CardStartFragment.class, getArguments())
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