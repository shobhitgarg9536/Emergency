package in.silive.emergency;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
    EditText mobile,name,dob,age,address,bloodgroup,inheriteddiseases,diseases;
    Button submit;
    SharedPreferences sharedPreferences;
    String MyProfile = "Profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enterpersonaldetail);
        mobile = (EditText) findViewById(R.id.etmobile);
        name = (EditText) findViewById(R.id.etname);
        dob = (EditText) findViewById(R.id.etdob);
        address = (EditText) findViewById(R.id.etaddress);
        age = (EditText) findViewById(R.id.etage);
        bloodgroup = (EditText) findViewById(R.id.etbloodgroup);
        inheriteddiseases = (EditText) findViewById(R.id.etinheriteddiseases);
        diseases = (EditText) findViewById(R.id.etdiseases);
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
           if(!name.getText().toString().isEmpty() && !mobile.getText().toString().isEmpty() && mobileNoLength > 7
                   && !dob.getText().toString().isEmpty() && !age.getText().toString().isEmpty()
                   && !bloodgroup.getText().toString().isEmpty() && !inheriteddiseases.getText().toString().isEmpty()
                   && !diseases.getText().toString().isEmpty() && !address.getText().toString().isEmpty()) {
               sharedPreferences = getSharedPreferences(MyProfile, Context.MODE_PRIVATE);
               SharedPreferences.Editor editor = sharedPreferences.edit();
               editor.putString("Name", name.getText().toString());
               editor.putString("MobileNO", mobile.getText().toString());
               editor.putString("DOB", dob.getText().toString());
               editor.putString("Age", age.getText().toString());
               editor.putString("Address", address.getText().toString());
               editor.putString("BloodGroup", bloodgroup.getText().toString());
               editor.putString("InheritedDiseases", inheriteddiseases.getText().toString());
               editor.putString("Diseases", diseases.getText().toString());
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
               Toast.makeText(this,"Name is empty",Toast.LENGTH_SHORT).show();
           }
           else if (mobile.getText().toString().isEmpty()){
               Toast.makeText(this,"Mobile no. is empty",Toast.LENGTH_SHORT).show();
           }
           else  if (mobileNoLength <=7){
               Toast.makeText(this,"Mobile no is incorrect",Toast.LENGTH_SHORT).show();
           }
           else if (dob.getText().toString().isEmpty()){
               Toast.makeText(this,"DOB. is empty",Toast.LENGTH_SHORT).show();
           }
           else if (age.getText().toString().isEmpty()){
               Toast.makeText(this,"Age is empty",Toast.LENGTH_SHORT).show();
           }
           else if (bloodgroup.getText().toString().isEmpty()){
               Toast.makeText(this,"Blood Group is empty",Toast.LENGTH_SHORT).show();
           }
           else if (address.getText().toString().isEmpty()){
               Toast.makeText(this,"Address is empty",Toast.LENGTH_SHORT).show();
           }
           else if (inheriteddiseases.getText().toString().isEmpty()){
               Toast.makeText(this,"Inherited Diseases is empty",Toast.LENGTH_SHORT).show();
           }
           else if (diseases.getText().toString().isEmpty()){
               Toast.makeText(this,"Diseases is empty",Toast.LENGTH_SHORT).show();
           }

       }
    }
    public void startnewactivity(){
        /** MODIFIED THE INTENT TO LOAD SELECTCONTACT ACTIVITY**/
        Intent intent = new Intent(this, SelectContacts.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
