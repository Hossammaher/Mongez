package com.example.hossam1.mongez;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcordeiro.library.ShakeBack;


public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        ShakeBack.initialize(getActivity(), "hossammaher34@gmail.com", "App feedback")
                .setVibrationEnabled(true);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        ShakeBack.activate();
    }

    @Override
    public void onPause() {
        super.onPause();
        ShakeBack.deactivate();
    }


}
