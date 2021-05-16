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
import com.equipa18.geoquest.player.PlayerManager;
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


    private InterestPoint interestPoint;
    private Quizz quizz;

    private CountDownTimer countdown;
    private ProgressBar progressBar;

    private List<Button> optionButtons;
    private List<TextView> optionTexts;

    private TextView questionText;
    private TextView questionTitle;
    private int score;
    private int correctAnswers;
    private Button buttonHelp;


    public QuizzFragment(InterestPoint interestPoint) {
        this.interestPoint = interestPoint;

        this.quizz = WorldManager.getNewQuizz(interestPoint);

        score = 0;
        correctAnswers = 0;
    }

    private void started(){
        MapsFragment mapsFragment = (MapsFragment)getParentFragmentManager().getFragments().get(0);
        mapsFragment.quizzStarted();
    }
    private void ended(){
        MapsFragment mapsFragment = (MapsFragment)getParentFragmentManager().getFragments().get(0);
        mapsFragment.quizzEnded();
    }


    private void conquered(){
        System.out.println("Score = " + score);
        MapsFragment mapsFragment = (MapsFragment)getParentFragmentManager().getFragments().get(0);
        mapsFragment.conqueredCurrentPoint(score);
    }

    private void failed(){
        PlayerManager.getCurrentPlayer().failedAttempt(interestPoint.id);
        PlayerManager.savePlayers(getContext());
        getParentFragmentManager().popBackStack();
    }

    private void showNextQuestion(){
        QuizzQuestion question = quizz.getNextQuestion();

        if(question == null){

            if(correctAnswers >= 3){
                conquered();
            } else {
                failed();
            }
            ended();
            return;
        }
        questionTitle.setText(question.getQuestionTitle());

        questionText.setText(question.getQuestionText());

        if(PlayerManager.getCurrentPlayer().getScore() < 500){
            buttonHelp.setEnabled(false);

        } else {
            buttonHelp.setEnabled(true);

        }

        List<QuizzAnswer> options = question.getOptions();
        for(int i = 0; i < options.size(); i++){

            Button optionButton = optionButtons.get(i);
            optionButton.setEnabled(true);
            //optionButton.setText(options.get(i).getAnswerText());

            String letter = "";
            switch(i){
                case 0:
                    letter = "A: ";
                    break;
                case 1:
                    letter = "B: ";
                    break;
                case 2:
                    letter = "C: ";
                    break;
                case 3:
                    letter = "D: ";
                    break;

            }

            TextView optionText = optionTexts.get(i);
            optionText.setText(letter + options.get(i).getAnswerText());
            optionText.setTextColor(Color.BLACK);


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

        optionTexts = new ArrayList<>();
        optionTexts.add(view.findViewById(R.id.optionA_text));
        optionTexts.add(view.findViewById(R.id.optionB_text));
        optionTexts.add(view.findViewById(R.id.optionC_text));
        optionTexts.add(view.findViewById(R.id.optionD_text));


        questionTitle = view.findViewById(R.id.question_title);

        questionText = view.findViewById(R.id.question_text);

        buttonHelp = view.findViewById(R.id.button_help);


        setActions(view);

        countdown = new CountDownTimer(30000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress((int)(millisUntilFinished/30000.0*100));
            }

            @Override
            public void onFinish() {
                showNextQuestion();
            }
        };


        showNextQuestion();

        started();

        return view;
    }



    private void setActions(View view){
        Button buttonGiveUp = view.findViewById(R.id.button_giveup);

        buttonGiveUp.setOnClickListener(v -> failed());


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
                        optionTexts.get(value).setTextColor(Color.RED);

                        ordered.remove(index);

                        removedButtons++;
                    }
                }

                PlayerManager.getCurrentPlayer().spendScore(500);
                Snackbar.make(getView(), "Gastou 500 pontos.", Snackbar.LENGTH_LONG).show();


                buttonHelp.setEnabled(false);

            }
        });

    }

    private void pressedOption(int i) {
        QuizzQuestion question = quizz.getCurrentQuestion();


        if(question.isAnswerCorrect(i)){
            Snackbar.make(getView(), "Resposta Correcta!", Snackbar.LENGTH_LONG).show();
            correctAnswers++;

            score += 100 + progressBar.getProgress();

        } else{
            Snackbar.make(getView(), "Resposta Incorrecta...", Snackbar.LENGTH_LONG).show();
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