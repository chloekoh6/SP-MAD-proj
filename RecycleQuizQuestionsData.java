package com.sp.android_studio_project;

import java.util.List;

public class RecycleQuizQuestionsData {

    private String question;
    private List<String> answers;
    private String correctans;
    private int Qpoints;

    public RecycleQuizQuestionsData(){
        //empty constructor for firebase
    }

    public RecycleQuizQuestionsData(String question, List<String> answers, String correctans, int Qpoints) {
        this.question = question;
        this.answers = answers;
        this.correctans = correctans;
        this.Qpoints = Qpoints;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public String getCorrectans() {
        return correctans;
    }

    public int getQpoints() {
        return Qpoints;
    }
}
