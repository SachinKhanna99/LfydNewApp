package com.letsdevelopit.lfydnewapp.Feed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.letsdevelopit.lfydnewapp.ProfileSection.ProfileActivity;
import com.letsdevelopit.lfydnewapp.R;

import java.util.HashMap;

public class ActivityFeedback extends AppCompatActivity {

    TextView aftersubmit;
    Button sumbit, call;
    EditText write;
    CardView ccard, emailcard, writecard;
    String feedback;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        call = findViewById(R.id.call);
        sumbit = findViewById(R.id.submit);
        write = findViewById(R.id.writemail);
        aftersubmit = findViewById(R.id.aftersubmit);
        ccard = findViewById(R.id.callcard);

        writecard = findViewById(R.id.feedbackcard);

        reference = FirebaseDatabase.getInstance().getReference();


        sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback = write.getText().toString();
                if (TextUtils.isEmpty(feedback)) {
                    Toast.makeText(ActivityFeedback.this, "You  cannot write feedback", Toast.LENGTH_SHORT).show();
                } else {
                    saveit();
                }
                aftersubmit.setVisibility(View.VISIBLE);
                ccard.setVisibility(View.INVISIBLE);

                writecard.setVisibility(View.INVISIBLE);
            }
        });
        call.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //TODO iske baad intent lgake main Dashboard Me Bhej Dena


                Intent intent = new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel: +919031716589"));
                startActivity(intent);
            }
        });
    }




    private void saveit() {
        HashMap<String , Object> feed=new HashMap<>();
        feed.put("feedback",feedback);

        reference.child("Feedback").push().updateChildren(feed).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ActivityFeedback.this, "Feedback Send Successfully", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ActivityFeedback.this, "There is some error "+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intentList = new Intent(getApplicationContext(), ProfileActivity.class);
        intentList.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intentList);
        overridePendingTransition(0,0);
        super.onBackPressed();

    }
}
