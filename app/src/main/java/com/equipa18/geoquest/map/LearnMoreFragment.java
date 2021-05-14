package com.equipa18.geoquest.map;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.equipa18.geoquest.world.InterestPoint;
import com.equipa18.geoquest.R;


public class LearnMoreFragment extends Fragment {
    private InterestPoint interestPoint;

    public LearnMoreFragment(InterestPoint interestPoint) {
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
        View view = inflater.inflate(R.layout.fragment_learn_more, container, false);

        ((TextView)view.findViewById(R.id.learn_textView)).setText(interestPoint.learnMore);

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