package com.example.anshme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class appMainActivity extends AppCompatActivity {

    static final String mypref = "namePref";
    WifiManager wifiManager;
    TextView statusTextview;
    Button wifiOnButton, discoverButton;
    ListView wifiAvaliableList;
    WifiP2pManager wifiP2pManager;
    WifiP2pManager.Channel channel;
    BroadcastReceiver broadcastReceiver;
    IntentFilter intentFilter;

    List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    String[] deviceNameArray;
    WifiP2pDevice[] deviceArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_main);

        initially();

        wifiOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activeWifi();
            }
        });

        discoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                    }

                    return;
                }
                wifiP2pManager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        statusTextview.setText(R.string.Discovery_started);
                    }

                    @Override
                    public void onFailure(int i) {
                        statusTextview.setText(R.string.Discovery_fail);
                    }
                });
            }
        });
    }

    private void activeWifi(){
        if(!wifiManager.isWifiEnabled()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                Toast.makeText(getApplicationContext(), "Turn On Wifi", Toast.LENGTH_LONG).show();
            } else {
                wifiManager.setWifiEnabled(true);
                Toast.makeText(getApplicationContext(), "Turning On Wifi", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initially()
    {

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        SharedPreferences sharedPreferences = getSharedPreferences("Prefs",MODE_PRIVATE);
        String userName  = sharedPreferences.getString(mypref,"");
        statusTextview = findViewById(R.id.statusTextview);
        statusTextview.setText(getString(R.string.Welcome)+" "+ userName + " 😎");
        wifiOnButton = findViewById(R.id.wifiOnButton);
        discoverButton = findViewById(R.id.discoverButton);
        wifiAvaliableList = findViewById(R.id.wifiDevices);

        wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = wifiP2pManager.initialize(getApplicationContext(),getMainLooper(),null);

        broadcastReceiver = new WiFiDirectBrodcastReceiver(wifiP2pManager,channel,this.peerListListener,this);
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
            Toast.makeText(getApplicationContext(),"Peer",Toast.LENGTH_LONG).show();
            if(!wifiP2pDeviceList.getDeviceList().equals(peers)){
                peers.clear();
                peers.addAll(wifiP2pDeviceList.getDeviceList());
                deviceNameArray = new String[wifiP2pDeviceList.getDeviceList().size()];
                deviceArray = new WifiP2pDevice[wifiP2pDeviceList.getDeviceList().size()];

                int index = 0;
                for(WifiP2pDevice device : wifiP2pDeviceList.getDeviceList())
                {
                    deviceNameArray[index] = device.deviceName;
                    deviceArray[index] = device;
                    index++;
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,deviceNameArray);
                wifiAvaliableList.setAdapter(arrayAdapter);
                if(peers.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"No Device Found",Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
}