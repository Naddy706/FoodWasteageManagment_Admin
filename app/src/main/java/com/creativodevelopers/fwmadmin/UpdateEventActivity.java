package com.creativodevelopers.fwmadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UpdateEventActivity extends AppCompatActivity {

    Button btnDatePicker, btnTimePicker, btnupload,btnupdate,btnAddress;
    EditText txtDate, txtTime,title,description,Address;
    ImageView image;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private int PICK_IMAGE=420;
    private StorageTask uploadTask;
    private Uri fileUri;
    String myUrl="";
    private Toolbar mToolbar;

    String id,eImage,tii,descep,datte,timme,locat;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_event);

         Intent intent = getIntent();
         id=intent.getStringExtra("id");
         eImage=intent.getStringExtra("eventImage");
         tii=intent.getStringExtra("title");
         descep=intent.getStringExtra("description");
         datte=intent.getStringExtra("date");
         timme=intent.getStringExtra("time");
         locat=intent.getStringExtra("location");


         image=findViewById(R.id.updateImage);
         btnDatePicker=findViewById(R.id.Update_btn_date);
         btnTimePicker=findViewById(R.id.Update_btn_time);
         btnupload=findViewById(R.id.UpdateuploadPic);
         btnAddress=findViewById(R.id.Update_Address);
         btnupdate=findViewById(R.id.update);

         txtDate=findViewById(R.id.Update_in_date);
         txtTime=findViewById(R.id.Update_in_time);
         title=findViewById(R.id.updateTitle);
         description=findViewById(R.id.UpdateDescription);
         Address=findViewById(R.id.address);

         mAuth=FirebaseAuth.getInstance();
         databaseReference= FirebaseDatabase.getInstance().getReference();

         mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
         setSupportActionBar(mToolbar);
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         getSupportActionBar().setDisplayShowHomeEnabled(true);
         getSupportActionBar().setTitle("Update Event");


         Picasso.get().load(eImage).placeholder(R.drawable.eventimage).into(image);
         title.setText(tii);
         description.setText(descep);
         txtDate.setText(datte);
         txtTime.setText(timme);
         Address.setText(locat);


         btnDatePicker.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 date();
             }
         });

         btnTimePicker.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 time();
             }
         });

         btnupload.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent= new Intent();
                 intent.setType("image/*");
                 intent.setAction(Intent.ACTION_GET_CONTENT);
                 startActivityForResult(intent, PICK_IMAGE);

             }
         });

         btnupdate.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String t=title.getText().toString().trim();
                 String d=description.getText().toString().trim();
                 String dt=txtDate.getText().toString().trim();
                 String tt=txtTime.getText().toString().trim();
                 String lo=Address.getText().toString().trim();


                 if (TextUtils.isEmpty(t)) {
                     Toast.makeText(UpdateEventActivity.this, "Title can not be nulll...", Toast.LENGTH_SHORT).show();
                     return;
                 }
                 if (TextUtils.isEmpty(d)) {
                     Toast.makeText(UpdateEventActivity.this, "description can not be nulll...", Toast.LENGTH_SHORT).show();
                     return;
                 }
                 if (TextUtils.isEmpty(dt)) {
                     Toast.makeText(UpdateEventActivity.this, "date can not be nulll...", Toast.LENGTH_SHORT).show();
                     return;
                 }

                 if (TextUtils.isEmpty(tt)) {
                     Toast.makeText(UpdateEventActivity.this, "Time can not be nulll...", Toast.LENGTH_SHORT).show();
                     return;
                 }
                 if (TextUtils.isEmpty(lo)) {
                     Toast.makeText(UpdateEventActivity.this, "Location can not be nulll...", Toast.LENGTH_SHORT).show();
                     return;
                 }
                 else {

                     Map map = new HashMap();
                     map.put("image", myUrl);
                     map.put("title", t);
                     map.put("description", d);
                     map.put("date", dt);
                     map.put("time", tt);
                     map.put("location", lo);


                     databaseReference.child("Event").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             if(task.isSuccessful()){
                                 Toast.makeText(UpdateEventActivity.this, "Data updated", Toast.LENGTH_SHORT).show();
                             }
                             else {
                                 Toast.makeText(UpdateEventActivity.this, "failed to save", Toast.LENGTH_SHORT).show();
                             }
                         }
                     });

                 }
             }
         });
     }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {

        if(resultCode == RESULT_OK ){

            fileUri = data.getData();


            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Image Files");


            DatabaseReference userMessageKeyRef = databaseReference.child("Event").push();

           final String Imagename = userMessageKeyRef.getKey();

            final StorageReference filepath = storageReference.child(userMessageKeyRef + "." + "jpg");

            uploadTask = filepath.putFile(fileUri);
            Toast.makeText(this, ""+fileUri.toString(), Toast.LENGTH_SHORT).show();
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filepath.getDownloadUrl();
                }

            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();

                    }
                }
            });


        }
    }

    public void date(){

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    public  void time(){

        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        txtTime.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }
}
