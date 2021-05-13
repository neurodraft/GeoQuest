package com.equipa18.geoquest.world;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WorldManager {
    private static List<InterestPoint> interestPoints;


    public static List<InterestPoint> getInterestPoints(){
        if(interestPoints == null){
            interestPoints = new ArrayList<>();
        }
        return interestPoints;
    }

    public static void loadInterestPoints(InputStream is) {
        InputStreamReader isr = new InputStreamReader(is);

        // use GSON library to construct a list of InterestPoint instances from the json file
        Gson gson = new Gson();
        Type interestPointListType = new TypeToken<ArrayList<InterestPoint>>(){}.getType();
        interestPoints = gson.fromJson(isr, interestPointListType);
    }

    public static Quizz getNewQuizz(InterestPoint interestPoint){
        Quizz quizz = new Quizz();

        for(int i = 0; i< 5; i++){
            QuizzQuestion question = new QuizzQuestion( "Pergunta " + (i+1) + " de 5",
                    "<Texto da pergunta "+ (i+1) + " aqui>");
            int correctAnswerIndex = (int) Math.round((Math.random()*3.5)-0.5);
            for(int j = 0; j < 4; j++){
                QuizzAnswer answer = new QuizzAnswer("Resposta "+ (i+1) + "-" + (j+1));
                question.addAnswer(answer);
                if(j == correctAnswerIndex){
                    question.setCorrectAnswer(answer);
                }
            }
            quizz.addQuestion(question);
        }
        return quizz;
    }
}
