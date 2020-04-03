package com.letsdevelopit.lfydnewapp.ui.qrscanner;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.letsdevelopit.lfydnewapp.MainActivity;
import com.letsdevelopit.lfydnewapp.R;


import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity {

    TextView upi_id,amtTopay,coinuse;
    final int UPI_PAYMENT = 0;

    EditText amt;
    Button pay;
    CheckBox check;

    LinearLayout coinll,amtll;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mref = database.getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser userc = mAuth.getCurrentUser();

    long ccoins;
    String uid;
    private String m_name;

    @Override
    protected void onStart() {
        super.onStart();

        mref.child("Merchant").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                m_name = String.valueOf(dataSnapshot.child("personal").child("name").getValue());
                //mname.setText(m_name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        final Bundle bundle = getIntent().getExtras();
        final String upiid = bundle.getString("upi_id");
        String upiresult = bundle.getString("upi_result");

        upi_id = findViewById(R.id.upiid);
        amt = findViewById(R.id.amt);
        check = findViewById(R.id.usecoin);
        amtTopay = findViewById(R.id.finalamount);
        coinll = findViewById(R.id.coinll);
        amtll = findViewById(R.id.amtll);
        coinuse = findViewById(R.id.coinuse);

        pay = findViewById(R.id.pay);

        uid = userc.getUid();

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payUsingUpi(m_name,upiid,"LFYD PAYMENT",amtTopay.getText().toString());
            }
        });



        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (check.isChecked()){
                    String amtt = String.valueOf(amt.getText());
                    if (amtt.equals("") || amtt.isEmpty())
                    {
                        amt.setError("Enter the amount");
                        amt.requestFocus();
                    }else {

                        mref.child("Merchant").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                               String co = String.valueOf(dataSnapshot.child("wallet").child("coin").getValue());
                               long coins = Long.parseLong(co);

                               ccoins = coins;

                                String amtt = String.valueOf(amt.getText());

                                long amount = Long.parseLong(amtt);

                                double famount = amount * 0.01;

                                String a = String.valueOf(famount);
                                coinll.setVisibility(View.VISIBLE);
                                amtll.setVisibility(View.VISIBLE);
                                coinuse.setText(a);

                                double amttopay = amount - famount;
                                double coinleft = (double)ccoins - famount;

                                long xy = Math.round(coinleft);
                                long ammmm = Math.round(amttopay);

                               // Toast.makeText(PaymentActivity.this, "coinleft : "+xy, Toast.LENGTH_SHORT).show();

                            mref.child("Merchant").child(uid).child("wallet").child("coin").setValue(xy);
                                String aa = String.valueOf(ammmm);
                                amtTopay.setText(aa);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                }
            }
        });



        upi_id.setText(upiid);




    }

    void payUsingUpi(String name, String upiId, String note, String amount) {

        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)

                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")

                .build();
        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        Intent chooser = Intent.createChooser(upiPayIntent, "Select Payment Method ");

        if (null != chooser.resolveActivity(PaymentActivity.this.getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(PaymentActivity.this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");

                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {

                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //when user simply back without payment

                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(getApplicationContext())) {
            String str = data.get(0);

            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }
            if (status.equals("success")) {

                Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                startActivity(intent);
                finish();


                Toast.makeText(PaymentActivity.this, "Transaction successful.", Toast.LENGTH_SHORT).show();


            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(PaymentActivity.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(PaymentActivity.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();

            }
        }

    }
    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

}
