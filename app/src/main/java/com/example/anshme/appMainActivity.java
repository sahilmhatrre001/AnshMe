package com.example.anshme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class appMainActivity extends AppCompatActivity {

    static final String mypref="namePref";
    WifiManager wifiManager;
    Button sendBtn,receiveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_main);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        SharedPreferences sharedPreferences = getSharedPreferences("Prefs",MODE_PRIVATE);
        String userName  = sharedPreferences.getString(mypref,"");
        TextView yourNameTextView = findViewById(R.id.yournameTextview);
        yourNameTextView.setText(getString(R.string.Welcome)+" "+ userName + " 😎");
        sendBtn = findViewById(R.id.sendButton);
        receiveBtn = findViewById(R.id.receiveButton);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activeTethering();
            }
        });

        receiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!wifiManager.isWifiEnabled()){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                        val panelIntent = Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
//                        startActivityForResult(panelIntent, 0)
                    } else {
                        wifiManager.setWifiEnabled(true);
                        Toast.makeText(getApplicationContext(),"Turning On Wifi",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }

    private void activeTethering(){
        Toast.makeText(getApplicationContext(),"Turn On Hotspot",Toast.LENGTH_LONG).show();
        Intent tetherSettings = new Intent();
        tetherSettings.setClassName("com.android.settings", "com.android.settings.TetherSettings");
        startActivity(tetherSettings);
    }

}