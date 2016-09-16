package in.silive.emergency;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class TypeOfEmergency extends Fragment implements View.OnClickListener {
Button hospital,police,pharmacy;
TextView address;
double latitude = 0,longitude =0;
    String currentaddress = "";

    public TypeOfEmergency() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        LinearLayout linearLayout = (LinearLayout)inflater.inflate(R.layout.typeofemergency, container, false);

        hospital = (Button) linearLayout.findViewById(R.id.bthospital);
        police = (Button) linearLayout.findViewById(R.id.btpolice);
        pharmacy = (Button) linearLayout.findViewById(R.id.btpharmacy);
        address = (TextView) linearLayout.findViewById(R.id.tvaddress);



        hospital.setOnClickListener(this);
        police.setOnClickListener(this);
        pharmacy.setOnClickListener(this);

        CurrentLocation currentLocation = new CurrentLocation(getContext());

        Location location =currentLocation.getLocation(LocationManager.NETWORK_PROVIDER);

        if(location!=null){
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            final LocationAddress locationAddress = new LocationAddress(getContext() ,new AddressResponse() {
                @Override
                public void processFinish(String output) {

                    currentaddress = output;
                    if(currentaddress!=null) {
                        address.setVisibility(View.VISIBLE);
                        address.setText("Your Location:\n" + currentaddress);
                    }
                }
            } );
            locationAddress.execute(latitude,longitude);




        }
        else {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

            alertDialog.setTitle("GPS is settings");

            alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    getContext().startActivity(intent);
                }
            });

            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.setCancelable(true);
                }
            });


            alertDialog.setCancelable(false);
            alertDialog.show();

        }

        return linearLayout;



    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.bthospital:

                Intent intent = new Intent(getContext(), MapsActivity.class);
                intent.putExtra("type","hospital");

                startActivity(intent);

                break;
            case R.id.btpharmacy:

                intent = new Intent(getContext(), MapsActivity.class);
                intent.putExtra("type","pharmacy");

                startActivity(intent);

                break;
            case R.id.btpolice:

                intent = new Intent(getContext(), MapsActivity.class);
                intent.putExtra("type","police");

                startActivity(intent);

                break;

        }
    }


}
