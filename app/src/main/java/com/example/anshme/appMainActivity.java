package com.example.anshme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class appMainActivity extends AppCompatActivity {

    static final String mypref="namePref";
    WifiManager wifiManager;
    Button wifiOnButton ;
    ListView wifiAvaliableList;
    WifiP2pManager  wifiP2pManager;
    WifiP2pManager.Channel channel;
    BroadcastReceiver broadcastReceiver;
    IntentFilter intentFilter;

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
        TextView yourNameTextView = findViewById(R.id.yournameTextview);
        yourNameTextView.setText(getString(R.string.Welcome)+" "+ userName + " 😎");
        wifiOnButton = findViewById(R.id.wifiOnButton);
        wifiAvaliableList = findViewById(R.id.wifiDevices);

        wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = wifiP2pManager.initialize(getApplicationContext(),getMainLooper(),null);

        broadcastReceiver = new WiFiDirectBrodcastReceiver(wifiP2pManager,channel,this);
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

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