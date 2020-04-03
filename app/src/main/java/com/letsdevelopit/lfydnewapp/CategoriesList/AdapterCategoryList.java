package com.letsdevelopit.lfydnewapp.CategoriesList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.letsdevelopit.lfydnewapp.R;
import com.letsdevelopit.lfydnewapp.ShopLists.ShopListActivity;
import com.letsdevelopit.lfydnewapp.Utils;


import java.util.ArrayList;
import java.util.List;

public class AdapterCategoryList extends RecyclerView.Adapter<AdapterCategoryList.Viewholder> {

    Context mcontext;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Activity activity;

    FirebaseUser user = mAuth.getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mref = database.getReference();




    private List<ModelCategoryClass> ModelCategoryClass;
    private List<ModelCategoryClass> exampleListFull;





    public AdapterCategoryList(Context context, List<ModelCategoryClass> ModelCategoryClass) {
        this.mcontext = context;
        this.ModelCategoryClass = ModelCategoryClass;
        exampleListFull = new ArrayList<>(ModelCategoryClass);


    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dashrow, viewGroup, false);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder viewholder, final int i) {

        final String url = ModelCategoryClass.get(i).getURL();
        final String name = ModelCategoryClass.get(i).getName();


//        // Log.d("msgg",url);
//        // Log.d("msgg",name);
        viewholder.set_data(name,url);


//        viewholder.img.getDrawable();

        viewholder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mcontext, ShopListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("type",name);
                mcontext.startActivity(intent);

            }
        });

        if (name.equals("Restaurants & Cafes")){
            viewholder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mcontext, ShopListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("type","RestaurantsandEateries");
                    mcontext.startActivity(intent);

                }
            });
        }
        if (name.equals("Desktop & Laptop shop")){
            viewholder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mcontext, ShopListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("type","ComputerProducts");
                    mcontext.startActivity(intent);

                }
            });
        }


    }


    @Override
    public int getItemCount() {
        return ModelCategoryClass.size();
    }

    public void a(){
        int al;
    }

    public Filter getFilter() {
        return exampleFilter;
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

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ModelCategoryClass> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(ModelCategoryClass);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ModelCategoryClass item : exampleListFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ModelCategoryClass.clear();
            ModelCategoryClass.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
