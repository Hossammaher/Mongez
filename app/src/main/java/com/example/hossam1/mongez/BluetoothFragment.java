package com.example.hossam1.mongez;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class BluetoothFragment extends Fragment implements AdapterView.OnItemClickListener{
    Button discover;
    Switch on_off;
    BluetoothAdapter bluetoothAdapter ;
    private static final String TAG = "MainActivity";
    ArrayList<BluetoothDevice> mDevice=new ArrayList<>();
    DeviceListAdapter deviceListAdapter;
    ListView listView;
    TextView On_off_text ,before_turnOn_text ,bluetooth_name ;
    LinearLayout after_trunOn_text;

    //bluetooth connection
    BluetoothConnectionService mBluetoothConnection;
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View inflate = inflater.inflate(R.layout.activity_bluetooth, parent, false);

        on_off=inflate.findViewById(R.id.bluetooth);
        listView=inflate.findViewById(R.id.lvNewDevices);
        On_off_text=inflate.findViewById(R.id.On_OFF_txt);
        discover=inflate.findViewById(R.id.bluetooth_discover);
        before_turnOn_text=inflate.findViewById(R.id.before_turnOn_text);
        after_trunOn_text=inflate.findViewById(R.id.Turning_on_layout);
        bluetooth_name=inflate.findViewById(R.id.bluetooth_name);


        listView.setOnItemClickListener(this);
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        IntentFilter filter =new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        getActivity().registerReceiver(mBroadcastReceiver4,filter);


        if (bluetoothAdapter.isEnabled()){
            before_turnOn_text.setVisibility(View.GONE);
            after_trunOn_text.setVisibility(View.VISIBLE);
            bluetooth_name.setText(bluetoothAdapter.getName());
            on_off.setChecked(true);
            On_off_text.setText("On");
        }
        else {
            before_turnOn_text.setVisibility(View.VISIBLE);
            after_trunOn_text.setVisibility(View.GONE);
            on_off.setChecked(false);
            On_off_text.setText("Off");

        }

        on_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

             enable_disable();
            }
        });


        discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                discover();
            }
        });

        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Set<BluetoothDevice> pairedDevices= bluetoothAdapter.getBondedDevices();

        if(pairedDevices.size()>0){

            for(BluetoothDevice device : pairedDevices){

                String Dname=device.getName();
                Toast.makeText(getActivity(), Dname, Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void enable_disable() {

        if (bluetoothAdapter==null){
            Log.d(TAG, "enableDisableBT: Does not have BT capabilities.");

        }

        if (!bluetoothAdapter.isEnabled()){

            Intent enabled =new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enabled);
            IntentFilter filter =new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            getActivity().registerReceiver(mBroadcastReceiver1, filter);
            checkBTPermissions();


            before_turnOn_text.setVisibility(View.GONE);
            after_trunOn_text.setVisibility(View.VISIBLE);

            bluetooth_name.setText(bluetoothAdapter.getName());

            on_off.setChecked(true);
            On_off_text.setText("ON");
            enable_disable_discover();
            discover();
        }

        if (bluetoothAdapter.isEnabled()){

            IntentFilter filter =new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            getActivity().registerReceiver(mBroadcastReceiver1, filter);

            bluetoothAdapter.disable();
            before_turnOn_text.setVisibility(View.VISIBLE);
            after_trunOn_text.setVisibility(View.GONE);

            on_off.setChecked(false);
            On_off_text.setText("OFF");

        }

    }


//    @Override
//    public void onDestroy() {
//        Log.d(TAG, "onDestroy: called.");
//        super.onDestroy();
//        getActivity().unregisterReceiver(mBroadcastReceiver1);
//        getActivity().unregisterReceiver(mBroadcastReceiver2);
//        getActivity().unregisterReceiver(mBroadcastReceiver3);
//        getActivity().unregisterReceiver(mBroadcastReceiver4);
//    }

    public void enable_disable_discover() {

        Toast.makeText(getActivity(), "enable discover", Toast.LENGTH_SHORT).show();

        Intent intent =new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);

        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
        startActivity(intent);

        IntentFilter filter= new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        getActivity().registerReceiver(mBroadcastReceiver2,filter);

    }

    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                switch(state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    /**
     * Broadcast Receiver for changes made to bluetooth states such as:
     * 1) Discoverability mode on/off or expire.
     */
    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {

                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode) {
                    //Device is in Discoverable Mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Enabled.");
                        break;
                    //Device not in discoverable mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Able to receive connections.");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Not able to receive connections.");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "mBroadcastReceiver2: Connecting....");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "mBroadcastReceiver2: Connected.");
                        break;
                }

            }
        }
    };

    /**
     * Broadcast Receiver for listing devices that are not yet paired
     * -Executed by btnDiscover() method.
     */
    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND.");

            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);
                mDevice.add(device);
                Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
                deviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mDevice);
                listView.setAdapter(deviceListAdapter);
            }
        }
    };

    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){

                if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                    BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    //3 cases:
                    //case1: bonded already
                    if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED){
                        Toast.makeText(context, "BOND_BONDED", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "BroadcastReceiver: BOND_BONDED.");
                    }
                    //case2: creating a bone
                    if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                        Log.d(TAG, "BroadcastReceiver: BOND_BONDING.");
                    }
                    //case3: breaking a bond
                    if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                        Log.d(TAG, "BroadcastReceiver: BOND_NONE.");
                        Toast.makeText(context, "none", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        }
    };


    public void discover() {

        if (bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();

            checkBTPermissions();
            bluetoothAdapter.startDiscovery();
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            getActivity().registerReceiver(mBroadcastReceiver3,filter);
        }
        if (!bluetoothAdapter.isDiscovering()){
            checkBTPermissions();
            bluetoothAdapter.startDiscovery();
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            getActivity().registerReceiver(mBroadcastReceiver3,filter);
        }
    }

    private void checkBTPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        } else {

        }

    }

//
//    private void checkBTPermissions() {
//        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
//            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
//            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
//            if (permissionCheck != 0) {
//
//                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
//            }
//        }else{
//            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
//        }
//    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        bluetoothAdapter.cancelDiscovery();

        Log.d(TAG, "onItemClick: You Clicked on a device.");
        String deviceName = mDevice.get(i).getName();
        String deviceAddress = mDevice.get(i).getAddress();

        Log.d(TAG, "onItemClick: deviceName = " + deviceName);
        Log.d(TAG, "onItemClick: deviceAddress = " + deviceAddress);

        //create the bond.
        //NOTE: Requires API 17+? I think this is JellyBean
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
            Toast.makeText(getActivity(), "Trying to pair with " + deviceName, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Trying to pair with " + deviceName);
            mDevice.get(i).createBond();
        }

    }


    public void connect(String respond){
        byte[] bytes = respond.getBytes(Charset.defaultCharset());
        mBluetoothConnection.write(bytes);

    }


}
