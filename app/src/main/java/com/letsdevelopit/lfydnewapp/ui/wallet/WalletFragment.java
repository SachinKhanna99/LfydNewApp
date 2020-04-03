package com.letsdevelopit.lfydnewapp.ui.wallet;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.letsdevelopit.lfydnewapp.R;
import com.letsdevelopit.lfydnewapp.ui.wallet.AddMoney.AddMoneyFragment;

public class WalletFragment extends Fragment {

    private WalletViewModel walletViewModel;
    TextView textView,walletcash,walletcoin;
    String aa;
    ImageView addmoney;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mref = database.getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser userc = mAuth.getCurrentUser();

    String uid;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        walletViewModel =
                ViewModelProviders.of(this).get(WalletViewModel.class);
        View root = inflater.inflate(R.layout.fragment_wallet, container, false);
       textView = root.findViewById(R.id.textname);
       addmoney = root.findViewById(R.id.addmoney);
       walletcash = root.findViewById(R.id.wallet_cash);
       walletcoin = root.findViewById(R.id.wallet_coin);

        root.setBackgroundColor(Color.WHITE);
       uid = userc.getUid();

        mref.child("Merchant").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String money = String.valueOf(dataSnapshot.child("wallet").child("money").getValue());
                walletcash.setText(money);

                String coin = String.valueOf(dataSnapshot.child("wallet").child("coin").getValue());
                walletcoin.setText(coin);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        String possibleEmail="";
        try{
            Account[] accounts = AccountManager.get(getContext()).getAccountsByType("com.google");
            for (int i = 0; i<=accounts.length ; i++) {
                possibleEmail = accounts[0].name ;
            }
        }
        catch(Exception e)
        {
            Log.i("Exception", "Exception:"+e) ;
        }
        final String finalPossibleEmail = possibleEmail;
        walletViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                aa = finalPossibleEmail;
                textView.setText(aa);
            }
        });


        addmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new AddMoneyFragment());
                fragmentTransaction.commit();

//                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
//                navigationView.getMenu().getItem(1).setChecked(true);

//                Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    toolbar.setTitle("Events");
//                }
            }
        });

        return root;
    }




}