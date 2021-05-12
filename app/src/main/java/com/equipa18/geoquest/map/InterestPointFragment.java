package com.equipa18.geoquest.map;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.equipa18.geoquest.R;

import java.io.IOException;
import java.io.InputStream;

public class InterestPointFragment extends Fragment {

    private InterestPointViewModel mViewModel;

    public static InterestPointFragment newInstance() {
        return new InterestPointFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interest_point, container, false);
        Bundle b = getArguments();

        ((TextView)view.findViewById(R.id.interest_point_title)).setText((String)b.get("title"));

        try {
            InputStream is = getContext().getAssets().open("images/" + b.get("imageFile"));
            Bitmap bmp = BitmapFactory.decodeStream(is);
            ((ImageView)view.findViewById(R.id.interest_point_image)).setImageBitmap(bmp);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(InterestPointViewModel.class);
        // TODO: Use the ViewModel
    }

}