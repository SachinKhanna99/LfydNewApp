package com.letsdevelopit.lfydnewapp.Login;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialPickerConfig;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.letsdevelopit.lfydnewapp.Info.LocActivity;
import com.letsdevelopit.lfydnewapp.MainActivity;
import com.letsdevelopit.lfydnewapp.R;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    boolean doubleBackToExitPressedOnce = false;


    FloatingActionButton checkotp;

    EditText etphone,etotp;

    Button sendotp;

    ProgressBar progressBar;


    private GoogleApiClient mGoogleApiClient;
    private int RC_HINT = 2;
    private int RESOLVE_HINT = 2;
    private String phoneMain;
    String otprecived;
    private String verificationcode;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mref = database.getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser userc;
    private String UID;
    private String type;

//    TextView changenum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sendotp = findViewById(R.id.sendotp);
        checkotp = findViewById(R.id.checkotp);

        etphone = findViewById(R.id.etphone);
        etotp = findViewById(R.id.etotp);



//        changenum = findViewById(R.id.changenum);

        progressBar = findViewById(R.id.progressbar);

        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                progressBar.setVisibility(View.VISIBLE);
                verifynum();

            }
        });

//        changenum.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                phonell.setVisibility(View.VISIBLE);
//                otpll.setVisibility(View.GONE);
//                etphone.setText("");
//
//            }
//        });


        checkotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Log.d("msg","pressed");
                String otp = etotp.getText().toString();

                if(otp.isEmpty() || otp.equals(""))
                {
                    etotp.setError("Please Enter the OTP");
                    etotp.requestFocus();
                    progressBar.setVisibility(View.GONE);

                }
                else {
                    try {

                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationcode, otp);
                        signInWithPhoneAuthCredential(credential);

                    }catch (Exception e)
                    {
                        Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);

                    }
                }
            }
        });




        String[] NECESSARY_PERMISSIONS = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NECESSARY_PERMISSIONS = new String[]{Manifest.permission.READ_PHONE_NUMBERS};
        }

        if (ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.READ_PHONE_NUMBERS) == PackageManager.PERMISSION_GRANTED) {
        } else {

            if (NECESSARY_PERMISSIONS != null) {
                ActivityCompat.requestPermissions(
                        LoginActivity.this,
                        NECESSARY_PERMISSIONS, 123);
            } else {
            }
        }


        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Auth.CREDENTIALS_API)
                .build();


        showHint();
    }


    private void checkdatapresent() {

        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String uid = userc.getUid();

                boolean check = dataSnapshot.child("Merchant").child(uid).exists();

                if (check) {
                    Toast.makeText(LoginActivity.this, "OTP Verified", Toast.LENGTH_SHORT).show();


                    Toast.makeText(LoginActivity.this, "Already Registered..", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    finish();


                } else {


                    Toast.makeText(LoginActivity.this, "Enter the Details..", Toast.LENGTH_SHORT).show();


                    //mref.child("Merchant").child(UID).child("phone").setValue(phoneMain);

                    Toast.makeText(LoginActivity.this, "OTP Verified", Toast.LENGTH_SHORT).show();
//                            k = 1;
//                            checkdata(UID);
                    Intent intent = new Intent(LoginActivity.this, LocActivity.class);
                    intent.putExtra("uid",UID
                    );
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    private void showHint() {
        HintRequest hintRequest = new HintRequest.Builder()
                .setHintPickerConfig(new CredentialPickerConfig.Builder()
                        .setShowCancelButton(true)
                        .build())
                .setPhoneNumberIdentifierSupported(true)
                .build();

        PendingIntent intent =
                Auth.CredentialsApi.getHintPickerIntent(mGoogleApiClient, hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(), RC_HINT, null, 0, 0, 0,new Bundle());
        } catch (IntentSender.SendIntentException e) {
            Log.e("Login", "Could not start hint picker Intent", e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_HINT) {
            if (resultCode == RESULT_OK) {
                Credential cred = data.getParcelableExtra(Credential.EXTRA_KEY);
                etphone.setText(cred.getId().substring(3));
            }
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    private void verifynum() {

        String phone = etphone.getText().toString();

        if (phone.isEmpty() || phone.length() < 10) {
            etphone.setError("Invalid No.");
//            progressBar.setVisibility(View.GONE);
//            tryagain.setVisibility(View.GONE);


            progressBar.setVisibility(View.GONE);

            etphone.requestFocus();
        } else {
            phone = "+91" + phone;
            phoneMain = phone;
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phone,
                    40,
                    TimeUnit.SECONDS,
                    this,
                    mcallbacks
            );
        }


    }





    PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // Log.d("app:", "comp");
            String st;
            st = phoneAuthCredential.getSmsCode();
            otprecived = st;

            if (st == null) {
//
//                progressBar.setVisibility(View.GONE);
//                tryagain.setVisibility(View.VISIBLE);
//
//                progress.setVisibility(View.GONE);

                Toast.makeText(LoginActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

//                tryagain.setVisibility(View.VISIBLE);
            } else {
                // Log.d("msg", "1");
                signInWithPhoneAuthCredential(phoneAuthCredential);
                etotp.setText(st);
                etphone.setEnabled(false);
                etotp.setEnabled(false);
//                progressBar.setVisibility(View.GONE);
//                tryagain.setVisibility(View.GONE);


                // Log.d("msg", "2");
//                Intent intent = new Intent(PhoneAuth.this, Info.class);
//                // Log.d("msg",phoneMain+" "+UID);
//                intent.putExtra("phone",phoneMain);
//                intent.putExtra("uid",UID);
//
//                startActivity(intent);
            }
        }


        @Override
        public void onVerificationFailed(FirebaseException e) {
            // Log.d("app:", "not comp" + e);
//            progressBar.setVisibility(View.GONE);
//            tryagain.setVisibility(View.VISIBLE);
//
//            progress.setVisibility(View.GONE);

            progressBar.setVisibility(View.GONE);

            Toast.makeText(LoginActivity.this, "Try Again!!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationcode = s;
            Toast.makeText(LoginActivity.this,"Code sent",Toast.LENGTH_SHORT).show();

        }
    };



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Log.d("msg", "3");
//                            Toast.makeText(PhoneAuth.this, "hlo", Toast.LENGTH_SHORT).show();
                            // Sign in success, update UI with the signed-in user's information
                            // Log.d("app:", "signInWithCredential:success");
                            // Log.d("msg", "4");
                            userc = task.getResult().getUser();
                            UID = userc.getUid();
                            // Log.d("msg", UID);

                            checkdatapresent();

                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
//                            progressBar.setVisibility(View.GONE);
//                            checking.setVisibility(View.GONE);
//                            tryagain.setVisibility(View.VISIBLE);


                            Toast.makeText(LoginActivity.this, "Sign In Failed!", Toast.LENGTH_SHORT).show();
                            Log.w("app", "signInWithCredential:failure", task.getException());

                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);

    }
}
