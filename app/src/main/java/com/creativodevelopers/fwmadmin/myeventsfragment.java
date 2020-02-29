package com.creativodevelopers.fwmadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class myeventsfragment extends Fragment {


    public static List<String> Title,Description,Location,Time,Date,event_Image,countt,Foodname,key;
    public static List<Integer> vote;
    public static  Integer highnestvotes=0;

    public static myevent_adapter ad;
    ProgressDialog dialog;
    public static Integer maxIdx=0;
    private DatabaseReference myeventlist,voteref;
    public static ListView list;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_my_report, container, false);
        dialog=new ProgressDialog(getActivity());
        dialog.setCancelable(false);
        dialog.setMessage("Loading...");
       // dialog.show();
        list=root.findViewById(R.id.list);
        Title =  new ArrayList<String>();
        Description =  new ArrayList<String>();
        Location =  new ArrayList<String>();
        Time =  new ArrayList<String>();
        countt =  new ArrayList<String>();
        vote= new ArrayList<Integer>();
        Foodname= new ArrayList<String>();
        Date =  new ArrayList<String>();
        event_Image =  new ArrayList<String>();
        key =  new ArrayList<String>();

        myeventlist= FirebaseDatabase.getInstance().getReference();
        voteref= FirebaseDatabase.getInstance().getReference();
        ad=new myevent_adapter(getActivity(),Title);
        load_clients(root);

        return root;
    }

    public void load_clients(View root){


        myeventlist.child("interestedusers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    dialog.dismiss();
                    if (dsp.exists()) {


                            final String event = dsp.child("event_key").getValue().toString();
                            myeventlist.child("Event").child(event).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.exists()) {

                                        String eventImage = dataSnapshot.child("image").getValue().toString();
                                        String description = dataSnapshot.child("description").getValue().toString();
                                        String title = dataSnapshot.child("title").getValue().toString();
                                        String loc = dataSnapshot.child("location").getValue().toString();
                                        String date = dataSnapshot.child("date").getValue().toString();
                                        String time = dataSnapshot.child("time").getValue().toString();
                                        countt.add(dataSnapshot.getKey().toString());
                                        Title.add(title);
                                        Description.add(description);
                                        Location.add(loc);
                                        Date.add(date);
                                        Time.add(time);
                                        event_Image.add(eventImage);
                                        checkVote(event);
                                        ad.notifyDataSetChanged();

                                    }


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });



                    }


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void checkVote(final String eventID){

        voteref.child("Food").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    if (dsp.exists()) {

                        if (dsp.child("eventid").getValue().toString().equals(eventID)) {
                            String foodName = dsp.child("foodname").getValue().toString();
                            String votes = dsp.child("Votes").getValue().toString();
                            Foodname.add(foodName);
                            vote.add(Integer.parseInt(votes));
                            key.add(dsp.getKey().toString());

                            ad.notifyDataSetChanged();
                            Toast.makeText(getActivity(), "" + foodName, Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Foodname.add("null");
                            vote.add(0);
                            key.add("null");
                            Toast.makeText(getActivity(), "null" , Toast.LENGTH_SHORT).show();
                        }


                    }
                }
                Integer maxVal = Collections.max(vote);
                highnestvotes = Collections.max(vote);
                maxIdx = vote.indexOf(maxVal);


              //  Toast.makeText(getActivity(), "Votes :" + highnestvotes, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        list.setAdapter(ad);
    }

}