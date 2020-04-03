package com.letsdevelopit.lfydnewapp.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.letsdevelopit.lfydnewapp.DashContent;
import com.letsdevelopit.lfydnewapp.MainActivity;
import com.letsdevelopit.lfydnewapp.ProfileSection.ProfileActivity;
import com.letsdevelopit.lfydnewapp.R;
import com.letsdevelopit.lfydnewapp.SelcetLocActivity;
import com.letsdevelopit.lfydnewapp.ui.myshops.MyShopFragment;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;


import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private LinearLayout mtopic1,mtopic2;
    private int[] mImgIds;
    private LayoutInflater mInflater;
    List<ModelGalleryClass> personImages = new ArrayList<>();
    private AdapterGalleryList customAdapter;

    int k = 0;

    CarouselView carouselView;
    ArrayList<String> images = new ArrayList<String>();

    CardView myshop, transaction, offers;

    TextView city,profilelink;
    ImageView menu;

    View root;

    String uid;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mref = database.getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser userc = mAuth.getCurrentUser();

    @Override
    public void onStart() {
        super.onStart();

        mref.child("DashBoardSlide").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String a = ds.getKey();
                    assert a != null;
                    String url = String.valueOf(dataSnapshot.child(a).getValue());

                    images.add(url);
//                    ImageView imageView = new ImageView(getActivity());
//                    Glide.with(getActivity())
//                            .load(url)
//                            .into(imageView);
//                    viewFlipper.addView(imageView);
//                    viewFlipper.setFlipInterval(2500);
//                    viewFlipper.setAutoStart(true);
//                    viewFlipper.startFlipping();
//                    viewFlipper.setInAnimation(getContext(), android.R.anim.slide_in_left);
//                    viewFlipper.setOutAnimation(getContext(), android.R.anim.slide_out_right);

                }

                carouselView.setPageCount(images.size());

            }

            @Override

            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
//        menu = root.findViewById(R.id.menubtn);
        city = root.findViewById(R.id.city);

        carouselView = (CarouselView) root.findViewById(R.id.carouselView);

        myshop = root.findViewById(R.id.myshop);
        transaction = root.findViewById(R.id.transaction);
        offers = root.findViewById(R.id.offers);
        profilelink = root.findViewById(R.id.profilelink);

        profilelink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), ProfileActivity.class);
                startActivity(i);
            }
        });
        uid = userc.getUid();

        carouselView.setImageListener(imageListener);


        mref.child("Merchant").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(uid).child("shop").exists()) {
                    if (dataSnapshot.child(uid).child("shop").child("sname").exists())
                    {

                    }else {
                        Intent intent = new Intent(getContext(), DashContent.class);
                        startActivity(intent);

                    }
//                    Toast.makeText(getContext(), "Shop Added", Toast.LENGTH_SHORT).show();

                } else {
                    Intent intent = new Intent(getContext(), DashContent.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction().addToBackStack("a");
                fragmentTransaction.replace(R.id.nav_host_fragment, new MyShopFragment());
                fragmentTransaction.commit();

//                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
//                navigationView.getMenu().getItem(1).setChecked(true);

                     }
        });

//        transaction.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent i = new Intent(getContext(), DashContent.class);
//                startActivity(i);
//            }
//        });
        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), SelcetLocActivity.class);
                //i.putExtra("uid",userc.getUid());
                startActivity(i);
            }
        });





        final RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.businesstypes);
        GridLayoutManager staggeredGridLayoutManager = new GridLayoutManager(getContext(),4);
        recyclerView.setLayoutManager(staggeredGridLayoutManager); // set LayoutManager to RecyclerView

        k = 0;
        mref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                personImages.clear();

                city.setText(String.valueOf(dataSnapshot.child("Merchant").child(uid).child("basic").child("city").getValue()));

                for (DataSnapshot ds : dataSnapshot.child("DashBoardBusiness").getChildren()) {


                    String a = ds.getKey();
//                    // Log.d("msgg", a);


                    if ( k == 0){
                        String url = String.valueOf(dataSnapshot.child("DashBoardBusiness").child(a).child("img").getValue());
                        String name = String.valueOf(dataSnapshot.child("DashBoardBusiness").child(a).child("name").getValue());
                        personImages.add(new ModelGalleryClass(name, url));

                        if (name.equals("Load More"))
                        {
//                            // Log.d("msggg", "hey");
                            k = 1;
                        }

//                        // Log.d("msggg", url);
//                        // Log.d("msggg", name);
                        customAdapter = new AdapterGalleryList(getContext(), personImages);
                        recyclerView.setAdapter(customAdapter);
                        customAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



//        mref.child("Merchant").child(uid).child("city").addListenerForSingleValueEvent(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                city.setText((CharSequence) dataSnapshot.getValue());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

//        menu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DrawerLayout navDrawer = getActivity().findViewById(R.id.drawer_layout);
//                // If the navigation drawer is not open then open it, if its already open then close it.
//                if(!navDrawer.isDrawerOpen(GravityCompat.START))
//                    navDrawer.openDrawer(GravityCompat.START);
//                else
//                    navDrawer.closeDrawer(GravityCompat.END);
//            }
//        });

        mInflater = LayoutInflater.from(getActivity());
        initData();
        initView();
        return root;
    }

    private void initData()
    {
        mImgIds = new int[] { R.drawable.img, R.drawable.img, R.drawable.img,R.drawable.img, R.drawable.img, R.drawable.img, R.drawable.img,R.drawable.img
        };
    }

    @SuppressLint("SetTextI18n")
    private void initView()
    {
        mtopic1 = (LinearLayout) root.findViewById(R.id.id_content_a);

        for (int i = 0; i < (mImgIds.length); i++)
        {


            View view2 = mInflater.inflate(R.layout.topic_1_card,
                    mtopic1, false);




            ImageView img1 = (ImageView) view2
                    .findViewById(R.id.id_content_a_img);
            img1.setImageResource(mImgIds[i]);
            TextView txt1 = (TextView) view2
                    .findViewById(R.id.id_content_a_text);
            txt1.setText("Text "+i);


            mtopic1.addView(view2);

        }
    }

//    @Override
//    public void onBackPressed() {
//
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
//            Intent startMain = new Intent(Intent.ACTION_MAIN);
//            startMain.addCategory(Intent.CATEGORY_HOME);
//            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(startMain);
//            finish();
//            return;
//        }
//
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce=false;
//            }
//        }, 2000);
//
//    }


    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {

            Glide.with(getActivity())
                    .load(images.get(position))
                    .into(imageView);
//                imageView.setImageResource(sampleImages[position]);
        }
    };
}
