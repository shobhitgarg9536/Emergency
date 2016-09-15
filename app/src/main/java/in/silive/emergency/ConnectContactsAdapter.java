package in.silive.emergency;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Aniket on 12-09-2016.
 */
public class ConnectContactsAdapter extends ArrayAdapter implements LocationListener{
    Context context;

    double mLatitude =0;
    double mLongitude=0;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    Location location;

    ArrayList<Contact> list = new ArrayList<>();
    public ConnectContactsAdapter(Context context, int resource) {
        super(context, resource);
        this.context =context;
    }



    private class ViewHolder {
        TextView nameView;
        TextView phoneView;
        ImageButton callButton;
        ImageButton smsButton;
    }

    @Override
    public void add(Object object) {
        list.add((Contact)object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;         // Each row of the listView
        ViewHolder holder;

        if(row == null){
            /** create holder and row **/
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(R.layout.connect_contact_row, null);
            holder = new ViewHolder();

            /** Initialize holder **/
            holder.nameView = (TextView)row.findViewById(R.id.tv_connect_contactname);
            holder.phoneView = (TextView)row.findViewById(R.id.tv_connect_contactphone);
            holder.callButton = (ImageButton)row.findViewById(R.id.bt_call);
            holder.smsButton = (ImageButton)row.findViewById(R.id.bt_sendmessage);
            row.setTag(holder);         // can be used to store data within a view
        }
        else        holder = (ViewHolder) row.getTag();

        /** storing data into the holder **/
        final Contact contact = (Contact) this.getItem(position); // get contact
        holder.nameView.setText(contact.getName());
        holder.phoneView.setText(contact.getPhoneNumber());     // save data into respective views
        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + contact.getPhoneNumber().trim()));

                    /** Validate phone number before calling **/
                    context.startActivity(intent);


                }
                catch(Exception e){
                    Log.e("Call", e.getMessage(), e);
                }
            }
        });

        holder.smsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** CALL SMS ACTIVITY **/


                    String phoneNumber = contact.getPhoneNumber().trim();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
                    intent.putExtra("sms_body", createEmergencyMessage().toString());
                if(mLatitude !=0 && mLongitude !=0) {
                    context.startActivity(intent);
                }
                else
                {

                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    alertDialog.setTitle("Location");

                    alertDialog.setMessage("Cannot access location! Try again");

                    alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            alertDialog.setCancelable(true);
                        }
                    });




                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }
            }
        });

        return row;
    }

    /**
     * The method validates the phone number before calling
     * @param number
     * @return
     */
    private boolean isValidPhoneNumber(String number){
        if(number.length()!= 0)     return false;
        return    Patterns.PHONE.matcher(number).matches();

    }


    private StringBuilder createEmergencyMessage(){
        StringBuilder builder = new StringBuilder(context.getResources().getString(R.string.emergency_message));

        Location();

        SharedPreferences preferences = context.getSharedPreferences("Profile", context.MODE_PRIVATE);
        /** add location before name **/

        String Slocation = "Latitude: "+ String.valueOf(mLatitude) + " , "+"Longitude: "+String.valueOf(mLongitude)+"\n";
        builder.append(Slocation);
        String name = preferences.getString("Name", null);
        if(name != null)        builder.append(name);
        return builder;
    }

    private void Location() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);



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
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

                alertDialog.setTitle("GPS is settings");

                alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);
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

        if(mLatitude ==0 && mLongitude ==0) {

        }



    }

    @Override
    public void onLocationChanged(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();


    }


}
