package com.letsdevelopit.lfydnewapp.ProfileSection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.letsdevelopit.lfydnewapp.BusinessOffer.BusinessOffers;
import com.letsdevelopit.lfydnewapp.Feed.ActivityFeedback;
import com.letsdevelopit.lfydnewapp.Login.LoginActivity;
import com.letsdevelopit.lfydnewapp.MainActivity;

import com.letsdevelopit.lfydnewapp.ProfileSection.Personal.PersonalInfoActivity;
import com.letsdevelopit.lfydnewapp.R;
import com.letsdevelopit.lfydnewapp.faqlfyd.FaqActivity;
import com.letsdevelopit.lfydnewapp.privacy.PrivacyActivity;
import com.letsdevelopit.lfydnewapp.rateus.RateUsActivity;
import com.letsdevelopit.lfydnewapp.supportandhelp.SupportActivity;

public class ProfileActivity extends AppCompatActivity {

    TextView profile,logout, privacy;
    CardView boffer,feedback,refer,rateus,support,faq;

    ImageView mimg;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mref = database.getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser userc = mAuth.getCurrentUser();

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profile = findViewById(R.id.profile_sec);
        logout = findViewById(R.id.logout);
        mimg = findViewById(R.id.merchentimg);
        boffer = findViewById(R.id.businessoffer);
        feedback = findViewById(R.id.feedback);
        refer = findViewById(R.id.refer);
        rateus = findViewById(R.id.rateus);
        support = findViewById(R.id.support);
        faq = findViewById(R.id.faq);
        privacy = findViewById(R.id.privacy);


        uid = userc.getUid();

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, PersonalInfoActivity.class);
                startActivity(intent);
                finish();
            }
        });
        boffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, BusinessOffers.class);
                startActivity(intent);
                finish();
            }
        });
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ActivityFeedback.class);
                startActivity(intent);
                finish();
            }
        });
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, FaqActivity.class);
                startActivity(intent);
                finish();
            }
        });
        rateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, RateUsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, SupportActivity.class);
                startActivity(intent);
                finish();
            }
        });
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, PrivacyActivity.class);
                startActivity(intent);
                finish();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });


        mref.child("Merchant").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String m_url = String.valueOf(dataSnapshot.child("personal").child("image").getValue());
                if (m_url.isEmpty() || m_url == ""){
                    //.setText("Welcome ");
                    mimg.setImageResource(R.drawable.avtar);
                }else {

                    Glide.with(getApplicationContext())
                            .load(m_url)
                            .into(mimg);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mimg.setImageResource(R.drawable.avtar);
            }
        });



    }

    private void logout() {

        Toast.makeText(this, "Hey", Toast.LENGTH_SHORT).show();
        mAuth.signOut();
        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {

        Intent intentList = new Intent(getApplicationContext(), MainActivity.class);
        intentList.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intentList);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onBackPressed();

    }
}
