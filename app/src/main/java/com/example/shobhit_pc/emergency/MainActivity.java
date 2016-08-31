package com.example.shobhit_pc.emergency;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {


    SharedPreferences sharedPreferences;
    String MyNumber = "MobileNO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(MyNumber, MODE_PRIVATE);
        String MobileNO = (sharedPreferences.getString("MobileNO", ""));
        if(MobileNO.isEmpty()){
            Intent intent = new Intent(this,EnterPersonalDetail.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this,TypeOfEmergency.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
