package in.silive.emergency;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class DeleteContacts extends AppCompatActivity {

    ContactsAdapter adapter;
    Button deleteButton;
    ListView listView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_contacts);
        deleteButton = (Button)findViewById(R.id.bt_delete);
        listView = (ListView)findViewById(R.id.lv_delete_contacts);
        toolbar = (Toolbar) findViewById(R.id.tbdelete);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Delete Contact");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new ContactsAdapter(getApplicationContext(), R.layout.select_contact_row, true);
        final DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        ArrayList<Contact> contactList = dbHandler.getContactList();

        /** add to contacts adapter **/
        for(int index = 0; index < contactList.size(); index++){
            adapter.add(contactList.get(index));
        }
        listView.setAdapter(adapter);


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(int index = 0; index < adapter.getCount(); index++){
                    Contact contact = (Contact) adapter.getItem(index);
                    if(contact.isSelected()){
                        dbHandler.deleteContact(contact);
                    }
                }
                Intent  intent = new Intent(DeleteContacts.this, FragmentCallingActivity.class);
                startActivity(intent);

            }
        });
    }


}