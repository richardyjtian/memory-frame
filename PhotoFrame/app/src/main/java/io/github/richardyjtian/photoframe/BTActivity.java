package io.github.richardyjtian.photoframe;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BTActivity extends AppCompatActivity {

    //pair, discover buttons
    Button btnPaired,btnDiscovered;

    //pair, discover lists
    ListView devicelist, devicelist1;

    //newly discovered devices
    public  ArrayAdapter<String> mDeviceListAdapter;

    public String str;
    public HashMap<String, String> btpair = new HashMap<String, String>();


    //Initialize variables for Bluetooth
    private BluetoothAdapter myBluetooth = null;


    private Set<BluetoothDevice> pairedDevices;

    public static String EXTRA_ADDRESS = "device_address";

    public static OutputStream mmOutStream = null;

    //Sets all of the button functionality and other objects
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt);

        // Ask for location permissions
        Permissions.getLocationPermission(this);

        //set up view for discovered devices
        btnDiscovered = (Button) findViewById(R.id.find);
        devicelist1 = (ListView) findViewById(R.id.btlist); //discover

        //initialize array adapters, for newly discovered devices
        mDeviceListAdapter = new ArrayAdapter<String>(this,
                R.layout.device);
        devicelist1.setAdapter(mDeviceListAdapter);
        devicelist1.setOnItemClickListener(myListClickListener);


        //if the device has bluetooth
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        IntentFilter filter=new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver,filter);

        IntentFilter filter2=new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver,filter2);


        if(myBluetooth == null)
        {
            //Display a message if the bluetooth is not available on the android device
            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();

            //close the app
            finish();
        }
        else if(!myBluetooth.isEnabled())
        {
            //Ask to the user turn on the bluetooth on the device
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon,1);
        }

        btnDiscovered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //myDiscoveredArrayAdapter.clear();
                myBluetooth.startDiscovery();
                //Toast.makeText(getApplicationContext(), "start discovery...",Toast.LENGTH_LONG).show();
                discoveredDevices();
            }
        });
    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                String frameNum = device.getName();
                if(frameNum != null && frameNum.indexOf("Memory Frame") != -1) {
                    mDeviceListAdapter.add(device.getName() + "\n" + device.getAddress());
                    mDeviceListAdapter.notifyDataSetChanged();
                    btpair.put(frameNum, device.getAddress());
                }
            }
        }
    };

    @Override
    public void onDestroy(){
        super.onDestroy();

        this.unregisterReceiver(mReceiver);
    }


    public void discoveredDevices() {
        if(myBluetooth.isDiscovering()){
            myBluetooth.cancelDiscovery();
        }
        myBluetooth.startDiscovery();
    }


    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick (AdapterView<?> av, View v, int arg2, long arg3)
        {
            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            String name  = null;

            // Make an intent to start next activity.
            Intent intent = new Intent(BTActivity.this, FrameActivity.class);

            //Change the activity.
            for (Map.Entry<String, String> entry : btpair.entrySet()) {
                if (entry.getValue().equals(address)) {
                    name = entry.getKey();
                }
            }

            intent.putExtra("name", name);
            intent.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
            startActivity(intent);
        }
    };




}
