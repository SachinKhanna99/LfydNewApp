package com.letsdevelopit.lfydnewapp.ShopLists;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.letsdevelopit.lfydnewapp.MainActivity;
import com.letsdevelopit.lfydnewapp.ProfileSection.ProfileActivity;
import com.letsdevelopit.lfydnewapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShopListActivity extends AppCompatActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    List<ModelShopListClass> shoplist = new ArrayList<>();
    private AdapterShopList customAdapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mref = database.getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser userc = mAuth.getCurrentUser();

    ImageView not_found;
    String uid,type;
    private String currentcity = "";

    protected LocationManager locationManager;
    String mainCity;
       private GoogleApiClient mGoogleApiClient;
    private double lan2 = 0 , lon2 = 0,lan1 = 0 , lon1 = 0;
    private String book = "no";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);

        uid = userc.getUid();

        final Bundle bundle = getIntent().getExtras();
        type = bundle.getString("type");

        not_found = findViewById(R.id.not_found);


        location();


    }

    private void location() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(ShopListActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted

                buildGoogleApiClient();
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();

        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions

//                // Log.d("msg","helo");
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        assert locationManager != null;
//                Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locc =  locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


//                if (loc != null) {
//                    Toast.makeText(ShopListActivity.this, "loc = "+ loc.getLongitude()+ "  \n " + loc.getLatitude(), Toast.LENGTH_SHORT).show();
//                }


        String cityName = null;
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(locc.getLatitude(),
                    locc.getLongitude(), 1);
            lan2 = locc.getLatitude();
            lon2 = locc.getLongitude();
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();


            }
        }
        catch (Exception e) {

//            // Log.d("msg","error = " + e);
        }
        String ss = "Current City is: "
                + cityName;

//        // Log.d("la2" , String.valueOf(lan2));
//        // Log.d("lon2" , String.valueOf(lon2));
        mainCity = cityName;

//        Toast.makeText(ShopListActivity.this, ss, Toast.LENGTH_SHORT).show();

        updatedata();
    }

    private void updatedata() {
        final RecyclerView recyclerView = findViewById(R.id.recview);
        LinearLayoutManager staggeredGridLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(staggeredGridLayoutManager); // set LayoutManager to RecyclerView


        mref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                shoplist.clear();

                currentcity =  String.valueOf(dataSnapshot.child("Merchant").child(uid).child("basic").child("city").getValue());

                for (DataSnapshot ds : dataSnapshot.child("Category").child(type).getChildren()) {


                    String a = ds.getKey();
//                    // Log.d("msgg", a);

                    String scity = String.valueOf(dataSnapshot.child("Merchant").child(a).child("shop").child("scity").getValue());

                    if (currentcity.equals(scity)) {
                        String check = String.valueOf(dataSnapshot.child("Category").child(type).child(a).child("check").getValue());

                        if (check.equals("yes")) {
                            String sname = String.valueOf(dataSnapshot.child("Merchant").child(a).child("shop").child("sname").getValue());
                            String shcity = String.valueOf(dataSnapshot.child("Merchant").child(a).child("shop").child("scity").getValue());
                            String saddress = String.valueOf(dataSnapshot.child("Merchant").child(a).child("shop").child("saddress").getValue());
                            String scashback = String.valueOf(dataSnapshot.child("Merchant").child(a).child("shop").child("scash").getValue());
                            String sdis = String.valueOf(dataSnapshot.child("Merchant").child(a).child("shop").child("sdis").getValue());
                            String slan = String.valueOf(dataSnapshot.child("Merchant").child(a).child("shop").child("lat").getValue());
                            String slon = String.valueOf(dataSnapshot.child("Merchant").child(a).child("shop").child("lon").getValue());
                            String simg = String.valueOf(dataSnapshot.child("Merchant").child(a).child("shop").child("simagemain").getValue());

                            lan1 = Double.parseDouble(slan);
                            lon1 = Double.parseDouble(slon);

                            if (dataSnapshot.child("Bookmark").child(uid).hasChild(sname))
                            {
                                book = String.valueOf(dataSnapshot.child("Bookmark").child(uid).child(sname).child("book").getValue());
//                                // Log.d("msgg", book);
                            }

                            shoplist.add(new ModelShopListClass(simg, sname, saddress, sdis, scashback, lan1, lon1, lan2 , lon2 , a,type,book));
                            customAdapter = new AdapterShopList(ShopListActivity.this, shoplist);
                            recyclerView.setAdapter(customAdapter);
                            customAdapter.notifyDataSetChanged();
                        }
                    }


                }
                if (shoplist.isEmpty())
                {
                    not_found.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == MY_PERMISSIONS_REQUEST_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            //DO the download stuff

//            // Log.d("msg","helo");
            buildGoogleApiClient();
            location();
        }else {

           // loc.setEnabled(false);
        }
    }



    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(ShopListActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ShopListActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(ShopListActivity.this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(ShopListActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);

                                buildGoogleApiClient();
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(ShopListActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(ShopListActivity.this)
                .addConnectionCallbacks(ShopListActivity.this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    //
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
//                        loc.setEnabled(false);
//                        loc.setClickable(false);
                        dialog.cancel();

                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onLocationChanged(Location location) {


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

//        // Log.d("Latitude","status");

    }

    @Override
    public void onProviderEnabled(String s) {

//        // Log.d("Latitude","enable");

    }

    @Override
    public void onProviderDisabled(String s) {

//        // Log.d("Latitude","disab;e");

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
