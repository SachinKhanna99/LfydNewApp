package com.letsdevelopit.lfydnewapp.supportandhelp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatConfig;
import com.freshchat.consumer.sdk.FreshchatUser;
import com.freshchat.consumer.sdk.exception.MethodNotAllowedException;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.letsdevelopit.lfydnewapp.ProfileSection.ProfileActivity;
import com.letsdevelopit.lfydnewapp.R;

public class SupportActivity extends AppCompatActivity {


    private TabLayout tabLayout;
    private ViewPager viewPager;

    Button btn;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mref = database.getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser userc = mAuth.getCurrentUser();

    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);


        btn = findViewById(R.id.button);

        uid = userc.getUid();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phone = userc.getPhoneNumber();
                String mp = phone.substring(3,13);

                // Log.d("msg",mp);

                FreshchatConfig config = new FreshchatConfig("9dd20893-b889-484d-8fbf-5e5b665a6842","38ae104b-a843-491b-9598-005816ee25cb");

                FreshchatUser freshchatUser = Freshchat.getInstance(SupportActivity.this.getApplicationContext()).getUser();

                freshchatUser.setPhone("+91", mp);
                try {
                    Freshchat.getInstance(SupportActivity.this.getApplicationContext()).init(config);

                    Freshchat.getInstance(SupportActivity.this.getApplicationContext()).setUser(freshchatUser);
                } catch (MethodNotAllowedException e) {
                    e.printStackTrace();
                    // Log.d("msg", String.valueOf(e));
                }


                Freshchat.showConversations(SupportActivity.this);

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
