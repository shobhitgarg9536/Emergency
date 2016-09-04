package in.silive.emergency;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class NearestPlaceDetails extends Activity{

    TextView name,phone,website,international,vicinity,address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.nearestplacedetails);

        name =(TextView) findViewById(R.id.tvName);
        phone =(TextView) findViewById(R.id.tvphone);
        website =(TextView) findViewById(R.id.tvwebsite);
        address =(TextView) findViewById(R.id.tvaddress);
        international =(TextView) findViewById(R.id.tvinternational);
        vicinity =(TextView) findViewById(R.id.tvVicinity);


        String reference = getIntent().getStringExtra("reference");

        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
        sb.append("reference="+reference);
        sb.append("&sensor=true");
        sb.append("&key=AIzaSyABUAVb12NlYhSubPBAn5fz4Yc_c4RoxiM");

        PlaceDetailAysnTask pdAysnTask = new PlaceDetailAysnTask();
        pdAysnTask.execute(sb.toString());

    }

    private class PlaceDetailAysnTask extends AsyncTask<String, Integer, HashMap<String, String>> {

        HashMap<String, String> placedetail;

        @Override
        protected HashMap<String, String> doInBackground(String... params) {

            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try{
                URL url = new URL(params[0]);

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

                JSONObject jsonObject = new JSONObject(sb.toString());
                JSONObject jsonPlace = jsonObject.getJSONObject("result");
                placedetail = getPlaceDetails(jsonPlace);
            }catch(Exception e){

            }finally{
                try {
                    iStream.close();
                    urlConnection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return placedetail;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> result) {
            try {
                name.setText(result.get("name").toString());
                address.setText(result.get("formatted_address").toString());
                phone.setText(result.get("formatted_phone_number").toString());
                international.setText(result.get("international_phone_number").toString());
                website.setText(result.get("website").toString());
                vicinity.setText(result.get("vicinity").toString());
            }catch (Exception e){
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
        private HashMap<String, String> getPlaceDetails(JSONObject jPlaceDetails){

            HashMap<String, String> hPlaceDetails = new HashMap<String, String>();

            String name = "";
            String icon = "";
            String vicinity="";
            String latitude="";
            String longitude="";
            String formatted_address="";
            String formatted_phone="";
            String website="";
            String international_phone_number="";


            try {
                // Extracting Place name, if available
                if(!jPlaceDetails.isNull("name")){
                    name = jPlaceDetails.getString("name");
                }

                // Extracting Icon, if available
                if(!jPlaceDetails.isNull("icon")){
                    icon = jPlaceDetails.getString("icon");
                }

                // Extracting Place Vicinity, if available
                if(!jPlaceDetails.isNull("vicinity")){
                    vicinity = jPlaceDetails.getString("vicinity");
                }

                // Extracting Place formatted_address, if available
                if(!jPlaceDetails.isNull("formatted_address")){
                    formatted_address = jPlaceDetails.getString("formatted_address");
                }

                // Extracting Place formatted_phone, if available
                if(!jPlaceDetails.isNull("formatted_phone_number")){
                    formatted_phone = jPlaceDetails.getString("formatted_phone_number");
                }

                // Extracting website, if available
                if(!jPlaceDetails.isNull("website")){
                    website = jPlaceDetails.getString("website");
                }


                // Extracting rating, if available
                if(!jPlaceDetails.isNull("international_phone_number")){
                    international_phone_number = jPlaceDetails.getString("international_phone_number");
                }


                latitude = jPlaceDetails.getJSONObject("geometry").getJSONObject("location").getString("lat");
                longitude = jPlaceDetails.getJSONObject("geometry").getJSONObject("location").getString("lng");

                hPlaceDetails.put("name", name);
                hPlaceDetails.put("icon", icon);
                hPlaceDetails.put("vicinity", vicinity);
                hPlaceDetails.put("lat", latitude);
                hPlaceDetails.put("lng", longitude);
                hPlaceDetails.put("formatted_address", formatted_address);
                hPlaceDetails.put("formatted_phone", formatted_phone);
                hPlaceDetails.put("website", website);
                hPlaceDetails.put("international_phone_number", international_phone_number);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return hPlaceDetails;
        }
    }
}
