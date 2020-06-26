package com.example.anshme;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class WiFiDirectBrodcastReceiver extends BroadcastReceiver {
    private WifiP2pManager wManager;
    private WifiP2pManager.Channel wChannel;
    private Activity wActivity;
    private WifiP2pManager.PeerListListener p;

    public WiFiDirectBrodcastReceiver(WifiP2pManager wManager, WifiP2pManager.Channel wChannel, WifiP2pManager.PeerListListener p,Activity wActivity) {
        this.wManager = wManager;
        this.wChannel = wChannel;
        this.p = p;
        this.wActivity = wActivity;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);

            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {

            } else {

            }

        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            if (wManager != null) {
                if (ActivityCompat.checkSelfPermission(wActivity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                }
                Log.d("AnshMe","Here");
                wManager.requestPeers(wChannel, p);

                }
            }
        else if(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action))
        {

        }else if(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action))
        {

        }
    }
}
