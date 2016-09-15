package in.silive.emergency;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Aniket on 12-09-2016.
 */
public class ConnectContactsAdapter extends ArrayAdapter{
    Context context;
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
                context.startActivity(intent);
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
        SharedPreferences preferences = context.getSharedPreferences("Profile", context.MODE_PRIVATE);
        /** add location before name **/

        String name = preferences.getString("Name", null);
        if(name != null)        builder.append(name);
        return builder;
    }

}
