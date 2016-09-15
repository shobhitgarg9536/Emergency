package in.silive.emergency;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Shobhit-pc on 9/13/2016.
 */
public class Profile extends AppCompatActivity {

    TextView name,mobile,disease,inherited,dob,address,blood;
    Toolbar toolbar;
    SharedPreferences sharedPreferences;
    String MyProfile = "Profile";
    String Sname,Smobile,Saddress,Sblood,Sdob,Sinherited,Sdiseases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        toolbar = (Toolbar) findViewById(R.id.tbProfile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("PROFILE");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = (TextView) findViewById(R.id.tvname);
        mobile = (TextView) findViewById(R.id.tvmobile);
        dob = (TextView) findViewById(R.id.tvdob);
        address = (TextView) findViewById(R.id.tvaddress);
        inherited = (TextView) findViewById(R.id.tvinherited);
        disease = (TextView) findViewById(R.id.tvdiseases);
        blood = (TextView) findViewById(R.id.tvblood);


            sharedPreferences = getSharedPreferences(MyProfile, MODE_PRIVATE);
            Sname = sharedPreferences.getString("Name", "");
            Smobile = sharedPreferences.getString("MobileNO", "");
            Saddress = sharedPreferences.getString("Address", "");
            Sdob = sharedPreferences.getString("DOB", "");
            Sblood = sharedPreferences.getString("BloodGroup", "");
            Sinherited = sharedPreferences.getString("InheritedDiseases", "");
            Sdiseases = sharedPreferences.getString("Diseases", "");

        if(!Sname.isEmpty())
            name.setText(Sname);

        if(!Smobile.isEmpty())
            mobile.setText(Smobile);

        if(!Saddress.isEmpty())
            address.setText(Saddress);

        if(!Sblood.isEmpty())
            blood.setText(Sblood);

        if(!Sdob.isEmpty())
            dob.setText(Sdob);

        if(!Sinherited.isEmpty())
            inherited.setText(Sinherited);

        if(!Sdiseases.isEmpty())
            disease.setText(Sdiseases);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.itedit){

            Intent intent = new Intent(this , EnterPersonalDetail.class);
            intent.putExtra("mobile", "edit");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}
