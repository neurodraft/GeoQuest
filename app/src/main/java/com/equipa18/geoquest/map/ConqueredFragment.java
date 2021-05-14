package com.equipa18.geoquest.map;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;

import com.equipa18.geoquest.R;
import com.equipa18.geoquest.world.InterestPoint;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class ConqueredFragment extends Fragment {

    private InterestPoint interestPoint;
    private List<InterestPoint> unlockedPoints;



    public ConqueredFragment(InterestPoint interestPoint, List<InterestPoint> unlockedPoints) {
        this.interestPoint = interestPoint;
        this.unlockedPoints = unlockedPoints;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conquered, container, false);

        /*
        try {
            InputStream is = getContext().getAssets().open("images/" + interestPoint.imageFile);
            Bitmap bmp = BitmapFactory.decodeStream(is);
            ((ImageView)view.findViewById(R.id.interest_point_image)).setImageBitmap(bmp);
        } catch (IOException e) {
            e.printStackTrace();
        }

         */

        // Converts 14 dip into its equivalent px
        float dip = 100f;
        Resources r = getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip,
                r.getDisplayMetrics()
        );

        for(int i = 0; i < unlockedPoints.size(); i++){
            InterestPoint unlockedPoint = unlockedPoints.get(i);
            try {
                InputStream is = getContext().getAssets().open("images/" + unlockedPoint.imageFile);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                ImageView imageView = new ImageView(getContext());
                imageView.setImageBitmap(bmp);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new LinearLayout.LayoutParams((int)px, ViewGroup.LayoutParams.MATCH_PARENT));
                ((LinearLayout)view.findViewById(R.id.layout_unlocked)).addView(imageView);

                if(i < unlockedPoints.size() - 1) {
                    Space space = new Space(getContext());
                    space.setLayoutParams(new LinearLayout.LayoutParams((int) px / 8, ViewGroup.LayoutParams.MATCH_PARENT));
                    ((LinearLayout) view.findViewById(R.id.layout_unlocked)).addView(space);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        setActions(view);

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