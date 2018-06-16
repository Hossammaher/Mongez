package com.example.hossam1.mongez;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import hari.allagi.Allagi;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> menuList = new ArrayList<>();     //menu titles
        ArrayList<Integer> imagesList = new ArrayList<>();      //menu backgrounds
        ArrayList<Fragment> fragmentsList = new ArrayList<>();      //fragments for each menu headers in second activity

        menuList.add("Chat");       //add titles
        menuList.add("Bluetooth");
        menuList.add("Profile");
        menuList.add("About");

        imagesList.add(R.drawable.x);        //add background images
        imagesList.add(R.drawable.xblue);
        imagesList.add(R.drawable.xprofile);
        imagesList.add(R.drawable.xabout);


        fragmentsList.add(new ChatFragment());      //add fragment instances
        fragmentsList.add(new BluetoothFragment());
        fragmentsList.add(new ProfileFragment());
        fragmentsList.add(new AboutFragment());

        Allagi allagi = Allagi.initialize(MainActivity.this, menuList, imagesList, fragmentsList);
        allagi.setTransitionDuration(500);      //default value is 1000 milliseconds
        allagi.start();         //start the menu list activity

    }
}
