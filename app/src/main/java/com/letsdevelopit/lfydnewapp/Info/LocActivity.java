package com.letsdevelopit.lfydnewapp.Info;

import android.Manifest;
import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.letsdevelopit.lfydnewapp.MainActivity;
import com.letsdevelopit.lfydnewapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class LocActivity extends AppCompatActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private String type;


    ArrayList<String> listState = new ArrayList<String>();
    ArrayList<String> listCity = new ArrayList<String>();
    AutoCompleteTextView actcity, actstate;
    private String mstate;

    protected LocationManager locationManager;

    Button loc;
    AutoCompleteTextView city;

    String mainCity;

    ProgressBar progress;

    private GoogleApiClient mGoogleApiClient;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mref = database.getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser userc = mAuth.getCurrentUser();
    private String UID;

    FloatingActionButton locsub;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc);


        final Bundle bundle = getIntent().getExtras();
        UID = bundle.getString("uid");

        progress = findViewById(R.id.progress);

        loc = findViewById(R.id.loc);

        city=findViewById(R.id.actCity);

        locsub = findViewById(R.id.submitloc);

        final String phone = userc.getPhoneNumber();

        locsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mainCity.isEmpty() || mainCity == "" || mainCity.equals(null) || mainCity.equals("null") )
                {
                    Toast.makeText(LocActivity.this, "Please Choose Your City", Toast.LENGTH_SHORT).show();
                }
                else {

                    mref.child("Merchant").child(UID).child("basic").child("city").setValue(mainCity);
                    mref.child("Merchant").child(UID).child("basic").child("phoneNo").setValue(phone.substring(3, 13));

                    mref.child("Merchant").child(UID).child("wallet").child("money").setValue(0);
                    mref.child("Merchant").child(UID).child("wallet").child("coin").setValue(0);

//                    mref.child("Merchant").child(UID).child("personal").child("name").setValue("");
//                    mref.child("Merchant").child(UID).child("personal").child("address").setValue("");
//                    mref.child("Merchant").child(UID).child("personal").child("dob").setValue("");
//                    mref.child("Merchant").child(UID).child("personal").child("phoneNo").setValue(phone.substring(3, 13));
//                    mref.child("Merchant").child(UID).child("personal").child("image").setValue("");
//
//                    mref.child("Merchant").child(UID).child("business").child("sname").setValue("");
//                    mref.child("Merchant").child(UID).child("business").child("sphone").setValue(phone.substring(3, 13));
//                    mref.child("Merchant").child(UID).child("business").child("simage1").setValue("");
//                    mref.child("Merchant").child(UID).child("business").child("simage2").setValue("");
//                    mref.child("Merchant").child(UID).child("business").child("simage3").setValue("");
//                    mref.child("Merchant").child(UID).child("business").child("sdis").setValue("");
//                    mref.child("Merchant").child(UID).child("business").child("stype").setValue("");
//                    mref.child("Merchant").child(UID).child("business").child("check").setValue("no");
//                    mref.child("Merchant").child(UID).child("business").child("scash").setValue("0");
//
//                    mref.child("Merchant").child(UID).child("bank").child("supi").setValue("");
//                    mref.child("Merchant").child(UID).child("bank").child("sbanknum").setValue("");
//
//                    mref.child("Merchant").child(UID).child("location").child("saddress").setValue("");
//                    mref.child("Merchant").child(UID).child("location").child("scity").setValue("");
//                    mref.child("Merchant").child(UID).child("location").child("lat").setValue("");
//                    mref.child("Merchant").child(UID).child("location").child("lon").setValue("");
//                    mref.child("Merchant").child(UID).child("location").child("check").setValue("no");


                    Intent intent = new Intent(LocActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(LocActivity.this,
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        assert locationManager != null;
        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }


        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress.setVisibility(View.VISIBLE);

                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions

                    // Log.d("msg","helo");
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                assert locationManager != null;
//                Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                Location locc =  LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                Location locc =  locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


//                if (loc != null) {
//                    Toast.makeText(LocActivity.this, "loc = "+ loc.getLongitude()+ "  \n " + loc.getLatitude(), Toast.LENGTH_SHORT).show();
//                }


                String cityName = null;
                Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                List<Address> addresses;
                try {
                    addresses = gcd.getFromLocation(locc.getLatitude(),
                            locc.getLongitude(), 1);
                    if (addresses.size() > 0) {
                        System.out.println(addresses.get(0).getLocality());
                        cityName = addresses.get(0).getLocality();
                    }
                }
                catch (Exception e) {

                    // Log.d("msg","error = " + e);
                }
                if (cityName != null)
                {
                    if (cityName.equals("") || cityName.equals("null") || cityName.isEmpty()){

                        Toast.makeText(LocActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();

                    }else{
                        String ss = "Current City is: "
                                + cityName;

                        mainCity = cityName;

                        city.setText(cityName);
                        Toast.makeText(LocActivity.this, ss, Toast.LENGTH_SHORT).show();

                    }
                }else {
                    Toast.makeText(LocActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();

                }

                progress.setVisibility(View.GONE);



//                                       progress.setVisibility(View.VISIBLE);
            }
        });

        callAll();


    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == MY_PERMISSIONS_REQUEST_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            //DO the download stuff

            // Log.d("msg","helo");
            buildGoogleApiClient();
        }else {

            loc.setEnabled(false);
        }
    }



    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(LocActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(LocActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(LocActivity.this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(LocActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);

                                buildGoogleApiClient();
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(LocActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }


        protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(LocActivity.this)
                .addConnectionCallbacks(LocActivity.this)
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
                        loc.setEnabled(false);
                        loc.setClickable(false);
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

        // Log.d("Latitude","status");

    }

    @Override
    public void onProviderEnabled(String s) {

        // Log.d("Latitude","enable");

    }

    @Override
    public void onProviderDisabled(String s) {

        // Log.d("Latitude","disab;e");

    }



    public void callAll()
    {
        obj_list();
        addState();
    }

    // Get the content of cities.json from assets directory and store it as string
    public String getJson()
    {
        String json=null;
        try
        {
            // Opening cities.json file
            InputStream is = getAssets().open("cities.json");
            // is there any content in the file
            int size = is.available();
            byte[] buffer = new byte[size];
            // read values in the byte array
            is.read(buffer);
            // close the stream --- very important
            is.close();
            // convert byte to string
            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return json;
        }
        return json;
    }

    // This add all JSON object's data to the respective lists
    void obj_list()
    {
        // Exceptions are returned by JSONObject when the object cannot be created
        try
        {
            // Convert the string returned to a JSON object
            JSONObject jsonObject=new JSONObject(getJson());
            // Get Json array
            JSONArray array=jsonObject.getJSONArray("array");
            // Navigate through an array item one by one
            for(int i=0;i<array.length();i++)
            {
                // select the particular JSON data
                JSONObject object=array.getJSONObject(i);
                String city=object.getString("name");
                String state=object.getString("state");
                // add to the lists in the specified format
                listState.add(state);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    void addCity()
    {

        try
        {
            // Convert the string returned to a JSON object
            JSONObject jsonObject=new JSONObject(getJson());
            // Get Json array
            JSONArray array=jsonObject.getJSONArray("array");
            // Navigate through an array item one by one
            for(int i=0;i<array.length();i++)
            {
                // select the particular JSON data
                JSONObject object=array.getJSONObject(i);
                String city=object.getString("name");
                String state=object.getString("state");
                // add to the lists in the specified format

                if (state.equals(mstate))
                {
                    listCity.add(city);
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }


        actcity=(AutoCompleteTextView)findViewById(R.id.actCity);
        adapterSettingcity(listCity);
    }

    // The third auto complete text view
    void addState()
    {
        Set<String> set = new HashSet<String>(listState);
        actstate=(AutoCompleteTextView)findViewById(R.id.actState);
        adapterSettingstate(new ArrayList(set));

    }

    // setting adapter for auto complete text views
    void adapterSettingcity(ArrayList arrayList)
    {


        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,arrayList);
        actcity.setAdapter(adapter);
        hideKeyBoardcity();
    }

    // hide keyboard on selecting a suggestion
    public void hideKeyBoardcity()
    {
        actcity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {

                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                mainCity = String.valueOf(actcity.getText());
            }
        });
    }



    void adapterSettingstate(ArrayList arrayList)
    {
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,arrayList);
        actstate.setAdapter(adapter);
        hideKeyBoardstate();
    }

    // hide keyboard on selecting a suggestion
    public void hideKeyBoardstate()
    {
        actstate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                // Log.d("msg", String.valueOf(actstate.getText()));

                mstate = String.valueOf(actstate.getText());

                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                listCity.clear();
                addCity();
                actcity.setEnabled(true);
            }
        });
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

        Intent intentList = new Intent(getApplicationContext(), LocActivity.class);
        intentList.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intentList);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onBackPressed();

    }
}
