package com.creativodevelopers.fwmadmin;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.PhoneAuthProvider;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;

public class myUserAdapter extends ArrayAdapter {

    private final Activity context;
    private final List<String> subtitle;


    public myUserAdapter(Activity context,List<String> subtitle) {
        super(context, R.layout.show_events_list, subtitle);
        // TODO Auto-generated constructor stub

        this.context=context;

        this.subtitle=subtitle;


    }


    TextView phone, email,name;
    ImageView Userimage;

    public View getView(final int position, final View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View itemView=inflater.inflate(R.layout.userlist, null,true);

        name = itemView.findViewById(R.id.UserName);
        email = itemView.findViewById(R.id.UserEmail);
        phone = itemView.findViewById(R.id.UserPhone);
        Userimage = itemView.findViewById(R.id.Userimg);

        name.setText(UserFragment.Name.get(position));
        email.setText(UserFragment.Email.get(position));
        phone.setText(UserFragment.Phone.get(position));

      //  Picasso.get().load(myUserAdapter.event_Image.get(position)).placeholder(R.drawable.eventimage).into(Userimage);



        return itemView;
    }


}

