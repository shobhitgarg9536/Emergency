package in.silive.emergency;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class ConnectContacts extends AppCompatActivity {
    ConnectContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_contacts);
        ListView listView = (ListView)findViewById(R.id.lv_connect_contact);
        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        ArrayList<Contact> contactList = dbHandler.getContactList();
        adapter = new ConnectContactsAdapter(ConnectContacts.this, R.layout.connect_contact_row);
        for(int index = 0; index < contactList.size(); index++){
            adapter.add(contactList.get(index));
        }
        listView.setAdapter(adapter);

    }
}
