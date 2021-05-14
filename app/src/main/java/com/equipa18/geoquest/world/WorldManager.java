package com.equipa18.geoquest.world;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class WorldManager {
    private static List<InterestPoint> interestPoints;

    private static Map<String, List<String>> categoryAnswerMap;


    public static List<InterestPoint> getInterestPoints(){
        if(interestPoints == null){
            interestPoints = new ArrayList<>();
            categoryAnswerMap = new HashMap<>();
        }
        return interestPoints;
    }

    public static void loadInterestPoints(InputStream is) {
        InputStreamReader isr = new InputStreamReader(is);

        // use GSON library to construct a list of InterestPoint instances from the json file
        Gson gson = new Gson();
        Type interestPointListType = new TypeToken<ArrayList<InterestPoint>>(){}.getType();
        interestPoints = gson.fromJson(isr, interestPointListType);

        categoryAnswerMap = new HashMap<>();

        for(InterestPoint interestPoint : interestPoints){
            for(QuestionData data : interestPoint.quizz){
                if(!categoryAnswerMap.containsKey(data.category)){
                    categoryAnswerMap.put(data.category, new ArrayList<>());
                }
                categoryAnswerMap.get(data.category).add(data.answer);
            }
        }

    }

    public static Quizz getNewQuizz(InterestPoint interestPoint){
        Quizz quizz = new Quizz();
        Random r = new Random();


        for(int i = 0; i< 5; i++){

            QuizzQuestion question = new QuizzQuestion( "Pergunta " + (i+1) + " de 5",
                    interestPoint.quizz[i].question);

            List<String> incorrectAnswers = getIncorrectAnswers(interestPoint.quizz[i].answer, interestPoint.quizz[i].category);

            int correctAnswerIndex = r.nextInt(4);
            int cursor = 0;
            for(int j = 0; j < 4; j++){
                QuizzAnswer answer;
                if(j == correctAnswerIndex){
                    answer = new QuizzAnswer(interestPoint.quizz[i].answer);
                    question.setCorrectAnswer(answer);
                } else{

                    answer = new QuizzAnswer(incorrectAnswers.get(cursor++));
                }
                question.addAnswer(answer);

            }
            quizz.addQuestion(question);
        }
        return quizz;
    }

    private static List<String> getIncorrectAnswers(String correctAnswer, String category){
        List<String> incorrectAnswers = new ArrayList<>();
        Random r = new Random();

        List<Integer> triedIndexes = new ArrayList<>();

        List<String> categoryAnswers = categoryAnswerMap.get(category);

        while (incorrectAnswers.size() < 3){
            int index = r.nextInt(categoryAnswers.size());
            if(triedIndexes.contains(index)){
                continue;
            }
            String picked = categoryAnswers.get(index);
            if(picked.equalsIgnoreCase(correctAnswer)){
                triedIndexes.add(index);
                continue;
            }
            incorrectAnswers.add(picked);
            triedIndexes.add(index);
        }

        return incorrectAnswers;
    }
}
