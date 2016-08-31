package com.example.shobhit_pc.emergency;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Shobhit-pc on 8/31/2016.
 */
public class EnterPersonalDetail extends AppCompatActivity implements View.OnClickListener {
    EditText mobile,name;
    Button submit;
    SharedPreferences sharedPreferences;
    String MyName = "Name";
    String MyNumber = "MobileNO";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enterpersonaldetail);
        mobile = (EditText) findViewById(R.id.etmobile);
        name = (EditText) findViewById(R.id.etname);
        submit = (Button) findViewById(R.id.btSubmit);
        submit.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.about){

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
       if( view.getId() == R.id.btSubmit){
          int mobileNoLength =  mobile.getText().toString().length();
           if(!name.getText().toString().isEmpty() && !mobile.getText().toString().isEmpty() && mobileNoLength > 7) {
               sharedPreferences = getSharedPreferences(MyName, Context.MODE_PRIVATE);
               SharedPreferences.Editor editor = sharedPreferences.edit();
               editor.putString("Name", name.getText().toString());
               editor.commit();
               sharedPreferences = getSharedPreferences(MyNumber, Context.MODE_PRIVATE);
               editor = sharedPreferences.edit();
               editor.putString("MobileNO", mobile.getText().toString());
               editor.commit();
               AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
               alertDialog.setTitle("Personal Details");
               alertDialog.setMessage("You have successfull enter your details");
               alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       startnewactivity();
                   }
               });
               alertDialog.create();
               alertDialog.show();
           }
           else if (name.getText().toString().isEmpty()){
               Toast.makeText(this,"Name should not be empty",Toast.LENGTH_SHORT).show();
           }
           else if (mobile.getText().toString().isEmpty()){
               Toast.makeText(this,"Mobile no. should not be empty",Toast.LENGTH_SHORT).show();
           }
           else  if (mobileNoLength <=7){
               Toast.makeText(this,"Mobile no is incorrect",Toast.LENGTH_SHORT).show();
           }
       }
    }
    public void startnewactivity(){
        Intent intent = new Intent(this, TypeOfEmergency.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
