package com.creativodevelopers.fwmadmin;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;


public class ShowEventFragment extends Fragment {

    private View showEvent;
    private RecyclerView showEventList;

    private DatabaseReference eventRef,foodref;

    public ShowEventFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        showEvent=inflater.inflate(R.layout.fragment_show_event, container, false);
        eventRef= FirebaseDatabase.getInstance().getReference().child("Event");
        foodref= FirebaseDatabase.getInstance().getReference().child("Food");

        showEventList =  showEvent.findViewById(R.id.event_list);
        showEventList.setLayoutManager(new LinearLayoutManager(getActivity()));


        return showEvent;
    }


    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Event>()
                .setQuery(eventRef, Event.class)
                .build();



        FirebaseRecyclerAdapter<Event,EventViewHolder> adapter= new FirebaseRecyclerAdapter<Event, EventViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final EventViewHolder holder, int position, @NonNull Event model) {

                final String eventId=getRef(position).getKey();

                eventRef.child(eventId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){

                            if(dataSnapshot.hasChild("image")){

                                final String eventImage=dataSnapshot.child("image").getValue().toString();
                                final String tit=dataSnapshot.child("title").getValue().toString();
                                final String des=dataSnapshot.child("description").getValue().toString();
                                final String da=dataSnapshot.child("date").getValue().toString();
                                final String ti=dataSnapshot.child("time").getValue().toString();
                                final String l=dataSnapshot.child("location").getValue().toString();

                                holder.Title.setText(tit);
                                holder.Description.setText(des);
                                holder.Location.setText(l);
                                holder.Date.setText(da);
                                holder.Time.setText(ti);
                                Picasso.get().load(eventImage).placeholder(R.drawable.eventimage).into(holder.EventImage);
                                holder.Update.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        UpdateEvent(eventId,eventImage,tit,des,da,ti,l);
                                    }
                                });

                                holder.Delete.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        DeleteEvent(eventId);
                                    }
                                });
                                holder.Food.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AddFoodDailog(eventId);
                                    }
                                });


//                                foodref.addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                        for(DataSnapshot data:dataSnapshot.getChildren()){
//
//                                            String key=data.child("eventid").getValue().toString();
//
//
//                                            if(key == eventId){
//                                            }
//                                            else {
//
//                                            }
//
//                                        }
//
//
//
//                                    }
//
//                                    @Override
//                                    public void onCancelled(DatabaseError databaseError) {
//
//                                    }
//                                });


                            }
                            else {

                                String tit=dataSnapshot.child("title").getValue().toString();
                                String des=dataSnapshot.child("description").getValue().toString();
                                String da=dataSnapshot.child("date").getValue().toString();
                                String ti=dataSnapshot.child("time").getValue().toString();
                                String l=dataSnapshot.child("location").getValue().toString();

                                holder.Title.setText(tit);
                                holder.Description.setText(des);
                                holder.Location.setText(l);
                                holder.Date.setText(da);
                                holder.Delete.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        DeleteEvent(eventId);
                                    }
                                });

                                holder.Food.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AddFoodDailog(eventId);
                                    }
                                });
//                                foodref.addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                        for(DataSnapshot data:dataSnapshot.getChildren()){
//
//                                            String key=data.child("eventid").getValue().toString();
//
//
//                                            if(key == eventId){
//
//                                            }
//                                            else {
//
//
//                                            }
//
//                                        }
//
//
//
//                                    }
//
//                                    @Override
//                                    public void onCancelled(DatabaseError databaseError) {
//
//                                    }
//                                });


                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }

            @NonNull
            @Override
            public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.show_events_list,viewGroup,false);
                EventViewHolder viewHolder= new EventViewHolder(view);

                return viewHolder;

            }
        };

        showEventList.setAdapter(adapter);
        adapter.startListening();
    }

    private void AddFoodDailog(final String eventId) {

        Context context = getActivity();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Food Type Like Veg,NonVeg,Continental and etc...");

        final EditText FoodName=new EditText(getActivity());
        FoodName.setHint("e.g Food Type");
        layout.addView(FoodName);
        alertDialogBuilder.setView(layout);


        alertDialogBuilder.setPositiveButton(" Add ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String Foodname=FoodName.getText().toString();
                if(TextUtils.isEmpty(Foodname)){
                    Toast.makeText(getActivity(), "Please Write Food Type", Toast.LENGTH_SHORT).show();
                }
                else {
                    AddFood(eventId,Foodname);
                }
            }
        });
        alertDialogBuilder.setNegativeButton(" Cancel ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();


    }

    private void AddFood(String eventId, String foodName ) {




        HashMap<String,String> map=new HashMap<>();
        map.put("eventid",eventId);
        map.put("foodname",foodName);
        map.put("Votes","0");
        foodref.push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    Toast.makeText(getActivity(), "Food Preference Added", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), "Failed Adding food Preference", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //
//        HashMap<String,String> map=new HashMap<>();
//        map.put("eventid",eventId);
//        map.put("foodname1",foodName1);
//        map.put("foodname2",foodName2);
//        map.put("foodname3",foodName3);
//        map.put("foodname4",foodName4);
//        map.put("foodname5",foodName5);
//
//        foodref.push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//
//                if(task.isSuccessful())
//                {
//                    Toast.makeText(getActivity(), "Food Preference Added", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Toast.makeText(getActivity(), "Failed Adding food Preference", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

    }

    private void DeleteEvent(String eventId) {

        eventRef.child(eventId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Toast.makeText(getActivity(), "Event Deleted", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void UpdateEvent(String eventId, String eventImage, String tit, String des, String da, String ti, String l) {
        Intent updateEvent= new Intent(getActivity(),UpdateEventActivity.class);
        updateEvent.putExtra("id",eventId);
        updateEvent.putExtra("eventImage",eventImage);
        updateEvent.putExtra("title",tit);
        updateEvent.putExtra("description",des);
        updateEvent.putExtra("date",da);
        updateEvent.putExtra("time",ti);
        updateEvent.putExtra("location",l);
        startActivity(updateEvent);
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView Title, Description,Date , Time , Location;
        Button Update,Delete,Food;
        ImageView EventImage;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            Title = itemView.findViewById(R.id.title);
            Description = itemView.findViewById(R.id.description);
            Date= itemView.findViewById(R.id.date);
            Time=itemView.findViewById(R.id.time);
            Location=itemView.findViewById(R.id.location);
            EventImage= itemView.findViewById(R.id.Eventimage);
            Update=itemView.findViewById(R.id.update);
            Delete=itemView.findViewById(R.id.delete);
            Food=itemView.findViewById(R.id.foodSuggestions);
        }
    }

}