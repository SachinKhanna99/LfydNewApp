package com.letsdevelopit.lfydnewapp.faqlfyd;

import android.view.View;
import android.widget.TextView;

import com.letsdevelopit.lfydnewapp.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class QuestionViewHolder extends ChildViewHolder {

    //ITEM VIEW HOLDER
    private TextView mtextView;
    public QuestionViewHolder(View itemView) {
        super(itemView);
        mtextView=itemView.findViewById(R.id.textView);
    }
    public void bind(Question question)
    {
        mtextView.setText(question.name);
    }
}
