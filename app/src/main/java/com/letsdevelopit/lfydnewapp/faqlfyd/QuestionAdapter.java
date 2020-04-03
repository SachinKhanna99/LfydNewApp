package com.letsdevelopit.lfydnewapp.faqlfyd;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.letsdevelopit.lfydnewapp.R;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class QuestionAdapter extends ExpandableRecyclerViewAdapter<AnswerViewHolder, QuestionViewHolder> {
    public QuestionAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public AnswerViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list,parent,false);
        return new AnswerViewHolder(v);
    }

    @Override
    public QuestionViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_recyclerview,parent,false);
        return new QuestionViewHolder(v);
    }

    @Override
    public void onBindChildViewHolder(QuestionViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final Question question=(Question) group.getItems().get(childIndex);
        holder.bind(question);
    }

    @Override
    public void onBindGroupViewHolder(AnswerViewHolder holder, int flatPosition, ExpandableGroup group) {
        final Answer answer=(Answer)group;
        holder.bind(answer);
    }


}
