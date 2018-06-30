package com.example.hossam1.mongez;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.github.clans.fab.FloatingActionButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.webianks.easy_feedback.EasyFeedback;


public class AboutFragment extends Fragment {
    FloatingActionButton feedback ;
    EditText email, feedback_text;
    Button btn, send, cancel;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        feedback = view.findViewById(R.id.feedback);

        final String info =
                "MODEL: " + Build.MODEL + " ID: " + Build.ID
                        + " SDK: " + Build.VERSION.SDK_INT;

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
                View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
                alertDialogBuilderUserInput.setView(mView);

//                final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);

                email = mView.findViewById(R.id.email);
                feedback_text = mView.findViewById(R.id.feedback_text);
                send = mView.findViewById(R.id.send_feed);
                cancel = mView.findViewById(R.id.cancel);
                alertDialogBuilderUserInput
                        .setCancelable(true);


                final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();

                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (email.getText().toString().isEmpty()) {
                            email.setError("Email required");
                            email.requestFocus();
                        }
                        if (feedback_text.getText().toString().isEmpty()) {
                            feedback_text.setError("Enter your feedback");
                        }

                        if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                            email.setError("Enter vaild Email");
                            email.requestFocus();
                            return;
                        }


                        feedback_model feed = new feedback_model(email.getText().toString(), feedback_text.getText().toString(), info);
                        myRef.child("feed").push().setValue(feed);
                        Toast.makeText(getActivity(), "Thanks", Toast.LENGTH_SHORT).show();
                        alertDialogAndroid.cancel();

                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialogAndroid.cancel();

                    }
                });
            }

        });


        return view;
    }




}
