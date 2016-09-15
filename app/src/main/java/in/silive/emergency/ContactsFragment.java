package in.silive.emergency;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Shobhit-pc on 9/6/2016.
 */
public class ContactsFragment extends Fragment {

    ConnectContactsAdapter adapter;

    public ContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contactView = inflater.inflate(R.layout.contacts_fragment, container, false);
        ListView listView = (ListView)contactView.findViewById(R.id.lv_connect_contact);
        DatabaseHandler dbHandler = new DatabaseHandler(getActivity());

        ArrayList<Contact> contactList = dbHandler.getContactList();
        adapter = new ConnectContactsAdapter(getActivity(), R.layout.connect_contact_row);

        for(int index = 0; index < contactList.size(); index++){
            adapter.add(contactList.get(index));
        }
        listView.setAdapter(adapter);

        return listView;

    }


}
