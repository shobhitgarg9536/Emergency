package in.silive.emergency;

import android.app.Dialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import android.os.Bundle;

import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    double mLatitude =0;
    double mLongitude=0;
    String typeofemergency;
    HashMap<String, String> mMarkerPlaceLink = new HashMap<String, String>();
    RelativeLayout progessLayout;
    SlidingDrawer slidingDrawerMove;
    TextView phone,website,international,vicinity,address,slidingText;
    private Toolbar toolbar;
    Thread thread;
    int count=5000;
    Location location;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        typeofemergency = getIntent().getStringExtra("type");

        slidingDrawerMove = (SlidingDrawer) findViewById(R.id.sdmove);
        progessLayout = (RelativeLayout) findViewById(R.id.rlprogesslayout);

        slidingText =(TextView) findViewById(R.id.handle);

        phone =(TextView) findViewById(R.id.tvphone);
        website =(TextView) findViewById(R.id.tvwebsite);
        address =(TextView) findViewById(R.id.tvaddress);
        international =(TextView) findViewById(R.id.tvinternational);
        vicinity =(TextView) findViewById(R.id.tvVicinity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(typeofemergency.equals("hospital"))
        getSupportActionBar().setTitle("HOSPITAL");
        if(typeofemergency.equals("pharmacy"))
            getSupportActionBar().setTitle("PHARMACY");
        if(typeofemergency.equals("police"))
            getSupportActionBar().setTitle("POLICE");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        if(status!= ConnectionResult.SUCCESS){ // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        }
        else {
            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);
            }
            else {

                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(this);
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Sorry, your device doesn't connect to internet!");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                alertDialog.create();
                alertDialog.show();
            }

        }
        slidingDrawerMove.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                slidingDrawerMove.setClickable(false);
            }
        });
        slidingDrawerMove.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                slidingDrawerMove.setClickable(true);
            }
        });


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setPadding(0 ,0,0,100);
        mMap.getUiSettings().setCompassEnabled(true);

        mMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


/*
        // Getting Current Location From GPS
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        try {

            onLocationChanged(location);

        }catch (NullPointerException e){
            e.printStackTrace();
        }

        locationManager.requestLocationUpdates(String.valueOf(location), 40000, 0, new android.location.LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();

                LatLng latLng = new LatLng(mLatitude, mLongitude);

                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });
*/
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);


        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
