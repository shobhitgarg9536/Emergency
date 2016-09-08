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

public class NearestPlaceDetails  extends AsyncTask<String, Integer, HashMap<String, String>> {

        HashMap<String, String> placedetail;

        public MapAsynResponse delegate = null;

    public NearestPlaceDetails(MapAsynResponse asyncResponse) {
        delegate = asyncResponse;//Assigning call back interfacethrough constructor
    }

        @Override
        protected HashMap<String, String> doInBackground(String... params) {


            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try{
                URL url = new URL(params[0]);


                urlConnection = (HttpURLConnection) url.openConnection();


                urlConnection.connect();


                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb = new StringBuffer();

                String line = "";
                while( ( line = br.readLine()) != null){
                    sb.append(line);
                }


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

                delegate.processFinish(result);
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

                if(!jPlaceDetails.isNull("name")){
                    name = jPlaceDetails.getString("name");
                }


                if(!jPlaceDetails.isNull("icon")){
                    icon = jPlaceDetails.getString("icon");
                }


                if(!jPlaceDetails.isNull("vicinity")){
                    vicinity = jPlaceDetails.getString("vicinity");
                }


                if(!jPlaceDetails.isNull("formatted_address")){
                    formatted_address = jPlaceDetails.getString("formatted_address");
                }


                if(!jPlaceDetails.isNull("formatted_phone_number")){
                    formatted_phone = jPlaceDetails.getString("formatted_phone_number");
                }


                if(!jPlaceDetails.isNull("website")){
                    website = jPlaceDetails.getString("website");
                }



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

