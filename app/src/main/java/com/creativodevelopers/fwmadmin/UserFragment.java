package com.creativodevelopers.fwmadmin;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    public static List<String> Name,Email,Phone;
    public static myUserAdapter ad;
    private DatabaseReference myUser;
    public static ListView list;

    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_user, container, false);


        list=root.findViewById(R.id.list);
        Name =  new ArrayList<String>();
        Email =  new ArrayList<String>();
        Phone =  new ArrayList<String>();
        myUser= FirebaseDatabase.getInstance().getReference();
        ad=new myUserAdapter(getActivity(),Name);
        load_clients(root);





        return root;

    }

    public void load_clients(View root){

        myUser.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    if (dsp.exists()) {

                        String name=dsp.child("name").getValue().toString();
                        String email=dsp.child("email").getValue().toString();
                        String phone=dsp.child("phone").getValue().toString();

                        Name.add(name);
                        Email.add(email);
                        Phone.add(phone);
                        ad.notifyDataSetChanged();

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    list.setAdapter(ad);
    }

}
