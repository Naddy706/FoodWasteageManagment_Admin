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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Button Phonelogin, LoginButton;
    //private ImageView login_image;
    private EditText login_email;
    private EditText login_password;
    private Button login_button;
    private TextView forget_password_link;
    private TextView need_new_account_link;
    private TextView login_using;
    private ProgressDialog loadingBar;
    private boolean isEmailVerified;

    private DatabaseReference userRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        userRef= FirebaseDatabase.getInstance().getReference().child("Admin");


//        Phonelogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SendUserToPhoneLoginActivity();
//            }
//        });

        need_new_account_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regINtent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(regINtent);
                finish();
                //SendUserToRegisterActivity();
                Toast.makeText(LoginActivity.this, "clicked", Toast.LENGTH_SHORT).show();
            }
        });






    }

    private void SendUserToMainActivity() {
        Intent mainIntent=new Intent(LoginActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }







//
//    private void SendUserToPhoneLoginActivity() {
//        Intent Phoneloginintent = new Intent(LoginActivity.this, PhoneLoginActivity.class);
//        startActivity(Phoneloginintent);
//
//    }

    private void SendUserToRegisterActivity() {
        Intent regINtent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(regINtent);
        finish();

    }

    private void initView() {
        //  login_image = (ImageView) findViewById(R.id.login_image);
        Phonelogin = (Button) findViewById(R.id.phone_login_button);
        login_email = (EditText) findViewById(R.id.login_email);
        login_password = (EditText) findViewById(R.id.login_password);
        login_button = (Button) findViewById(R.id.login_button);
        forget_password_link = (TextView) findViewById(R.id.forget_password_link);
        need_new_account_link = (TextView) findViewById(R.id.need_new_account_link);
        login_using = (TextView) findViewById(R.id.login_using);
        loadingBar = new ProgressDialog(this);


        login_button.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                AllowUserToLoginIn();
                break;
        }
    }

    private void sendemail()
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
                            finish();

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

    private void AllowUserToLoginIn() {

        // validate
        final String email = login_email.getText().toString().trim();
        String password = login_password.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please Enter Email...", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "PLease Enter Password...", Toast.LENGTH_SHORT).show();
            return;
        }
        else {

            final FirebaseUser user = mAuth.getCurrentUser();

            loadingBar.setTitle("Signing in to your Account");
            loadingBar.setMessage("Please wait ...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //   if(user.isEmailVerified()){

                            if (task.isSuccessful()){

                                SendUserToMainActivity();
                                Toast.makeText(LoginActivity.this, "You login SuccessFully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                            }
                            else{

                                String message=task.getException().toString();
                                Toast.makeText(LoginActivity.this, "Email Password are in correct", Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                            }

                           /*}
                            else{
                              ///  sendemail();
                                Toast.makeText(LoginActivity.this, "Another Verification Email has been sent to your email : "+user.getEmail(), Toast.LENGTH_LONG).show();
                            }*/
                        }
                    });


        }


    }




}
