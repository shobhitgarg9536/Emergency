package in.silive.emergency;

import android.app.Dialog;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    double mLatitude =0;
    double mLongitude=0;
    String typeofemergency;
    HashMap<String, String> mMarkerPlaceLink = new HashMap<String, String>();
    Button normalView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        normalView = (Button) findViewById(R.id.btNormallView);

        typeofemergency = getIntent().getStringExtra("type");
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        if(status!= ConnectionResult.SUCCESS){ // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        }
        else {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
        normalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    normalView.setText("Satellite View");
                }
                else{
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    normalView.setText("Normal View");
                }
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //Creating object to retrive provider
        Criteria criteria = new Criteria();

        //Getting the provider
        String provider = locationManager.getBestProvider(criteria, false);

        // Getting Current Location From GPS
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        try {

            onLocationChanged(location);

        }catch (NullPointerException e){
            e.printStackTrace();
        }

        locationManager.requestLocationUpdates(provider, 20000, 0, new android.location.LocationListener() {

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

                Intent intent = new Intent(getBaseContext(), NearestPlaceDetails.class);

                String reference = mMarkerPlaceLink.get(arg0.getId());
                intent.putExtra("reference", reference);

                startActivity(intent);
            }
        });
        Thread thread = new Thread() {

            @Override
            public void run() {
                StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                sb.append("location="+mLatitude+","+mLongitude);
                sb.append("&radius=5000");
                sb.append("&types="+typeofemergency);
                sb.append("&key=AIzaSyABUAVb12NlYhSubPBAn5fz4Yc_c4RoxiM");

                PlacesAsynTask placesAsynTask = new PlacesAsynTask();

                placesAsynTask.execute(sb.toString());
            }
        };
        thread.start();

    }

    public void Zoom(View view){
        if(view.getId() == R.id.btzoomin){

            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        }

        if(view.getId() == R.id.btzoomout){

            mMap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }


    public void onLocationChanged(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
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

                // Setting the title for the marker.
                //This will be displayed on taping the marker
                markerOptions.title(name + " : " + vicinity);

                // Placing a marker on the touched position
                Marker m = mMap.addMarker(markerOptions);

                // Linking Marker id and place reference
                mMarkerPlaceLink.put(m.getId(), hmPlace.get("reference"));
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
}
