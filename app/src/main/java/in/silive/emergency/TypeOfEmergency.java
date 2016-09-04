package in.silive.emergency;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by Shobhit-pc on 8/31/2016.
 */
public class TypeOfEmergency extends AppCompatActivity implements View.OnClickListener {
Button hospital,police,fireBridage,contacts;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.typeofemergency);
        hospital = (Button) findViewById(R.id.bthospital);
        police = (Button) findViewById(R.id.btpolice);
        fireBridage = (Button) findViewById(R.id.btfirebrigade);
        contacts = (Button) findViewById(R.id.btcontacts);
        hospital.setOnClickListener(this);
        police.setOnClickListener(this);
        fireBridage.setOnClickListener(this);
        contacts.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.bthospital:
                Intent intent = new Intent(this, MapsActivity.class);
                intent.putExtra("type","hospital");
                startActivity(intent);

                break;
            case R.id.btpolice:
                intent = new Intent(this, MapsActivity.class);
                intent.putExtra("type","police");
                startActivity(intent);
                break;
            case R.id.btfirebrigade:
                intent = new Intent(this, MapsActivity.class);
                intent.putExtra("type","fire_station");
                startActivity(intent);
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.emergencymenu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.about:

                break;
            case R.id.profile:

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
