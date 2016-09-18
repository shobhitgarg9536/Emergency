package in.silive.emergency;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Shobhit-pc on 9/16/2016.
 */
public class LocationAddress extends AsyncTask<Double , String ,String> {


    private Context mContext;
    public AddressResponse delegate = null;

    public LocationAddress(Context context ,AddressResponse addressResponse ){
        delegate = addressResponse;
        mContext = context;

    }


    @Override
    protected String doInBackground(Double... params) {

        double latitude = params[0];
        double longitude = params[1];


        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        String result = null;
        try {
            List<Address> addressList = geocoder.getFromLocation(
                    latitude, longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i)).append("\n");
                }
                sb.append(address.getLocality()).append("\n");
                sb.append(address.getPostalCode()).append("\n");
                sb.append(address.getCountryName());
                result = sb.toString();
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {

        try {

            delegate.processFinish(s);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}