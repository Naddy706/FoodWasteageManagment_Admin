package com.creativodevelopers.fwmadmin;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    //private ImageView register_image;
    private EditText register_email,names,phones,register_password;
    private Button register_button;
    private TextView already_have_an_account;
    private FirebaseAuth mAuth;
    private DatabaseReference ref;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();

        mAuth=FirebaseAuth.getInstance();

        already_have_an_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToLoginActivity();
            }
        });
    }

    private void SendUserToLoginActivity() {
        Intent loginintent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(loginintent);

    }

    private void initView() {
        //register_image = (ImageView) findViewById(R.id.register_image);
        names = (EditText) findViewById(R.id.name);
        phones = (EditText) findViewById(R.id.phone);
        register_email = (EditText) findViewById(R.id.register_email);
        register_password = (EditText) findViewById(R.id.register_password);
        register_button = (Button) findViewById(R.id.register_button);
        already_have_an_account = (TextView) findViewById(R.id.already_have_an_account);
        loadingBar = new ProgressDialog(this);
        ref= FirebaseDatabase.getInstance().getReference();
        register_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_button:
                submit();
                break;
        }
    }




    private void sendVerificationEmail()
    {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent


                            // after email is sent just logout the user and finish this activity


                            FirebaseAuth.getInstance().signOut();
                            Toast.makeText(RegisterActivity.this, "Verification Email has been sent to your email :"+user.getEmail(), Toast.LENGTH_LONG).show();
                            SendUserToLoginActivity();


                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do

                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });
    }


    private void submit() {
        // validate
        final String name = names.getText().toString().trim();
        final String phone = phones.getText().toString().trim();
        final String email = register_email.getText().toString().trim();
        String password = register_password.getText().toString().trim();


        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "name...", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "phone...", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email...", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password...", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(password.length() < 6){
            Toast.makeText(this, "Password must be greater then 6 Characters", Toast.LENGTH_SHORT).show();
        }
        else {


            loadingBar.setTitle("Creating new Account");
            loadingBar.setMessage("Please wait ... while we are creating a new account for you");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){

                                //Device to device push notification//
                                String currentUserId=mAuth.getCurrentUser().getUid();

                                String deviceToken= FirebaseInstanceId.getInstance().getToken();

                                HashMap<String,String> user= new HashMap<>();
                                user.put("name",name);
                                user.put("phone",phone);
                                user.put("email",email);
                                user.put("device_token",deviceToken);

                                ref.child("Admin").child(currentUserId).setValue(user);

                                ref.child("Admin").child(currentUserId).child("device_token").setValue(deviceToken);

                                Toast.makeText(RegisterActivity.this, "Account Created Succesfully", Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                                //sendVerificationEmail();
                                SendUserToMainActivity();

//Device to Device push Notification//


                                //before Notification
                     /*   String currentUserId=mAuth.getCurrentUser().getUid();
                        ref.child("Users").child(currentUserId).setValue("");
                        Toast.makeText(RegisterActivity.this, "Account Created Succesfully", Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                        //sendVerificationEmail();
                        SendUserToMainActivity();

                      */

                            }else {
                                String message=task.getException().toString();
                                Toast.makeText(RegisterActivity.this, "Error"+message, Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
        }


    }

    private void SendUserToMainActivity() {
        Intent mainIntent=new Intent(RegisterActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
