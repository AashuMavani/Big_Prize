package com.app.bigprize.Adapter;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

public class Big_FAQsParentView implements Parent<Big_FAQsChildView> {

    // a recipe contains several ingredients
    private String question;
    private List<Big_FAQsChildView> answer;

    public Big_FAQsParentView(String question, List<Big_FAQsChildView> answer) {
        this.question = question;
        this.answer = answer;
    }

    @Override
    public List<Big_FAQsChildView> getChildList() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}