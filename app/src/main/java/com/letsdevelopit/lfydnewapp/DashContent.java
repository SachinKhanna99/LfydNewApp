package com.letsdevelopit.lfydnewapp;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.letsdevelopit.lfydnewapp.ProfileSection.BusinessInfo.BusinessEdit;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DashContent extends AppCompatActivity {


    Button addshop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_content);

        addshop = findViewById(R.id.addshop);

        addshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DashContent.this, BusinessEdit.class);
                startActivity(intent);
                finish();
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

            Intent intentList = new Intent(getApplicationContext(), DashContent.class);
            intentList.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intentList);
            overridePendingTransition(0,0);
            super.onBackPressed();

    }
}
