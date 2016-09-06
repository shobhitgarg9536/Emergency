package in.silive.emergency;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aniket on 06-09-2016.
 */

/***
 * CustomAdapter converts the data to be displayed in each slot into a suitable View Object
 * 1. Store the ContactDataProvider in list. Use this list to getItem() and getCount()
 * 2. Method getView() is to return the View object which will display the required data
 *
 */
public class ContactsAdapter extends ArrayAdapter {

     List list = new ArrayList();
    public ContactsAdapter(Context context, int resource) {
        super(context, resource);
    }


    static class DataHandler {
        TextView nameView;
        TextView phoneView;
        CheckBox checkBox;
    }

    @Override
    public void add(Object object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.getItem(position);
    }

    /**
     * The getView() works as ..
     * 1. Create a row object of View type using inflater.
     * 2. Create a handler object which contains widget objects to store data
     * 3. Initialize the handler members.
     * 4. Create a object of ContactsDataProvider.
     * 5. Set the handler member properties and methods like setText() using the ContactsDataProvider
     *
     **/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        DataHandler handler;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            /** initialize a view object row with inflater **/
            row = inflater.inflate(R.layout.contact_row, parent, false);
            handler = new DataHandler();
            handler.nameView = (TextView)row.findViewById(R.id.tv_contactname);
            handler.phoneView = (TextView)row.findViewById(R.id.tv_contactno);
            handler.checkBox = (CheckBox)row.findViewById(R.id.cb_selectcontact);
            row.setTag(handler);
        }
        else {
            handler = (DataHandler)row.getTag();
        }
        ContactsDataProvider dataProvider;
        dataProvider = (ContactsDataProvider)this.getItem(position);
        handler.nameView.setText(dataProvider.getName());
        handler.phoneView.setText(dataProvider.getPhoneNumber());
        handler.checkBox.setSelected(false);
        return row;
    }
}
