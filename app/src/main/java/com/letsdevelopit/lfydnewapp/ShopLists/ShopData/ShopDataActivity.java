package com.letsdevelopit.lfydnewapp.ShopLists.ShopData;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.letsdevelopit.lfydnewapp.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

public class ShopDataActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mref = database.getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser userc = mAuth.getCurrentUser();

    String uid;

    CarouselView carouselView;
    ImageView bookmark;


    ArrayList<String> images = new ArrayList<String>();
    private String suid;
    private String sname , address,cash,dis,distance,phone;


    TextView s_name,add,cashback,discount,dist;
    private String type;
    private TextView s_type,getLoc;
    private ImageView phone_call;
    private String latitude, longitude,la,lon;
    private String book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_data);

            carouselView = (CarouselView) findViewById(R.id.carouselView);
            uid = userc.getUid();


        add = findViewById(R.id.address);
        cashback = findViewById(R.id.cashbacktv);
        discount = findViewById(R.id.discount);
        bookmark = findViewById(R.id.favourite);
        s_name = findViewById(R.id.shopname);
        s_type = findViewById(R.id.type);

        phone_call = findViewById(R.id.phone_call);

        dist = findViewById(R.id.distancetv);
        getLoc = findViewById(R.id.getloc);


        bookmark.setVisibility(View.GONE);


        final Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        suid = bundle.getString("suid");
        sname = bundle.getString("sname");
        address = bundle.getString("address");
        cash = bundle.getString("cash");
        dis = bundle.getString("dis");
        distance = bundle.getString("distance");
        type = bundle.getString("type");
        la = bundle.getString("lat");
        lon = bundle.getString("lon");
        book = bundle.getString("book");

        add.setText(address);
        s_name.setText(sname);
        cashback.setText(cash);
        dist.setText(distance);
        discount.setText(dis);
        s_type.setText(type);


//        // Log.d("msggggg", suid +"  "+la);

        if (book.equals("no")) {
            bookmark.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
            bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    book = "yes";

                    mref.child("Bookmark").child(uid).child(sname).child("book").setValue("yes");
                    mref.child("Bookmark").child(uid).child(sname).child("suid").setValue(suid);

                    bookmark.setImageResource(R.drawable.ic_bookmark_black_24dp);



                }
            });
        }
        else if(book.equals("yes")){
            bookmark.setImageResource(R.drawable.ic_bookmark_black_24dp);

            bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    book = "no";

                    bookmark.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                    mref.child("Bookmark").child(uid).child(sname).child("book").setValue("no");
                    mref.child("Bookmark").child(uid).child(sname).child("suid").setValue(suid);


                }
            });


        }



        assert suid != null;
//        // Log.d("msg",la);

            mref.child("Merchant").child(suid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String img1 = String.valueOf(dataSnapshot.child("shop").child("simage1").getValue());

                    String img2 = String.valueOf(dataSnapshot.child("shop").child("simage2").getValue());

                    String img3 = String.valueOf(dataSnapshot.child("shop").child("simage3").getValue());

                    phone =  String.valueOf(dataSnapshot.child("shop").child("sphone").getValue());

                    images.add(img1);
                    images.add(img2);
                    images.add(img3);

                    latitude =  String.valueOf(dataSnapshot.child("shop").child("lat").getValue());

                    longitude =  String.valueOf(dataSnapshot.child("shop").child("lon").getValue());
                    

//                    // Log.d("msg",img1);


                    carouselView.setPageCount(images.size());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            carouselView.setImageListener(imageListener);

            phone_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+phone));
                    startActivity(intent);
                }
            });

            getLoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String loc = "http://maps.google.com/maps?saddr="+la+","+lon+"&daddr="+latitude+","+longitude;

                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(loc));
                    startActivity(intent);

//
//                    String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//                    startActivity(intent);
                }
            });
        }

        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {

                Glide.with(getApplicationContext())
                        .load(images.get(position))
                        .into(imageView);
//                imageView.setImageResource(sampleImages[position]);
            }
        };




 }

