package com.example.anshme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class nameActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    static final String mypref="namePref";
    String LogTag = "AnshMeLog";
    EditText nameEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("Prefs",MODE_PRIVATE);
        String userName = sharedPreferences.getString(mypref,"");
        if(!userName.isEmpty())
        {
            Log.i(LogTag,"userName Present");
            Intent intent = new Intent(this, appMainActivity.class);
            intent.putExtra("YourName",userName);
            startActivity(intent);
            finish();
        }
        else
        {
            Log.i(LogTag,"userName not Present");
        }
        Button nextButton = findViewById(R.id.nextButton);
        nameEditText = findViewById(R.id.your_nameEditText);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAppMainActivity();
            }
        });
    }

    private void openAppMainActivity() {
        String userName  = nameEditText.getText().toString();
        Log.i(LogTag,userName);
        if(userName.isEmpty())
        {
            Toast.makeText(this,"Please Enter Name",Toast.LENGTH_LONG).show();
        }
        else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(mypref,userName);
            editor.apply();
            Intent intent = new Intent(this, appMainActivity.class);
            intent.putExtra("YourName",userName);
            startActivity(intent);
            finish();
        }
    }
}