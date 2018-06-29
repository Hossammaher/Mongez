package com.example.hossam1.mongez;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import hari.allagi.Allagi;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onStart() {
        super.onStart();
        ArrayList<String> menuList = new ArrayList<>();     //menu titles
        ArrayList<Integer> imagesList = new ArrayList<>();      //menu backgrounds
        ArrayList<Fragment> fragmentsList = new ArrayList<>();      //fragments for each menu headers in second activity

        menuList.add("CHAT");       //add titles
        menuList.add("CONNECTION");
        menuList.add("HOW TO USE");
        menuList.add("ABOUT");

        imagesList.add(R.drawable.chat_ui);
        imagesList.add(R.drawable.bluetooth_ui);
        imagesList.add(R.drawable.howtouse_ui);
        imagesList.add(R.drawable.about_ui);


        fragmentsList.add(new ChatFragment());      //add fragment instances
      fragmentsList.add(new BluetoothFragment());
        fragmentsList.add(new howtouse_fragment());
        fragmentsList.add(new AboutFragment());

        Allagi allagi = Allagi.initialize(MainActivity.this, menuList, imagesList, fragmentsList);

        allagi.setTransitionDuration(300);      //default value is 1000 milliseconds
        allagi.start();         //start the menu list activity

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ArrayList<String> menuList = new ArrayList<>();     //menu titles
        ArrayList<Integer> imagesList = new ArrayList<>();      //menu backgrounds
        ArrayList<Fragment> fragmentsList = new ArrayList<>();      //fragments for each menu headers in second activity

        menuList.add("Chat");       //add titles
        menuList.add("Bluetooth");
        menuList.add("How To use");
        menuList.add("About");

        imagesList.add(R.drawable.chat_ui);
        imagesList.add(R.drawable.bluetooth_ui);
        imagesList.add(R.drawable.howtouse_ui);
        imagesList.add(R.drawable.about_ui);


        fragmentsList.add(new ChatFragment());      //add fragment instances
//        fragmentsList.add(new BluetoothFragment());
        fragmentsList.add(new howtouse_fragment());
        fragmentsList.add(new AboutFragment());

        Allagi allagi = Allagi.initialize(MainActivity.this, menuList, imagesList, fragmentsList);
        allagi.setTransitionDuration(500);      //default value is 1000 milliseconds
        allagi.start();         //start the menu list activity

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> menuList = new ArrayList<>();     //menu titles
        ArrayList<Integer> imagesList = new ArrayList<>();      //menu backgrounds
        ArrayList<Fragment> fragmentsList = new ArrayList<>();      //fragments for each menu headers in second activity

        menuList.add("Chat");       //add titles
        menuList.add("Bluetooth");
        menuList.add("How To use");
        menuList.add("About");

        imagesList.add(R.drawable.chat_ui);
        imagesList.add(R.drawable.bluetooth_ui);
        imagesList.add(R.drawable.howtouse_ui);
        imagesList.add(R.drawable.about_ui);


        fragmentsList.add(new ChatFragment());      //add fragment instances
        fragmentsList.add(new BluetoothFragment());
        fragmentsList.add(new howtouse_fragment());
        fragmentsList.add(new AboutFragment());

        Allagi allagi = Allagi.initialize(MainActivity.this, menuList, imagesList, fragmentsList);
        allagi.setTransitionDuration(500);      //default value is 1000 milliseconds
        allagi.start();         //start the menu list activity

    }

}
