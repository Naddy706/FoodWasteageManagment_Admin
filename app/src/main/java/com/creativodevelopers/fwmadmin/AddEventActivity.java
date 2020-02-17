package com.creativodevelopers.fwmadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddEventActivity extends AppCompatActivity {

    Button btnDatePicker, btnTimePicker, btnupload,btnsave,btnmap;
    EditText txtDate, txtTime,title,description,Address;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private int PICK_IMAGE=420;
    private StorageTask uploadTask;
    private Uri fileUri;
    String myUrl="";
    private Toolbar mToolbar;
    String data="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        btnDatePicker=findViewById(R.id.btn_date);
        btnTimePicker=findViewById(R.id.btn_time);
        txtDate=findViewById(R.id.in_date);
        txtTime=findViewById(R.id.in_time);
        Address=findViewById(R.id.address);
        title= findViewById(R.id.title);
        description=findViewById(R.id.description);
        btnupload=findViewById(R.id.uploadPic);
        btnsave=findViewById(R.id.save);
        mAuth=FirebaseAuth.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference();
        btnmap=findViewById(R.id.btn_map);
        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add Event");

         data = getIntent().getStringExtra("location");
         Address.setText(data);


        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                date();

            }
        });


        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time();
            }
        });

        btnmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddEventActivity.this,MapActivity.class);
                startActivity(intent);
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

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String t=title.getText().toString().trim();
                String d=description.getText().toString().trim();
                String dt=txtDate.getText().toString().trim();
                String tt=txtTime.getText().toString().trim();
                String lo=Address.getText().toString().trim();




                if (TextUtils.isEmpty(t)) {
                    Toast.makeText(AddEventActivity.this, "Title can not be nulll...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(d)) {
                    Toast.makeText(AddEventActivity.this, "description can not be nulll...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(dt)) {
                    Toast.makeText(AddEventActivity.this, "date can not be nulll...", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(tt)) {
                    Toast.makeText(AddEventActivity.this, "Time can not be nulll...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(lo)) {
                    Toast.makeText(AddEventActivity.this, "Location can not be nulll...", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {

                Map map = new HashMap();
                map.put("image", myUrl);
                map.put("title", t);
                map.put("description", d);
                map.put("date", dt);
                map.put("time", tt);
                map.put("location", data);


                databaseReference.child("Event").push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AddEventActivity.this, "Data Saved", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(AddEventActivity.this, "failed to save", Toast.LENGTH_SHORT).show();
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

                final StorageReference filepath = storageReference.child(Imagename + "." + "jpg");

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
