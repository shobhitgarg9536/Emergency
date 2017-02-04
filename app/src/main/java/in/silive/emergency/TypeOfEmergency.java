package in.silive.emergency;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class TypeOfEmergency extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    Button hospital, police, pharmacy;
    TextView address;
    double latitude = 0, longitude = 0;
    String currentaddress = "";
    android.support.v7.widget.CardView cardView;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    public TypeOfEmergency() {
        // empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.typeofemergency, container, false);

        hospital = (Button) linearLayout.findViewById(R.id.bthospital);
        police = (Button) linearLayout.findViewById(R.id.btpolice);
        pharmacy = (Button) linearLayout.findViewById(R.id.btpharmacy);
        address = (TextView) linearLayout.findViewById(R.id.tvaddress);
        cardView = (android.support.v7.widget.CardView)linearLayout.findViewById(R.id.cardviewlocation);


        hospital.setOnClickListener(this);
        police.setOnClickListener(this);
        pharmacy.setOnClickListener(this);

        buildGoogleApiClient();
        try {
            displayLocation();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        getAddress();

        //object of current location
        CurrentLocation getcurrentLocation = new CurrentLocation(getContext());

        boolean isNetworkenable = getcurrentLocation.isNetworkEnable();

        if (!isNetworkenable) {
            //showing alertDialog if gps is off
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

            alertDialog.setTitle("GPS is settings");

            alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
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

    private void getAddress() {
        final LocationAddress locationAddress = new LocationAddress(getContext(), new AddressResponse() {
            @Override
            public void processFinish(String output) {

                    currentaddress = output;
                    if (currentaddress != null) {
                        //if address is not null then make it visible in textview
                        cardView.setVisibility(View.VISIBLE);
                        address.setVisibility(View.VISIBLE);
                        address.setText("Your Location:\n" + currentaddress);
                    }
            }
        });

        locationAddress.execute(latitude, longitude);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bthospital:

                Intent intent = new Intent(getContext(), MapsActivity.class);
                intent.putExtra("type", "hospital");
                startActivity(intent);

                break;
            case R.id.btpharmacy:

                intent = new Intent(getContext(), MapsActivity.class);
                intent.putExtra("type", "pharmacy");
                startActivity(intent);

                break;
            case R.id.btpolice:

                intent = new Intent(getContext(), MapsActivity.class);
                intent.putExtra("type", "police");
                startActivity(intent);

                break;

        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    private void displayLocation() {


        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();

        }else{
            currentlocation();
        }

    }

    private void currentlocation() {

        //object of current location
        CurrentLocation currentLocation = new CurrentLocation(getContext());
        //accessing location
        Location location = currentLocation.getLocation(LocationManager.NETWORK_PROVIDER);
        //if location is not null
        if (location != null) {
            //get latitude from location
            latitude = location.getLatitude();
            //get longitude from location
            longitude = location.getLongitude();
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {

    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        displayLocation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

}
