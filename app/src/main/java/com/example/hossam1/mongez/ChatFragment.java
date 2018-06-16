package com.example.hossam1.mongez;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import at.markushi.ui.CircleButton;
import chat.ChatAppMsgAdapter;
import chat.ChatAppMsgDTO;

public class ChatFragment extends Fragment {

    SpeechRecognizer mSpeechRecognizer ;
    Intent mSpeechRecognizerIntent;
    EditText input;
    CircleButton click_to_speak ;
    CircleButton save ;
    TextView show;
    ArrayList<String> Sent;
    String Label, txt;
    RecyclerView chatmesg;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View inflate = inflater.inflate(R.layout.activity_chat_fragment, parent, false);

        input = (EditText) inflate.findViewById(R.id.editText);
        save=inflate.findViewById(R.id.save);
        click_to_speak = (CircleButton) inflate.findViewById(R.id.mic_icon);
        chatmesg=inflate.findViewById(R.id.chat_recycler_view);


        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //recycler view setup
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        chatmesg.setLayoutManager(linearLayoutManager);

        //first message in left side
        final List<ChatAppMsgDTO> msgDtoList = new ArrayList<ChatAppMsgDTO>();
        ChatAppMsgDTO msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_RECEIVED, "hello");
        msgDtoList.add(msgDto);

        final ChatAppMsgAdapter chatAppMsgAdapter = new ChatAppMsgAdapter(msgDtoList);
        // Set data adapter to RecyclerView.
        chatmesg.setAdapter(chatAppMsgAdapter);



        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(getActivity());

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String v =input.getText().toString();
                if (!v.isEmpty()) {
                    click_to_speak.setVisibility(View.GONE);
                    save.setVisibility(View.VISIBLE);
                }
                else {
                    click_to_speak.setVisibility(View.VISIBLE);
                    save.setVisibility(View.GONE);
                }

            }
        });



        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-EG");
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

        click_to_speak.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_UP:
                        input.setHint("input show here");
                        mSpeechRecognizer.stopListening();

                        break;

                    case MotionEvent.ACTION_DOWN:
                        checkPermission();
                        input.setText("");
                        input.setHint("listening ");
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);

                        break;
                }
                return false;
            }
        });


        final String msgserver ="how are you ?";

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String msgContent = input.getText().toString();
                if (!TextUtils.isEmpty(msgContent)) {
                    // Add a new sent message to the list.
                    ChatAppMsgDTO msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_SENT, msgContent);
                    msgDtoList.add(msgDto);

                    int newMsgPosition = msgDtoList.size();

                    // Notify recycler view insert one new data.
                    chatAppMsgAdapter.notifyItemInserted(newMsgPosition);

                    // Scroll RecyclerView to the last message.
                    chatmesg.scrollToPosition(newMsgPosition);

                    ChatAppMsgDTO xx = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_RECEIVED, msgserver);
                    msgDtoList.add(xx);

                    // Empty the input edit text box.
                    input.setText("");

                }
            }

            });



        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
//                Toast.makeText(MainActivity.this, "Ready For Speech", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onBeginningOfSpeech() {
//                Toast.makeText(MainActivity.this, "Beginning Of Speech", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {
//                Toast.makeText(MainActivity.this, "Speech Received", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onEndOfSpeech() {
            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {

                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if (matches != null) {

                    input.setText(matches.get(0));

                }

            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });




    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 1);

        } else {

        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }



}
