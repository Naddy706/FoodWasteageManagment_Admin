package com.creativodevelopers.fwmadmin;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class user_upcoming_adapter extends ArrayAdapter {

    private final Activity context;

    private final List<String> subtitle;


    public user_upcoming_adapter(Activity context, List<String> subtitle) {
        super(context, R.layout.show_events_list, subtitle);
        // TODO Auto-generated constructor stub

        this.context=context;

        this.subtitle=subtitle;


    }
    TextView Title, Description,Date , Time , Location, user,vote,Foodname;
    Button Interested;
    ImageView EventImage;

    public View getView(final int position, final View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View itemView=inflater.inflate(R.layout.event_report, null,true);

        Title = itemView.findViewById(R.id.title);
        Description = itemView.findViewById(R.id.description);
        Date= itemView.findViewById(R.id.date);
        Time=itemView.findViewById(R.id.time);
        Location=itemView.findViewById(R.id.location);
        EventImage= itemView.findViewById(R.id.Eventimage);
        user= itemView.findViewById(R.id.users);
        vote=itemView.findViewById(R.id.vote);
        Foodname=itemView.findViewById(R.id.Foodnames);

        int count=0;
        if(myeventsfragment.countt.size()>0) {
            for (int i = 0; i < myeventsfragment.countt.size(); i++) {
                if (myeventsfragment.countt.get(i).equals(myeventsfragment.countt.get(position))) {
                    count++;
                }
            }
        }



        Title.setText(myeventsfragment.Title.get(position));
        Description.setText(myeventsfragment.Description.get(position));
        Date.setText(myeventsfragment.Date.get(position));
        Time.setText(myeventsfragment.Time.get(position));
        Location.setText(myeventsfragment.Location.get(position));
        user.setText(user_upcoming_fragment.username+"");
        vote.setText(user_upcoming_fragment.email+"");
        if(myeventsfragment.Foodname.size()<1)
        {
            Foodname.setText("");
        }
        else {
            Foodname.setText(myeventsfragment.Foodname.get(myeventsfragment.maxIdx) + "");
        }
        Picasso.get().load(myeventsfragment.event_Image.get(position)).placeholder(R.drawable.eventimage).into(EventImage);


            return itemView;

    };
}