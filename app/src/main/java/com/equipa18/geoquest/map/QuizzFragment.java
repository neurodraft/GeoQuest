package com.equipa18.geoquest.map;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
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
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuizzFragment extends Fragment {

    private QuizzViewModel mViewModel;

    private InterestPoint interestPoint;
    private Quizz quizz;

    private CountDownTimer countdown;
    private ProgressBar progressBar;

    private List<Button> optionButtons;

    private TextView questionText;
    private TextView questionTitle;
    private int score;
    private Button buttonHelp;

/*
    public static QuizzFragment newInstance() {
        return new QuizzFragment();
    }

 */

    public QuizzFragment(InterestPoint interestPoint) {
        this.interestPoint = interestPoint;

        this.quizz = WorldManager.getNewQuizz(interestPoint);

        score = 0;
    }

    private void showNextQuestion(){
        QuizzQuestion question = quizz.getNextQuestion();

        if(question == null){
            if(score > 0){
                MapsFragment mapsFragment = (MapsFragment)getParentFragmentManager().getFragments().get(0);
                mapsFragment.conqueredCurrentPoint();
            }
            return;
        }
        questionTitle.setText(question.getQuestionTitle());

        questionText.setText(question.getQuestionText());

        buttonHelp.setEnabled(true);

        List<QuizzAnswer> options = question.getOptions();
        for(int i = 0; i < options.size(); i++){
            Button optionButton = optionButtons.get(i);
            optionButton.setEnabled(true);
            optionButton.setText(options.get(i).getAnswerText());


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

        buttonHelp = view.findViewById(R.id.button_help);


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


        for(int i = 0; i < optionButtons.size(); i++){
            Button optionButton = optionButtons.get(i);
            int finalI = i;
            optionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pressedOption(finalI);
                }
            });
        }

        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View v) {
                Random r = new Random();
                List<Integer> ordered = new ArrayList<>(List.of(0,1,2,3));

                int removedButtons = 0;

                while(removedButtons < 2){
                    int index = r.nextInt(ordered.size());
                    Integer value = ordered.get(index);

                    if(quizz.getCurrentQuestion().isAnswerCorrect(value)){
                        ordered.remove(index);
                    } else {
                        optionButtons.get(value).setEnabled(false);
                        ordered.remove(index);

                        removedButtons++;
                    }
                }
                buttonHelp.setEnabled(false);

            }
        });

    }

    private void pressedOption(int i) {
        QuizzQuestion question = quizz.getCurrentQuestion();


        if(question.isAnswerCorrect(i)){
            Snackbar.make(getView(), "Resposta Correcta!", Snackbar.LENGTH_LONG).show();
            score += 1;
        } else{
            Snackbar.make(getView(), "Resposta Incorrecta...", Snackbar.LENGTH_LONG).show();
            score -= 1;
        }
        showNextQuestion();
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