package com.creativodevelopers.fwmadmin;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class ShowEventFragment extends Fragment {

    private View showEvent;
    private RecyclerView showEventList;

    private DatabaseReference eventRef;

    public ShowEventFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        showEvent=inflater.inflate(R.layout.fragment_show_event, container, false);
        eventRef= FirebaseDatabase.getInstance().getReference().child("Event");


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

                String eventId=getRef(position).getKey();

                eventRef.child(eventId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){

                            if(dataSnapshot.hasChild("image")){

                                String eventImage=dataSnapshot.child("image").getValue().toString();
                                String tit=dataSnapshot.child("title").getValue().toString();
                                String des=dataSnapshot.child("description").getValue().toString();
                                String da=dataSnapshot.child("date").getValue().toString();
                                String ti=dataSnapshot.child("time").getValue().toString();
                                String l=dataSnapshot.child("location").getValue().toString();

                                holder.Title.setText(tit);
                                holder.Description.setText(des);
                                holder.Location.setText(l);
                                holder.Date.setText(da);
                                holder.Time.setText(ti);
                                Picasso.get().load(eventImage).placeholder(R.drawable.eventimage).into(holder.EventImage);

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

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView Title, Description,Date , Time , Location;

        ImageView EventImage;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            Title = itemView.findViewById(R.id.title);
            Description = itemView.findViewById(R.id.description);
            Date= itemView.findViewById(R.id.date);
            Time=itemView.findViewById(R.id.time);
            Location=itemView.findViewById(R.id.location);
            EventImage= itemView.findViewById(R.id.Eventimage);

        }
    }

}