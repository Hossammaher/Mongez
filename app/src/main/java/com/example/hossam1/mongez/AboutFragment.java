package com.example.hossam1.mongez;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.github.clans.fab.FloatingActionButton;

import com.webianks.easy_feedback.EasyFeedback;


public class AboutFragment extends Fragment {
    FloatingActionButton feedback ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        feedback = view.findViewById(R.id.feedback);

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new EasyFeedback.Builder(getActivity())
                        .withEmail("hossammaher34@gmail.com")
                        .withSystemInfo()
                        .build()
                        .start();
            }
        });


        return view;
    }




}
