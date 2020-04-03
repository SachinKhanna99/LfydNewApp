package com.letsdevelopit.lfydnewapp.ProfileSection.Personal;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.letsdevelopit.lfydnewapp.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Calendar;

public class PersonalEdit extends AppCompatActivity {

    String name = "", phone = "", dob = "", address = "", image = "";
    EditText oname, ophone, oadd;
    TextView oimg, odob;
    Button img, save;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mref = database.getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser userc = mAuth.getCurrentUser();

    String urlmain = "";
    DatePickerDialog.OnDateSetListener mDateSetListener, nDateSetListener;

    String uid;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_edit);

        oname = findViewById(R.id.oname);
        ophone = findViewById(R.id.ophone);
        odob = findViewById(R.id.odob);
        oadd = findViewById(R.id.oadd);

        oimg = findViewById(R.id.oimg);

        img = findViewById(R.id.img);
        save = findViewById(R.id.save);

        uid = userc.getUid();

        mref.child("Merchant").child(uid).child("personal").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String nm = String.valueOf(dataSnapshot.child("personal").child("name").getValue());
                String add = String.valueOf(dataSnapshot.child("personal").child("address").getValue());
                String phnum = String.valueOf(dataSnapshot.child("personal").child("phoneNo").getValue());
                String date = String.valueOf(dataSnapshot.child("personal").child("dob").getValue());

                if (nm.isEmpty() || nm == "" || nm.equals("null")){
                    oname.setText("");
                }else {
                    oname.setText(nm);
                }

                if (add.isEmpty() || add == "" || add.equals("null")){
                    //.setText("Welcome ");
                    oadd.setText("");
                }else {
                   oadd.setText(add);
                }

                if (date.isEmpty() || date == "" || date.equals("null")){
                    //.setText("Welcome ");
                    odob.setText("");
                }else {
                    odob.setText(date);
                }

                if (phnum.isEmpty() || phnum == "" || phnum.equals("null")){
                    ophone.setText("");
                }else {
                    ophone.setText(phnum);
                }
//                oname.setText(dataSnapshot.child("name").getValue().toString());
//                oadd.setText(dataSnapshot.child("address").getValue().toString());
//                odob.setText(dataSnapshot.child("dob").getValue().toString());
//                ophone.setText(dataSnapshot.child("phoneNo").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                savedata();
            }
        });

        odob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(PersonalEdit.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = month + "/" + day + "/" + year;
                odob.setText(date);
            }
        };


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CropImage.activity(filePath)
                        .setAspectRatio(1, 1)
                        .start(PersonalEdit.this);
            }
        });


    }

    private void SelectImage() {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            filePath = result.getUri();
            image = String.valueOf(filePath);
            image = image.substring(image.lastIndexOf("/")+1);

            oimg.setText(image);
            //profileImageView.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(PersonalEdit.this, PersonalInfoActivity.class));
            finish();
        }
    }

    private void savedata() {

        name = String.valueOf(oname.getText());
        if (name.isEmpty() || name == "") {
            oname.setError("Enter the Name");
            oname.requestFocus();
        } else {
            dob = String.valueOf(odob.getText());
            if (dob.isEmpty() || dob == "") {
                odob.setError("Select D.O.B");
                odob.requestFocus();
            } else {

                address = String.valueOf(oadd.getText());
                if (address.isEmpty() || address == "") {
                    oadd.setError("Enter the Address");
                    oadd.requestFocus();
                } else {
                    phone = String.valueOf(ophone.getText());
                    if (phone.isEmpty() || phone == "") {
                        ophone.setError("Enter the Phone No.");
                        ophone.requestFocus();
                    } else {
                        if (image.isEmpty() || image == "") {

                            mref.child("Merchant").child(uid).child("personal").child("name").setValue(name);
                            mref.child("Merchant").child(uid).child("personal").child("address").setValue(address);
                            mref.child("Merchant").child(uid).child("personal").child("dob").setValue(dob);
                            mref.child("Merchant").child(uid).child("personal").child("phoneNo").setValue(phone);
//                            mref.child("Merchant").child(uid).child("personal").child("image").setValue("");

                            Intent intent = new Intent(PersonalEdit.this, PersonalInfoActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            mref.child("Merchant").child(uid).child("personal").child("name").setValue(name);
                            mref.child("Merchant").child(uid).child("personal").child("address").setValue(address);
                            mref.child("Merchant").child(uid).child("personal").child("dob").setValue(dob);
                            mref.child("Merchant").child(uid).child("personal").child("phoneNo").setValue(phone);
                            mref.child("Merchant").child(uid).child("personal").child("image").setValue("");

                            uploadImage();
                        }

                    }
                }
            }
        }
    }

    private void uploadImage() {
        if (filePath != null) {

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
                            "images/"
                                    + uid + ".jpg");

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath).continueWithTask(new Continuation() {
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
                                urlmain = downloadUrl.toString();

                                mref.child("Merchant").child(uid).child("personal").child("image").setValue(urlmain);

                                progressDialog.dismiss();


                                startActivity(new Intent(PersonalEdit.this, PersonalInfoActivity.class));
                                Toast.makeText(PersonalEdit.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(PersonalEdit.this, "Error Please check your internet connection ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }

    @Override
    public void onBackPressed() {

        Intent intentList = new Intent(getApplicationContext(), PersonalInfoActivity.class);
        intentList.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intentList);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onBackPressed();

    }
}
