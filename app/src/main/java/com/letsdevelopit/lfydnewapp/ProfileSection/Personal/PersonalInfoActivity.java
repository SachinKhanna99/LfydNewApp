package com.letsdevelopit.lfydnewapp.ProfileSection.Personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.letsdevelopit.lfydnewapp.ProfileSection.ProfileActivity;
import com.letsdevelopit.lfydnewapp.R;

public class PersonalInfoActivity extends AppCompatActivity {

    TextView name, dob, phone, address;
    Button edit;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mref = database.getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser userc = mAuth.getCurrentUser();

    String uid, nm, add, phnum, date ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        name = findViewById(R.id.owner_name);
        phone = findViewById(R.id.phone_num);
        dob = findViewById(R.id.dob);
        address = findViewById(R.id.paddress);
        edit = findViewById(R.id.edit);

        uid = userc.getUid();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PersonalInfoActivity.this , PersonalEdit.class);
                startActivity(intent);
                finish();

            }
        });

        mref.child("Merchant").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nm = String.valueOf(dataSnapshot.child("personal").child("name").getValue());
                add = String.valueOf(dataSnapshot.child("personal").child("address").getValue());
                phnum = String.valueOf(dataSnapshot.child("personal").child("phoneNo").getValue());
                date = String.valueOf(dataSnapshot.child("personal").child("dob").getValue());

                if (nm.isEmpty() || nm == "" || nm.equals("null")){
                    name.setText("Name");
                }else {
                    name.setText(nm);
                }

                if (add.isEmpty() || add == "" || add.equals("null")){
                    //.setText("Welcome ");
                    address.setText("Address");
                }else {
                    address.setText(add);
                }

                if (date.isEmpty() || date == "" || date.equals("null")){
                    //.setText("Welcome ");
                    dob.setText("D.O.B");
                }else {
                    dob.setText(date);
                }

                if (phnum.isEmpty() || phnum == "" || phnum.equals("null")){
                    phone.setText("Phone No.");
                }else {
                    phone.setText(phnum);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });




    }

    @Override
    public void onBackPressed() {

        Intent intentList = new Intent(getApplicationContext(), ProfileActivity.class);
        intentList.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intentList);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onBackPressed();

    }

}
