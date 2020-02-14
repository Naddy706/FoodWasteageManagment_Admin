package com.creativodevelopers.fwmadmin;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ShowEventFragment extends Fragment {



    public ShowEventFragment() {
        // Required empty public constructor
    }

    public static ShowEventFragment newInstance(String param1, String param2) {
        ShowEventFragment fragment = new ShowEventFragment();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_event, container, false);
    }



}