if(isGPSEnabled && isNetworkEnabled) {

    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


}
        else {
    if(isGPSEnabled)
    location = locationManager
            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
    else {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("GPS is settings");

        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alertDialog.show();

    }

}

        try {

            onLocationChanged(location);

        }catch (NullPointerException e){
            e.printStackTrace();
        }

        locationManager.requestLocationUpdates(String.valueOf(location), 40000, 0, new android.location.LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();

                LatLng latLng = new LatLng(mLatitude, mLongitude);

                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });




        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {
                progessLayout.setVisibility(View.VISIBLE);
                String reference = mMarkerPlaceLink.get(arg0.getId());
                StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");

                sb.append("reference="+reference);
                sb.append("&sensor=true");
                sb.append("&key=AIzaSyABUAVb12NlYhSubPBAn5fz4Yc_c4RoxiM");

                NearestPlaceDetails pdAysnTask = new NearestPlaceDetails(new MapAsynResponse() {
                    @Override
                    public void processFinish(HashMap<String, String> output) {

                        slidingDrawerMove.setVisibility(View.VISIBLE);
                        progessLayout.setVisibility(View.INVISIBLE);

                        slidingDrawerMove.open();

                        slidingText.setText(output.get("name").toString());
                        address.setText(output.get("formatted_address").toString());
                        phone.setText(output.get("formatted_phone_number").toString());
                        international.setText(output.get("international_phone_number").toString());
                        website.setText(output.get("website").toString());
                        vicinity.setText(output.get("vicinity").toString());

                    }
                });

                pdAysnTask.execute(sb.toString());


            }
        });
             /*   mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {


                        progessLayout.setVisibility(View.VISIBLE);

                        String reference = mMarkerPlaceLink.get(marker.getId());

                        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");

                        sb.append("reference="+reference);
                        sb.append("&sensor=true");
                        sb.append("&key=AIzaSyABUAVb12NlYhSubPBAn5fz4Yc_c4RoxiM");

                        NearestPlaceDetails pdAysnTask = new NearestPlaceDetails(new MapAsynResponse() {
                            @Override
                            public void processFinish(HashMap<String, String> output) {

                                slidingDrawerMove.setVisibility(View.VISIBLE);
                                slidingText.setText(output.get("name").toString());
                                address.setText(output.get("formatted_address").toString());
                                phone.setText(output.get("formatted_phone_number").toString());
                                international.setText(output.get("international_phone_number").toString());
                                website.setText(output.get("website").toString());
                                vicinity.setText(output.get("vicinity").toString());

                            }
                        });
                        pdAysnTask.execute(sb.toString());
                        progessLayout.setVisibility(View.INVISIBLE);
                        slidingDrawerMove.open();
                        return true;
                    }
                });*/

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                slidingDrawerMove.setVisibility(View.INVISIBLE);
            }
        });



        thread = new Thread() {

            @Override
            public void run() {
                StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                sb.append("location="+mLatitude+","+mLongitude);
                sb.append("&radius="+count);
                sb.append("&types="+typeofemergency);
                sb.append("&key=AIzaSyABUAVb12NlYhSubPBAn5fz4Yc_c4RoxiM");

                PlacesAsynTask placesAsynTask = new PlacesAsynTask();

                placesAsynTask.execute(sb.toString());
            }
        };
        thread.start();

    }




    public void onLocationChanged(Location location) {

        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        if( mLatitude == 0 && mLongitude == 0){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            alertDialog.setTitle("Network Error");

            alertDialog.setMessage("Network Connectivity Error! Try Again");

            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    finish();
                }
            });

            alertDialog.show();

        }

        LatLng latLng = new LatLng(mLatitude, mLongitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
    }


    public class PlacesAsynTask extends AsyncTask<String,Integer , List<HashMap<String, String>>> {
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        String data="";
        JSONArray jsonPlaces=null;
        List<HashMap<String, String>> placesList;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... params) {
            String nearByUrl = params[0];
            try{
                URL url = new URL(nearByUrl);

                // Creating an http connection to communicate with url
                urlConnection = (HttpURLConnection) url.openConnection();

                // Connecting to url
                urlConnection.connect();

                // Reading data from url
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb = new StringBuffer();

                String line = "";
                while( ( line = br.readLine()) != null){
                    sb.append(line);
                }
                data = sb.toString();

                br.close();

                JSONObject jsonObject = new JSONObject(data);

                try {
                    jsonPlaces = jsonObject.getJSONArray("results");
                    int placesCount = jsonPlaces.length();

                    placesList = new ArrayList<HashMap<String,String>>();

                    HashMap<String, String> place = null;

                    for(int i=0; i<placesCount;i++){
                        try {

                            place = getPlace((JSONObject)jsonPlaces.get(i));

                            placesList.add(place);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }


            }catch (Exception e){
                e.printStackTrace();
            }
            finally {
                try {
                    iStream.close();
                    urlConnection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return placesList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            mMap.clear();
            progessLayout.setVisibility(View.INVISIBLE);
            for(int i=0;i<result.size();i++){

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Getting a place from the places list
                HashMap<String, String> hmPlace = result.get(i);

                // Getting latitude of the place
                double lat = Double.parseDouble(hmPlace.get("lat"));

                // Getting longitude of the place
                double lng = Double.parseDouble(hmPlace.get("lng"));

                // Getting name
                String name = hmPlace.get("place_name");

                // Getting vicinity
                String vicinity = hmPlace.get("vicinity");

                LatLng latLng = new LatLng(lat, lng);

                // Setting the position for the marker
                markerOptions.position(latLng);

                if(typeofemergency.equals("hospital"))
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.hospitalicon));
                if(typeofemergency.equals("pharmacy"))
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pharmacyicon));
                if(typeofemergency.equals("police"))
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.policeicon));



                // Setting the title for the marker.
                //This will be displayed on taping the marker
                markerOptions.title(name + " : " + vicinity);



                // Placing a marker on the touched position
                Marker m = mMap.addMarker(markerOptions);

                // Linking Marker id and place reference
                mMarkerPlaceLink.put(m.getId(), hmPlace.get("reference"));
            }
            if(result.isEmpty()){
                count +=5000;
                if(thread.isAlive()){
                    thread.interrupt();
                }
                thread = new Thread();
                thread.start();
            }
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        private HashMap<String, String> getPlace(JSONObject jPlace){

            HashMap<String, String> place = new HashMap<String, String>();
            String placeName = "";
            String vicinity="";
            String latitude="";
            String longitude="";
            String reference="";

            try {

                if(!jPlace.isNull("name")){
                    placeName = jPlace.getString("name");
                }


                if(!jPlace.isNull("vicinity")){
                    vicinity = jPlace.getString("vicinity");
                }

                latitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lat");
                longitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lng");
                reference = jPlace.getString("reference");

                place.put("place_name", placeName);
                place.put("vicinity", vicinity);
                place.put("lat", latitude);
                place.put("lng", longitude);
                place.put("reference", reference);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return place;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.view) {
            if(mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

            }
            else{
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onRestart() {
        super.onRestart();

        Intent intent = getIntent();
        finish();
        startActivity(intent);

    }
}
