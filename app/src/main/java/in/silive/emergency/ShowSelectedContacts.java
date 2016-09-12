package in.silive.emergency;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/** Activity Class which displays the selected contacts from SelectContact activity and saves them into
 * the database. This class does not uses an AsyncTask class. No progress will be shown.
 */
public class ShowSelectedContacts extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;      // listView for showing all contacts
    ContactsAdapter adapter;        // adapter for saving data to view
    Button button;                  // save button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_selected_contacts);
        listView = (ListView)findViewById(R.id.lv_show_selected_contacts);

       /** Get the selected contacts passed by the SelectContacts activity**/
        Bundle bundle = getIntent().getExtras();

        /** store the names and phones separately **/
        ArrayList<String> contactName = bundle.getStringArrayList("contact_name");
        ArrayList<String> contactPhone = bundle.getStringArrayList("contact_phone");

        adapter = new ContactsAdapter(getApplicationContext(), R.layout.select_contact_row, false);
        button = (Button)findViewById(R.id.bt_save_contacts);
        button.setOnClickListener(this);    // set action listener

        /** add the contact to adapter **/
        for(int index = 0; index < contactName.size(); index++){
            adapter.add(new Contact(contactName.get(index), contactPhone.get(index)));
        }
        listView.setAdapter(adapter);
    }

    /**
     * Saves the contacts into the database when a view is clicked. The view is assumed to be
     * the save button.
     * @param view  View clicked.
     */
    @Override
    public void onClick(View view) {
        /** if save button is clicked **/
        if(view.getId() == button.getId()){
            DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());

            /** clear the previous database **/
            dbHandler.clearDatabase();

            /** retrieve the contacts from adapter and store it in database **/
            for(int index = 0; index < adapter.getCount(); index++){
                Contact contact = (Contact)adapter.getItem(index);
                    dbHandler.putContact(contact);
                }

            Intent intent = new Intent(this , FragmentCallingActivity.class);
            startActivity(intent);
            }
    }



    @Override
    public void onBackPressed(){        // when back key is pressed
        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        /** clear the previous database **/
        dbHandler.clearDatabase();
        Intent intent = new Intent(this, SelectContacts.class);
        startActivity(intent);
    }


}// end of class