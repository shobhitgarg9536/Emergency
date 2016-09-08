package in.silive.emergency;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;



public class SelectContacts extends AppCompatActivity {

    ListView listView;
    ArrayList<String> contactNames = new ArrayList<>();
    ArrayList<String> contactPhones = new ArrayList<>() ;
    ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contacts);
        listView = (ListView)findViewById(R.id.lv_contacts);
        adapter = new ContactsAdapter(getApplicationContext(), R.layout.contact_row);
        retrieveContacts();
        for(int index = 0; index < contactNames.size(); index++){
            ContactsDataProvider contactObject = new ContactsDataProvider(contactNames.get(index), contactPhones.get(index));
            adapter.add(contactObject);
            ++index;
        }
    }


    private void retrieveContacts(){
        /** To perform query over retrieval of contacts **/
        ContentResolver resolver = getContentResolver();
        /** gives you access to database **/
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            int index = 0;
        while(cursor.moveToNext()) {    /* returns false if the cursor is already past the last entry */
            // ContactsContracts.Contacts contains constants for contacts table
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

            /** save to contactNames array **/
            String tempName =cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            if (hasPhone.equalsIgnoreCase("1"))     // has atlease one phone
            {
                Cursor phones = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id, null, null);
                phones.moveToFirst();
                /** save to contactPhones array **/
                String tempPhone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                if(android.telephony.PhoneNumberUtils.isGlobalPhoneNumber(tempPhone)){ // add contacts with valid phone number only
                contactNames.add(index, tempName);
                contactPhones.add(index, tempPhone);
                    phones.close();
            }

            }
            ++index;
        }
        cursor.close();
    } /** end of method **/


}
