package com.letsdevelopit.lfydnewapp.BusinessOffer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.letsdevelopit.lfydnewapp.DashContent;
import com.letsdevelopit.lfydnewapp.MainActivity;
import com.letsdevelopit.lfydnewapp.ProfileSection.ProfileActivity;
import com.letsdevelopit.lfydnewapp.R;

import java.util.Calendar;

public class BusinessOffers extends AppCompatActivity {

    TextView start, end; Button apply;
    EditText discount,extraDiscount;
    DatePickerDialog.OnDateSetListener mDateSetListener, nDateSetListener;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mref = database.getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser userc = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_offers);



        discount = findViewById(R.id.discount);
        extraDiscount = findViewById(R.id.extraDiscount);
        start = findViewById(R.id.start);
        end = findViewById(R.id.end);
        apply = findViewById(R.id.apply);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(BusinessOffers.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month=month+1;
                String date=month+ "/" +day+"/" + year;
                start.setText(date);
            }
        };

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(BusinessOffers.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        nDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        nDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month=month+1;
                String date=month+ "/" +day+"/" + year;
                end.setText(date);
            }
        };


        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (start!=null || end!=null || discount!=null) {
                    String startDate = start.getText().toString().trim();
                    String endDate = end.getText().toString().trim();
                    String disc = discount.getText().toString().trim();
                    String exDisc = extraDiscount.getText().toString().trim();


                    mref.child("Offers").child(userc.getUid()).child("sdate").setValue(startDate);
                    mref.child("Offers").child(userc.getUid()).child("edate").setValue(endDate);
                    mref.child("Offers").child(userc.getUid()).child("discount").setValue(disc);
                    mref.child("Offers").child(userc.getUid()).child("extra_dis").setValue(exDisc);
                }else {
                    Toast.makeText(BusinessOffers.this, "Enter All Info", Toast.LENGTH_SHORT).show();
                }
//                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//                db.child().child(currentUser.getUid()).child().setValue(startDate);
//                db.child().child(currentUser.getUid()).child().setValue(endDate);
//                db.child().child(currentUser.getUid()).child().setValue(disc);
//                db.child().child(currentUser.getUid()).child().setValue(exDisc);
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
