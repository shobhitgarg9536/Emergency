package in.silive.emergency;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/** Activity Class which displays the selected contacts from SelectContact activity and saves them into
 * the database. This class does not uses an AsyncTask class. No progress will be shown.
 */
public class ShowSelectedContacts extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;      // listView for showing all contacts
    ContactsAdapter adapter;        // adapter for saving data to view
    Button button;                  // save button
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_selected_contacts);
        listView = (ListView)findViewById(R.id.lv_show_selected_contacts);
        toolbar = (Toolbar) findViewById(R.id.tbShowContacts);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Selected Contacts");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /** Get the selected contacts passed by the SelectContacts activity**/
        Bundle bundle = getIntent().getExtras();

        /** store the names and phones separately **/
        ArrayList<String> contactName = bundle.getStringArrayList("contact_name");
        ArrayList<String> contactPhone = bundle.getStringArrayList("contact_phone");

        adapter = new ContactsAdapter(getApplicationContext(), R.layout.select_contact_row, false);
        button = (Button)findViewById(R.id.bt_save_contacts);
        button.setOnClickListener(this);            // set action listener
        adapter.clear();        // clear the adapter

        /** add the contact to adapter **/
        for(int index = 0; index < contactName.size(); index++){
            adapter.add(new Contact(contactName.get(index), contactPhone.get(index)));
        }
        listView.setAdapter(adapter);
    }



    /**
     * Saves the contacts into the database when a view is clicked. The view is assumed to be
     * the save button. If the contacts are already present in the dialog then an alert message is shown.
     * @param view  View clicked.
     */
    @Override
    public void onClick(View view) {
        /** if save button is clicked **/
        if(view.getId() == button.getId()){

            /** retrieve the contacts from adapter and store it in database **/
            DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
            ArrayList<Contact> databaseContactList = dbHandler.getContactList();


            for(int index = 0; index < adapter.getCount(); index++) {
                Contact contact = (Contact) adapter.getItem(index);
                if (!contact.isInList(databaseContactList)) dbHandler.putContact(contact);
            }

            Intent intent = new Intent(getApplicationContext(), FragmentCallingActivity.class);
            startActivity(intent);


        }
    }


}// end of class