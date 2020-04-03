package com.letsdevelopit.lfydnewapp.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.letsdevelopit.lfydnewapp.CategoriesList.CategoriesList;
import com.letsdevelopit.lfydnewapp.R;
import com.letsdevelopit.lfydnewapp.ShopLists.ShopListActivity;
import com.letsdevelopit.lfydnewapp.Utils;


import java.util.List;

public class AdapterGalleryList extends RecyclerView.Adapter<AdapterGalleryList.Viewholder> {

    Context mcontext;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Activity activity;

    FirebaseUser user = mAuth.getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mref = database.getReference();




    private List<ModelGalleryClass> modelGallClassList;




    public AdapterGalleryList(Context context, List<ModelGalleryClass> modelGallClassList) {
        this.mcontext = context;
        this.modelGallClassList = modelGallClassList;

    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dashrow, viewGroup, false);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder viewholder, final int i) {

        final String url = modelGallClassList.get(i).getURL();
        final String name = modelGallClassList.get(i).getName();


//        // Log.d("msgg",url);
//        // Log.d("msgg",name);
        viewholder.set_data(name,url);



        viewholder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mcontext, ShopListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("type",name);
                mcontext.startActivity(intent);

            }
        });

        if (name.equals("Load More")){
            viewholder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mcontext, CategoriesList.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mcontext.startActivity(intent);

                }
            });
        }

//        if (name.equals("Restaurants & Cafes")){
//            viewholder.cardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Intent intent = new Intent(mcontext, ShopListActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.putExtra("type","RestaurantsandEateries");
//                    mcontext.startActivity(intent);
//
//                }
//            });
//        }
//        if (name.equals("Desktop & Laptop shop")){
//            viewholder.cardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Intent intent = new Intent(mcontext, ShopListActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.putExtra("type","ComputerProducts");
//                    mcontext.startActivity(intent);
//
//                }
//            });
//        }

    }

    @Override
    public int getItemCount() {
        return modelGallClassList.size();
    }


    class Viewholder extends RecyclerView.ViewHolder {

        private ImageView img;
        private TextView name;
        private CardView cardView;



        public Viewholder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            cardView = itemView.findViewById(R.id.cardbusiness);
        }

        private void set_data(String nm,String url) {

//            // Log.d("msgg",url);
            Utils.fetchSvg(mcontext, url, img);
            name.setText(nm);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                cardView.setTooltipText(nm);
            }

        }


    }

}
