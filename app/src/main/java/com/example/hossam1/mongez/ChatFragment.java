package com.example.hossam1.mongez;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.github.library.bubbleview.BubbleTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import chat.ChatAppMsgAdapter;
import chat.ChatAppMsgDTO;


public class ChatFragment extends Fragment {

    private static final int RESULT_OK = 5;
    SpeechRecognizer mSpeechRecognizer;
    Intent mSpeechRecognizerIntent;
    EditText input;
    ImageButton click_to_speak;
    ImageButton save;
    RecyclerView chatmesg;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    final List<ChatAppMsgDTO> msgDtoList = new ArrayList<ChatAppMsgDTO>();
    final ChatAppMsgAdapter chatAppMsgAdapter = new ChatAppMsgAdapter(msgDtoList);
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View inflate = inflater.inflate(R.layout.activity_chat_fragment, parent, false);

        input = (EditText) inflate.findViewById(R.id.editText);
        save = inflate.findViewById(R.id.save);
        click_to_speak = inflate.findViewById(R.id.mic_icon);
        chatmesg = (RecyclerView) inflate.findViewById(R.id.chat_recycler_view);
        System.out.println("hhh on create view chat ");

        return inflate;
    }

    public void onStart() {

        super.onStart();
        if (blue_model.d == 1) {
            new ConnectBT().execute();
        }
    }

    String address = "";


    private void sendSignal(String number) {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write(number.toString().getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    String msgserver = "how are you ?";

    private void msg(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute() {
//            progress = ProgressDialog.show(getActivity(), "Connecting...", "Please Wait!!!");
        }

        @Override
        protected Void doInBackground(Void... devices) {
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    System.out.println("hhhhh " + blue_model.add);
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(blue_model.add);
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    myBluetooth.cancelDiscovery();
                    btSocket.connect();
                }
            } catch (IOException e) {
                System.out.println("hhh exp" + e.getMessage());
                ConnectSuccess = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                msg("Connection Failed.Try again.");
                // getActivity().finish();
            } else {
                msg("Connected");
                isBtConnected = true;
            }

//            progress.dismiss();
        }
    }

    void chat(String msgContent, String command) {
        sendSignal(command);
        ChatAppMsgDTO msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_SENT, msgContent);
        msgDtoList.add(msgDto);
        msgserver = command;
        int newMsgPosition = msgDtoList.size();

        // Notify recycler view insert one new data.
        chatAppMsgAdapter.notifyItemInserted(newMsgPosition);

        // Scroll RecyclerView to the last message.
        chatmesg.scrollToPosition(newMsgPosition);

        ChatAppMsgDTO xx = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_RECEIVED, msgserver);
        msgDtoList.add(xx);

        // Empty the input edit text box.
        input.setText("");
//                    bt.send(msgContent, true);
        System.out.println("hhh " + msgContent);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //recycler view setup
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        chatmesg.setLayoutManager(linearLayoutManager);

        //first message in left side

        if (chatAppMsgAdapter.getItemCount() == 0) {
            ChatAppMsgDTO msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_RECEIVED, "hello");
            msgDtoList.add(msgDto);

        }

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

                String v = input.getText().toString();
                if (!v.isEmpty()) {
                    click_to_speak.setVisibility(View.GONE);
                    save.setVisibility(View.VISIBLE);
                } else {
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


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msg("save ");
                final String msgContent = input.getText().toString();
                if (!TextUtils.isEmpty(msgContent)) {
                    // Add a new sent message to the list.
                    /**/
                   // String url = "https://g-project-2018.appspot.com";
                    String url = "http://192.168.2.116:5050/";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    msg(" re : " + response);
                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        msg(obj.getString("pred"));
                                        final String msgContent = input.getText().toString();
                                        chat(msgContent, obj.getString("pred"));

                                    } catch (JSONException e) {

                                        e.printStackTrace();
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    msg("error" + error.getMessage());
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();

                            final String msgContent = input.getText().toString();
                            params.put("word", msgContent);

                            return params;
                        }

                    };
                    VolleySingleton.getInstance(getActivity()).addRequestQue(stringRequest);
                    /**/


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

    @Override
    public void onResume() {
        super.onResume();
        final ChatAppMsgAdapter chatAppMsgAdapter = new ChatAppMsgAdapter(msgDtoList);
        // Set data adapter to RecyclerView.
        chatmesg.setAdapter(chatAppMsgAdapter);
    }

}