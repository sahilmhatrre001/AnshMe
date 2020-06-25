package com.example.anshme;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.widget.Toast;

public class WiFiDirectBrodcastReceiver extends BroadcastReceiver {
   private WifiP2pManager wManager;
   private WifiP2pManager.Channel wChannel;
   private Activity wActivity;

    public WiFiDirectBrodcastReceiver(WifiP2pManager wManager, WifiP2pManager.Channel wChannel, Activity wActivity) {
        this.wManager = wManager;
        this.wChannel = wChannel;
        this.wActivity = wActivity;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action))
        {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1);

            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED){
                Toast.makeText(context,"Wifi is On",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(context,"Wifi is Off",Toast.LENGTH_LONG).show();
            }

        }else if(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action))
        {

        }else if(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action))
        {

        }else if(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action))
        {

        }
    }
}
