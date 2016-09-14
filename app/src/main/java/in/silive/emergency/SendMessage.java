package in.silive.emergency;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This Activity will send an SMS to the contact obtained form ConnectContacts activity. The message body will show an emergency message
 */
public class SendMessage extends AppCompatActivity {

    TextView phoneNumberView;
    EditText messageEditText;
    Button sendMessage, discardMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        /** get the layout components **/
        phoneNumberView = (TextView)findViewById(R.id.tv_message_phone);
        messageEditText = (EditText)findViewById(R.id.et_emergency_message);
        sendMessage = (Button)findViewById(R.id.bt_sendmessage);
        discardMessage = (Button)findViewById(R.id.bt_discardmessage);

        /** set the phone number **/
        final String phoneNumber = getIntent().getStringExtra("message_phone");
        phoneNumberView.setText(phoneNumber);

        /** set the message **/
       final  StringBuilder messageBuilder = createEmergencyMessage();
        messageEditText.setText(messageBuilder.toString());

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** send message here **/
                String message = messageEditText.getText().toString();
                sendSMS(phoneNumber, message);
            }
        });


        discardMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /** go to previous activity **/
                finish();
                onBackPressed();

            }
        });

    }



    /**
     * Sends sms using SMS Manager.
     * @param phone     Destination phone number
     * @param message   Message to send
     */
    private void sendSMS(String phone, String message){
       try {
           SmsManager manager = SmsManager.getDefault();
           manager.sendTextMessage(phone, null, message, null, null);
           Toast.makeText( this, "Message sent Successfully !!", Toast.LENGTH_SHORT).show();
           finish(); //end activity
           Intent intent = new Intent(this, FragmentCallingActivity.class);
           startActivity(intent);
       }
       catch(Exception e){
           Toast.makeText( this, "Unable to send message. Please try again !!", Toast.LENGTH_LONG).show();
           Log.e("SMS", "Error sending message", e);
       }
    }

    /**
     * This method is used to create custom messages.
     * @return  A string builder containing the message
     */
    private StringBuilder createEmergencyMessage(){
        StringBuilder message = new StringBuilder(getResources().getString(R.string.emergency_message));

        /** add location of the person **/
            String location = "mysamplelocation" + '\n';
            message.append(location);

        /** add name **/
        SharedPreferences preferences = getSharedPreferences("Profile", Context.MODE_PRIVATE);
        String name = preferences.getString("Name", null);
        if(name != null){
            /** Do if the name successfully retrieved **/
            message.append(name);
        }


        return message;
    }

}
