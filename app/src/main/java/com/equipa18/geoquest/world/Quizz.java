package com.equipa18.geoquest.world;

import java.util.ArrayList;
import java.util.List;

public class Quizz {
    private List<QuizzQuestion> questions;

    private int currentQuestion = -1;

    public Quizz() {
        this.questions = new ArrayList<>();
    }

    public void addQuestion(QuizzQuestion question){
        questions.add(question);
    }

    public QuizzQuestion getNextQuestion(){
        if(currentQuestion == questions.size()-1){
            return null;
        }
        return questions.get(++currentQuestion);
    }

    public QuizzQuestion getCurrentQuestion(){
        if(currentQuestion == -1 || currentQuestion >= questions.size()){
            return null;
        }
        return questions.get(currentQuestion);
    }
}
