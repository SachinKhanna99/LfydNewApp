package com.letsdevelopit.lfydnewapp.CategoriesList;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.letsdevelopit.lfydnewapp.DashContent;
import com.letsdevelopit.lfydnewapp.MainActivity;
import com.letsdevelopit.lfydnewapp.R;

import java.util.ArrayList;
import java.util.List;

public class CategoriesList extends AppCompatActivity {

    List<ModelCategoryClass> personImages = new ArrayList<>();
    private AdapterCategoryList customAdapter;

    int k = 0;
    SearchView searchView;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mref = database.getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser userc = mAuth.getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_list);

        final RecyclerView recyclerView = findViewById(R.id.businessty);
        GridLayoutManager staggeredGridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(staggeredGridLayoutManager); // set LayoutManager to RecyclerView

        searchView = findViewById(R.id.searchView);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customAdapter.getFilter().filter(newText);
                return false;
            }
        });

        mref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                personImages.clear();

                for (DataSnapshot ds : dataSnapshot.child("DashBoardBusiness").getChildren()) {


                    String a = ds.getKey();
//                    // Log.d("msgg", a);

                        String url = String.valueOf(dataSnapshot.child("DashBoardBusiness").child(a).child("img").getValue());
                        String name = String.valueOf(dataSnapshot.child("DashBoardBusiness").child(a).child("name").getValue());

                        k = 0;
                        if (name.equals("Load More"))
                        {
//                            // Log.d("msggg", "hey");
                            k = 1;
                        }

//                        // Log.d("msggg", url);
//                        // Log.d("msggg", name);
                        if ( k == 0) {
                            personImages.add(new ModelCategoryClass(name, url));
                            customAdapter = new AdapterCategoryList(CategoriesList.this, personImages);
                            recyclerView.setAdapter(customAdapter);
                            customAdapter.notifyDataSetChanged();
                        }
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intentList = new Intent(getApplicationContext(), MainActivity.class);
        intentList.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intentList);
        overridePendingTransition(0,0);
        super.onBackPressed();

    }
}
