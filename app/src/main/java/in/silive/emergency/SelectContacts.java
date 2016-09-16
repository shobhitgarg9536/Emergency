package in.silive.emergency;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class SelectContacts extends AppCompatActivity implements Button.OnClickListener {



    ListView listView;
    ArrayList<String> contactNames = new ArrayList<>(); // for storing selected names
    ArrayList<String> contactPhones = new ArrayList<>() ;   // for storing selected phone numbers
    ContactsAdapter adapter;
    Button button ;
    RelativeLayout relativeLayout;                          // to show loading
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contacts);
        button = (Button)findViewById(R.id.bt_next);
        button.setOnClickListener(this);
        toolbar = (Toolbar) findViewById(R.id.tbContacts);
        relativeLayout = (RelativeLayout) findViewById(R.id.rlprogessbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Contacts");

        listView = (ListView)findViewById(R.id.lv_contacts);
        /** if any item is clicked, then contacts will change its selected state **/

        /** for loading contacts **/
        MySyncTask task = new MySyncTask();
        task.execute();
    }



    @Override
    public void onClick(View view) {
        /** When next button is clicked **/
        if(view.getId() == button.getId()){

            /** clear the contactNames and contactPhones before adding any, useful to avoid redundancy when back button is pressed **/
            contactNames.clear(); contactPhones.clear();
            /** add selected contact's name and phone to respective array list **/

            Bundle bundle = new Bundle();           // for storing data and passind to next activity
            for(int adapterIndex = 0, listIndex = 0; adapterIndex <adapter.getCount(); adapterIndex++){
                Contact contact = (Contact)adapter.getItem(adapterIndex);
                if(contact.isSelected()) {
                    contactNames.add(listIndex, contact.getName());
                    contactPhones.add(listIndex, contact.getPhoneNumber());

                }
            }

            if(contactNames.size() == 0 || contactPhones.size() == 0)    Toast.makeText(this,R.string.select_contacts_error,Toast.LENGTH_SHORT).show();
            else {
                // save to bundle
                bundle.putStringArrayList("contact_name", contactNames);
                bundle.putStringArrayList("contact_phone", contactPhones);
                // pass to next activity
                Intent intent = new Intent(SelectContacts.this, ShowSelectedContacts.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }



    private ArrayList<Contact> retrieveContacts(){

        ArrayList<Contact> contactList = new ArrayList<>();
        /** To perform query over retrieval of contacts **/
        ContentResolver resolver = getContentResolver();
        /** gives you access to database **/

        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        int index = 0;
        while(cursor.moveToNext()) {    /* returns false if the cursor is already past the last entry */
            // ContactsContracts.Contact contains constants for contacts table
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            /** save to contactNames array **/
            String tempName =cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            if(cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {   // has atlease one phone

                // to navigate through different phone numbers, only one phone number per contact is shown
                Cursor phones = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                if (phones != null) {
                    phones.moveToFirst();
                    /** save to contactPhones array list**/
                        String tempPhone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        if (tempPhone != null) {
                            contactList.add(index, new Contact(tempName, tempPhone));
                            ++index;
                        }

                    phones.close();
                }
            }


        }
        cursor.close();
        return contactList;
    }





    private class MySyncTask extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            adapter = new ContactsAdapter(getApplicationContext(), R.layout.select_contact_row, true);

            /** retrieve contacts and add them to adapter **/
            ArrayList<Contact> contactList = retrieveContacts();
            ArrayList<Contact>  databaseContactList = new DatabaseHandler(getApplicationContext()).getContactList();

            /** first sor the list **/
            SelectContacts.sort(contactList);

            for(int index = 0; index < contactList.size(); index++){   // add retrieved contacts to adapter
                Contact contact = contactList.get(index);
                if(contact.isInList(databaseContactList))       contact.setSelected(true);
                adapter.add(contact);
            }

            return null;
        } /** end of method **/


        @Override
        protected void onPreExecute() {
            /** show loading **/
          relativeLayout.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Object o) {
            listView.setAdapter(adapter);
            /** if loading , them stop it **/
           relativeLayout.setVisibility(View.GONE);
        }
    }


    /**
     * Sorts a contact list using Collections.sort() method in ascending order.
     * @param contactList   List to be sorted.
     */
    private static void sort(ArrayList<Contact> contactList) {
        if(contactList != null) {
            Collections.sort(contactList, new Comparator<Contact>() {
                @Override
                public int compare(Contact contact1, Contact contact2) {
                    return ( contact1.getName().compareToIgnoreCase(contact2.getName()) );
                }
            });
        }
    }

}// end of class