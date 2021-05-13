package com.equipa18.geoquest.map;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.equipa18.geoquest.R;
import com.equipa18.geoquest.world.InterestPoint;
import com.equipa18.geoquest.world.Quizz;
import com.equipa18.geoquest.world.QuizzAnswer;
import com.equipa18.geoquest.world.QuizzQuestion;
import com.equipa18.geoquest.world.WorldManager;

import java.util.ArrayList;
import java.util.List;

public class QuizzFragment extends Fragment {

    private QuizzViewModel mViewModel;

    private InterestPoint interestPoint;
    private Quizz quizz;

    private CountDownTimer countdown;
    private ProgressBar progressBar;

    private List<Button> optionButtons;

    private TextView questionText;
    private TextView questionTitle;

/*
    public static QuizzFragment newInstance() {
        return new QuizzFragment();
    }

 */

    public QuizzFragment(InterestPoint interestPoint) {
        this.interestPoint = interestPoint;

        this.quizz = WorldManager.getNewQuizz(interestPoint);
    }

    private void showNextQuestion(){
        QuizzQuestion question = quizz.getNextQuestion();

        if(question == null){
            MapsFragment mapsFragment = (MapsFragment)getParentFragmentManager().getFragments().get(0);
            mapsFragment.conqueredCurrentPoint();
            getParentFragmentManager().popBackStack();
            return;
        }
        questionTitle.setText(question.getQuestionTitle());

        questionText.setText(question.getQuestionText());

        int i = 0;
        for(QuizzAnswer answer : question.getOptions()){
            optionButtons.get(i).setText(answer.getAnswerText());
            i++;
        }

        resetCountDown();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.quizz_fragment, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        optionButtons = new ArrayList<>();
        optionButtons.add(view.findViewById(R.id.button_optionA));
        optionButtons.add(view.findViewById(R.id.button_optionB));
        optionButtons.add(view.findViewById(R.id.button_optionC));
        optionButtons.add(view.findViewById(R.id.button_optionD));

        questionTitle = view.findViewById(R.id.question_title);

        questionText = view.findViewById(R.id.question_text);


        setActions(view);

        countdown = new CountDownTimer(10000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress((int)(millisUntilFinished/10000.0*100));
            }

            @Override
            public void onFinish() {
                showNextQuestion();
            }
        };

        showNextQuestion();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(QuizzViewModel.class);
        // TODO: Use the ViewModel
    }

    private void setActions(View view){
        Button buttonGiveUp = view.findViewById(R.id.button_giveup);

        buttonGiveUp.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        for(Button optionButton : optionButtons){
            optionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showNextQuestion();
                }
            });
        }
    }

    private void resetCountDown(){
        progressBar.setProgress(100);
        countdown.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        countdown.cancel();
        countdown = null;
    }

}