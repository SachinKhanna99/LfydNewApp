package com.letsdevelopit.lfydnewapp.ProfileSection.BusinessInfo;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.letsdevelopit.lfydnewapp.MainActivity;
import com.letsdevelopit.lfydnewapp.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BusinessEdit extends AppCompatActivity  implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    String sname = "", phone = "", dis = "", image1 = "", image2 = "", image3 = "", imagemain = "", stype = "", ci = "", lon = "", lat = "", add = "";

    EditText s_name, s_phone, s_dis;
    EditText upi, account;
    EditText address;

    protected LocationManager locationManager;
    private GoogleApiClient mGoogleApiClient;


    TextView oimg1, oimg2, oimg3, oimg4;

    Button img1, img2, img3, imgmain, save;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mref = database.getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser userc = mAuth.getCurrentUser();

    Spinner type;
    String urlmain1 = "", urlmain2 = "", urlmain3 = "", urlmain4 = "";

    String uid, upiid, accnum;

    private Uri filePath1;
    private Uri filePath2;
    private Uri filePath3;
    private Uri filePath4;

    private final int PICK_IMAGE_REQUEST = 22;

    FirebaseStorage storage;
    StorageReference storageReference;
    ArrayList<String> languages = new ArrayList<>();

    private long no_type;
    private String check;
    private int k = 0, datayes = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_edit);

        s_name = findViewById(R.id.shop_name);
        s_phone = findViewById(R.id.shop_num);
        s_dis = findViewById(R.id.shop_discount);
        upi = findViewById(R.id.bank_upi2);
        account = findViewById(R.id.bank_acc_num2);
        address = findViewById(R.id.address);


        oimg1 = findViewById(R.id.oimg1);
        oimg2 = findViewById(R.id.oimg2);
        oimg3 = findViewById(R.id.oimg3);
        oimg4 = findViewById(R.id.oimg4);

        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        imgmain = findViewById(R.id.img4);

        save = findViewById(R.id.save);

        uid = userc.getUid();
        type = findViewById(R.id.types);

        languages.add("Select Any One");

        type.setVisibility(View.VISIBLE);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);

        mref.child("DashBoardBusiness").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                no_type = dataSnapshot.getChildrenCount();
                // Log.d("msg", String.valueOf(no_type));

                for (int i = 1; i <= no_type; i++) {
                    if (i != 12) {
                        String citystring = String.valueOf(dataSnapshot.child(String.valueOf(i)).child("name").getValue());
                        languages.add(citystring);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//
//        mref.child("Merchent").child(uid).child("business").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                s_name.setText(dataSnapshot.child("sname").getValue().toString());
//                s_phone.setText(dataSnapshot.child("sphone").getValue().toString());
//                s_dis.setText(dataSnapshot.child("sdis").getValue().toString());
//                check = dataSnapshot.child("check").getValue().toString();
//
//                if (check.equals("no")) {
//                    type.setVisibility(View.VISIBLE);
//                } else {
//                    type.setVisibility(View.GONE);
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkdata();

                if (datayes == 0) {
                    s_name.setError("Enter Details");
                    s_phone.setError("Enter Details");
                    s_dis.setError("Enter Details");
                    upi.setError("Enter Details");
                    account.setError("Enter Details");
                    address.setError("Enter Details");
                } else {
                    location();
//                savedata();
                }
            }
        });

        imgmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                k = 4;

                CropImage.activity(filePath4)
                        .setAspectRatio(1, 1)
                        .start(BusinessEdit.this);
            }
        });

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                k = 1;

                CropImage.activity(filePath1)
                        .setAspectRatio(4, 2)
                        .start(BusinessEdit.this);
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                k = 2;
                CropImage.activity(filePath2)
                        .setAspectRatio(4, 2)
                        .start(BusinessEdit.this);
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                k = 3;
                CropImage.activity(filePath3)
                        .setAspectRatio(4, 2)
                        .start(BusinessEdit.this);
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (k == 1) {
                filePath1 = result.getUri();
                image1 = String.valueOf(filePath1);
                image1 = image1.substring(image1.lastIndexOf("/") + 1);

                oimg1.setText(image1);
                uploadImage1();
            } else if (k == 2) {
                filePath2 = result.getUri();
                image2 = String.valueOf(filePath2);
                image2 = image2.substring(image2.lastIndexOf("/") + 1);

                oimg2.setText(image2);
                uploadImage2();
            } else if (k == 3) {
                filePath3 = result.getUri();
                image3 = String.valueOf(filePath3);
                image3 = image3.substring(image3.lastIndexOf("/") + 1);

                oimg3.setText(image3);
                uploadImage3();
            } else if (k == 4) {
                filePath4 = result.getUri();
                imagemain = String.valueOf(filePath4);
                imagemain = imagemain.substring(image3.lastIndexOf("/") + 1);

                oimg4.setText(imagemain);
                uploadImage4();
            }
            //profileImageView.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(BusinessEdit.this, MainActivity.class));
            finish();
        }
    }

    private void checkdata() {
        sname = String.valueOf(s_name.getText());
        if (sname.isEmpty() || sname == "") {
            s_name.setError("Enter the Shop Name");
            s_name.requestFocus();
        } else {
            dis = String.valueOf(s_dis.getText());
            if (dis.isEmpty() || dis == "") {
                s_dis.setError("Enter Discount");
                s_dis.requestFocus();
            } else {
                phone = String.valueOf(s_phone.getText());
                if (phone.isEmpty() || phone == "") {
                    s_phone.setError("Enter the Address");
                    s_phone.requestFocus();
                } else {
                    stype = type.getSelectedItem().toString();
                    if (stype.equals("Select Any One")) {
                        Toast.makeText(this, "Please Select Your Business Type", Toast.LENGTH_SHORT).show();
                    } else {
                        upiid = String.valueOf(upi.getText());
                        if (upiid.isEmpty() || upiid == "") {
                            upi.setError("Enter the Upi Id");
                            upi.requestFocus();
                        } else {
                            accnum = String.valueOf(account.getText());
                            if (accnum.isEmpty() || accnum == "") {
                                account.setError("Enter Bank Account Number");
                                account.requestFocus();
                            } else {
                                add = String.valueOf(address.getText());
                                if (add.isEmpty() || add == "") {
                                    address.setError("Enter Address");
                                    address.requestFocus();
                                }else {

                                datayes = 1;
                            }
                        }
                    }
                }
            }
        }
        }
    }

    private void savedata() {

        mref.child("Merchant").child(uid).child("shop").child("sname").setValue(sname);
        mref.child("Merchant").child(uid).child("shop").child("sdis").setValue(dis);
        mref.child("Merchant").child(uid).child("shop").child("sphone").setValue(phone);
        mref.child("Merchant").child(uid).child("shop").child("stype").setValue(stype);
        mref.child("Merchant").child(uid).child("shop").child("supi").setValue(upiid);
        mref.child("Merchant").child(uid).child("shop").child("sbanknum").setValue(accnum);
        mref.child("Merchant").child(uid).child("shop").child("scity").setValue(ci);
        mref.child("Merchant").child(uid).child("shop").child("saddress").setValue(add);
        mref.child("Merchant").child(uid).child("shop").child("scash").setValue(0);

        mref.child("Merchant").child(uid).child("shop").child("lat").setValue(lat);
        mref.child("Merchant").child(uid).child("shop").child("lon").setValue(lon);

        mref.child("Category").child(stype).child(uid).child("sname").setValue(sname);
        mref.child("Category").child(stype).child(uid).child("check").setValue("no");

        Toast.makeText(this, "Shop Added Successfully", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(BusinessEdit.this, MainActivity.class));
        finish();
    }




    private void uploadImage1() {
        if (filePath1 != null) {

            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            progressDialog.setCancelable(false);

            // Defining the child of storageReference
            final StorageReference ref
                    = storageReference
                    .child(
                            "shopimages/"
                                    + uid + "/" + "img1" + ".jpg");

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath1).continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return ref.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUrl = task.getResult();
                                urlmain1 = downloadUrl.toString();

                                mref.child("Merchant").child(uid).child("shop").child("simage1").setValue(urlmain1);

                                progressDialog.dismiss();


                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(BusinessEdit.this, "Error Please check your internet connection ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }


    private void uploadImage2() {
        if (filePath2 != null) {

            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            progressDialog.setCancelable(false);

            // Defining the child of storageReference
            final StorageReference ref
                    = storageReference
                    .child(
                            "shopimages/"
                                    + uid + "/" + "img2" + ".jpg");

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath2).continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return ref.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUrl = task.getResult();
                                urlmain2 = downloadUrl.toString();

                                mref.child("Merchant").child(uid).child("shop").child("simage2").setValue(urlmain2);

                                progressDialog.dismiss();


                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(BusinessEdit.this, "Error Please check your internet connection ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }

    private void uploadImage3() {
        if (filePath3 != null) {

            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            progressDialog.setCancelable(false);

            // Defining the child of storageReference
            final StorageReference ref
                    = storageReference
                    .child(
                            "shopimages/"
                                    + uid + "/" + "img3" + ".jpg");

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath3).continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return ref.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUrl = task.getResult();
                                urlmain3 = downloadUrl.toString();

                                mref.child("Merchant").child(uid).child("shop").child("simage3").setValue(urlmain3);

                                progressDialog.dismiss();


                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(BusinessEdit.this, "Error Please check your internet connection ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }

    private void uploadImage4() {
        if (filePath4 != null) {

            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            progressDialog.setCancelable(false);

            // Defining the child of storageReference
            final StorageReference ref
                    = storageReference
                    .child(
                            "shopimages/"
                                    + uid + "/" + "img4" + ".jpg");

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath4).continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return ref.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUrl = task.getResult();
                                urlmain4 = downloadUrl.toString();

                                mref.child("Merchant").child(uid).child("shop").child("simagemain").setValue(urlmain4);

                                progressDialog.dismiss();


                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(BusinessEdit.this, "Error Please check your internet connection ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }



    private void location() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(BusinessEdit.this,
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
//                    Toast.makeText(BusinessEdit.this, "loc = "+ loc.getLongitude()+ "  \n " + loc.getLatitude(), Toast.LENGTH_SHORT).show();
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
                lat = String.valueOf(locc.getLatitude());
                lon = String.valueOf(locc.getLongitude());

            }
        }
        catch (Exception e) {

//            // Log.d("msg","error = " + e);
        }
        String ss = "Current City is: "
                + cityName;

//        // Log.d("la2" , String.valueOf(lan2));
//        // Log.d("lon2" , String.valueOf(lon2));
        ci = cityName;

//        Toast.makeText(BusinessEdit.this, ss, Toast.LENGTH_SHORT).show();

        if (ci != null) {
            if (ci.equals("") || ci.isEmpty() || ci.equals("null")) {
                Toast.makeText(this, "Connection Error. Try Again!", Toast.LENGTH_SHORT).show();
            } else {
                savedata();
            }
        }else {
            Toast.makeText(this, "Connection Error. Try Again!", Toast.LENGTH_SHORT).show();

        }
    }





    @Override
    public void onBackPressed() {

        Intent intentList = new Intent(getApplicationContext(), MainActivity.class);
        intentList.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intentList);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onBackPressed();

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
        if (ContextCompat.checkSelfPermission(BusinessEdit.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(BusinessEdit.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(BusinessEdit.this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(BusinessEdit.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);

                                buildGoogleApiClient();
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(BusinessEdit.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(BusinessEdit.this)
                .addConnectionCallbacks(BusinessEdit.this)
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

}
