package com.creativodevelopers.fwmadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    private TabLayout myTabLayout;
    private ViewPager myViewPager;
//    private TabItem tab1,tab2,tab3;
    PagerAdapter pagerAdapter;
    Button btn;
    private Toolbar mToolbar;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();

        myViewPager=(ViewPager) findViewById(R.id.ViewPager);
        pagerAdapter = new TabsAccessorAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(pagerAdapter);

        myTabLayout=(TabLayout) findViewById(R.id.tablayout);
        myTabLayout.setupWithViewPager(myViewPager);

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Show Event");

    }


    private void SendUserToLoginActivity() {
        Intent LoginIntent = new Intent(MainActivity.this,LoginActivity.class);
        //   LoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(LoginIntent);
        finish();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser= mAuth.getCurrentUser();

        if(currentUser == null ){
            SendUserToLoginActivity();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

                if(item.getItemId() == R.id.AddEvent){
                    Intent i =new Intent(MainActivity.this,AddEventActivity.class);
                    startActivity(i);
                }
                else if(item.getItemId() == R.id.Setting){
                    Toast.makeText(this, "Map", Toast.LENGTH_SHORT).show();
                    Intent i =new Intent(MainActivity.this,MapActivity.class);
                    startActivity(i);
                }

                return true;

    }
}
