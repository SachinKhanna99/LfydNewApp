package com.letsdevelopit.lfydnewapp.ui.wallet.AddMoney;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.letsdevelopit.lfydnewapp.R;
import com.letsdevelopit.lfydnewapp.ui.wallet.WalletFragment;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class AddMoneyFragment extends Fragment {

    private AddMoneyViewModel addMoneyViewModel;
    EditText amount, note, name, upivirtualid;
    Button send;
    String TAG = "main";
    final int UPI_PAYMENT = 0;
    String moneyamout;

    String uid;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mref = database.getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser userc = mAuth.getCurrentUser();

    FragmentTransaction fragmentTransaction;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addMoneyViewModel =
                ViewModelProviders.of(this).get(AddMoneyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_add_money, container, false);

        root.setBackgroundColor(Color.WHITE);




        send = root.findViewById(R.id.send);
        amount = root.findViewById(R.id.amount_et);
        note = root.findViewById(R.id.note);
        name = root.findViewById(R.id.name);
        upivirtualid = root.findViewById(R.id.upi_id);


        uid = userc.getUid();

        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

        // setDrawerLocked(true);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moneyamout= String.valueOf(amount.getText());
                if (TextUtils.isEmpty(name.getText().toString().trim())) {
                    Toast.makeText(getActivity(), " Name is invalid", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(upivirtualid.getText().toString().trim())) {
                    Toast.makeText(getActivity(), " UPI ID is invalid", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(note.getText().toString().trim())) {
                    Toast.makeText(getActivity(), " Note is invalid", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(moneyamout)) {
                    Toast.makeText(getActivity(), " Amount is invalid", Toast.LENGTH_SHORT).show();
                } else {
                    payUsingUpi(name.getText().toString(), upivirtualid.getText().toString(),
                            note.getText().toString(), moneyamout);


                }



            }
        });


        return root;
    }




    private void addamount() {


        mref.child("Merchant").child(uid).child("wallet").child("money").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String money = String.valueOf(dataSnapshot.getValue());

                long mm = Long.parseLong(money);
                long add = Long.parseLong(moneyamout);

                mm = mm + add;
                mref.child("Merchant").child(uid).child("wallet").child("money").setValue(mm);

                fragmentTransaction.replace(R.id.nav_host_fragment, new WalletFragment());
                fragmentTransaction.commit();




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

        if (null != chooser.resolveActivity(getActivity().getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(getActivity(), "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
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
        if (isConnectionAvailable(getActivity())) {
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
                addamount();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new WalletFragment());
                fragmentTransaction.commit();


                Toast.makeText(getActivity(), "Transaction successful.", Toast.LENGTH_SHORT).show();


            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(getActivity(), "Payment cancelled by user.", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getActivity(), "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();

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