package com.letsdevelopit.lfydnewapp.ShopLists;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.letsdevelopit.lfydnewapp.R;
import com.letsdevelopit.lfydnewapp.ShopLists.ShopData.ShopDataActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AdapterShopList extends RecyclerView.Adapter<AdapterShopList.Viewholder> {

    Context mcontext;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Activity activity;

    FirebaseUser user = mAuth.getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mref = database.getReference();

    String uid;



    private List<ModelShopListClass> ModelShopListClass;
    private List<ModelShopListClass> exampleListFull;


    public AdapterShopList(Context context, List<ModelShopListClass> ModelShopListClass) {
        this.mcontext = context;
        this.ModelShopListClass = ModelShopListClass;
        exampleListFull = new ArrayList<>(ModelShopListClass);


    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shop_list, viewGroup, false);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder viewholder, final int i) {

        final String img = ModelShopListClass.get(i).getImagefav();
        final String sname = ModelShopListClass.get(i).getShopname();
        final String address = ModelShopListClass.get(i).getAddress();
        final String cashback = ModelShopListClass.get(i).getCashback();
        final String discount = ModelShopListClass.get(i).getDiscount();
        final double lat1 = ModelShopListClass.get(i).getLan1();
        final double lon1 = ModelShopListClass.get(i).getLon1();
        final double lat2 = ModelShopListClass.get(i).getLan2();
        final double lon2 = ModelShopListClass.get(i).getLon2();
        final String suid = ModelShopListClass.get(i).getSuid();
        final String type = ModelShopListClass.get(i).getType();
        final String book = ModelShopListClass.get(i).getBook();

        uid = user.getUid();


        if (book.equals("no")) {
            viewholder.bookmark.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
            viewholder.bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mref.child("Bookmark").child(uid).child(sname).child("book").setValue("yes");
                    mref.child("Bookmark").child(uid).child(sname).child("suid").setValue(suid);

                    viewholder.bookmark.setImageResource(R.drawable.ic_bookmark_black_24dp);


                }
            });
        }
        else if(book.equals("yes")){
            viewholder.bookmark.setImageResource(R.drawable.ic_bookmark_black_24dp);

            viewholder.bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewholder.bookmark.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                    mref.child("Bookmark").child(uid).child(sname).child("book").setValue("no");
                    mref.child("Bookmark").child(uid).child(sname).child("suid").setValue(suid);

                }
            });


        }




        viewholder.set_data(img,sname,address,discount,cashback,lat1,lon1,lat2,lon2);




        viewholder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                // Log.d("msgxx",String.valueOf(lat2) );
                Intent intent = new Intent(mcontext, ShopDataActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("sname",sname);
                intent.putExtra("address",address);
                intent.putExtra("cash",cashback);
                intent.putExtra("dis",discount);
                intent.putExtra("distance",viewholder.distance.getText().toString());
                intent.putExtra("suid",suid);
                intent.putExtra("type",type);
                intent.putExtra("lat",String.valueOf(lat2));
                intent.putExtra("lon",String.valueOf(lon2));
                intent.putExtra("book",book);

                mcontext.startActivity(intent);

            }
        });


    }


    @Override
    public int getItemCount() {
        return ModelShopListClass.size();
    }

    public void a(){
        int al;
    }




    class Viewholder extends RecyclerView.ViewHolder {

        private ImageView img,bookmark;
        private TextView sname , sadd , distance , scash, sdis;
        private CardView cardView;



        public Viewholder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.imageView);
            cardView = itemView.findViewById(R.id.shopcard);
            sadd = itemView.findViewById(R.id.address);
            scash = itemView.findViewById(R.id.cashbacktv);
            sdis = itemView.findViewById(R.id.discount);
            bookmark = itemView.findViewById(R.id.favourite);
            sname = itemView.findViewById(R.id.shopname);

            distance = itemView.findViewById(R.id.distancetv);

        }

        private void set_data(String imagefav, String shopname, String address, String discount, String cashback, double la , double lon, double la2, double lon2) {

            Glide.with(itemView)
                    .load(imagefav)
                    .into(img);

            sname.setText(shopname);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                cardView.setTooltipText(shopname);
            }
            sadd.setText(address);
            sdis.setText(discount);
            scash.setText(cashback+ "%");


//            double theta = lon - lon2;
//            double dist = Math.sin(deg2rad(la))
//                    * Math.sin(deg2rad(la2))
//                    + Math.cos(deg2rad(la))
//                    * Math.cos(deg2rad(la2))
//                    * Math.cos(deg2rad(theta));
//            dist = Math.acos(dist);
//            dist = rad2deg(dist);
//            dist = dist * 60 * 1.1515;

            Location mylocation = new Location("");
            Location dest_location = new Location("");

            // Log.d("la1" , String.valueOf(la));
            // Log.d("lon1" , String.valueOf(lon));
            // Log.d("la2" , String.valueOf(la2));
            // Log.d("lon2" , String.valueOf(lon2));
            mylocation.setLatitude(la2);
            mylocation.setLongitude(lon2);

            dest_location.setLatitude(la);
            dest_location.setLongitude(lon);

            float a = (mylocation.distanceTo(dest_location))/1000;

//            String dx = String.valueOf(a);
            distance.setText(new DecimalFormat("##.#").format(a) + " Km");


//            return (dist);

        }


    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

//    private Filter exampleFilter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            List<ModelShopListClass> filteredList = new ArrayList<>();
//
//            if (constraint == null || constraint.length() == 0) {
//                filteredList.addAll(ModelShopListClass);
//            } else {
//                String filterPattern = constraint.toString().toLowerCase().trim();
//
//                for (ModelShopListClass item : exampleListFull) {
//                    if (item.getName().toLowerCase().contains(filterPattern)) {
//                        filteredList.add(item);
//                    }
//                }
//            }
//
//            FilterResults results = new FilterResults();
//            results.values = filteredList;
//
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            ModelShopListClass.clear();
//            ModelShopListClass.addAll((List) results.values);
//            notifyDataSetChanged();
//        }
//    };
}
