package com.equipa18.geoquest.world;

import java.util.ArrayList;
import java.util.List;

public class QuizzQuestion {
    private String questionTitle;
    private String questionText;

    private List<QuizzAnswer> options;
    private QuizzAnswer correctAnswer;

    public QuizzQuestion(String questionTitle, String questionText) {
        this.questionTitle = questionTitle;
        this.questionText = questionText;
        this.options = new ArrayList<>();
    }

    public void addAnswer(QuizzAnswer answer){
        this.options.add(answer);
    }

    public void setCorrectAnswer(QuizzAnswer correctAnswer){
        this.correctAnswer = correctAnswer;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<QuizzAnswer> getOptions() {
        return options;
    }

    public boolean isAnswerCorrect(int i) {
        return options.get(i) == correctAnswer;
    }
}
