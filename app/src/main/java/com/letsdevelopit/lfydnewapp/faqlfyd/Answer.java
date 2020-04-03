package com.letsdevelopit.lfydnewapp.faqlfyd;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class Answer extends ExpandableGroup<Question> {
    //GROUP MODEL
    public Answer(String title, List items) {
        super(title, items);
    }

//    protected Answer(Parcel in) {
//        super(in);
//    }
}
