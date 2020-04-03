package com.letsdevelopit.lfydnewapp.privacy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.letsdevelopit.lfydnewapp.ProfileSection.ProfileActivity;
import com.letsdevelopit.lfydnewapp.R;

public class PrivacyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
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
