package in.silive.emergency;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Aniket on 06-09-2016.
 */

/***
 * ContactAdapter converts the data to be displayed in each slot into a suitable View Object. The adapter will be used for
 * multiple list views. It contains a nested ViewHolder class containing the widgets used to display information.
 * 1. Store the ContactDataProvider in list. Use this list to getItem() and getCount()
 * 2. Method getView() is to return the View object which will display the required data
 *
 */
public class ContactsAdapter extends ArrayAdapter {

    /** List to store the contacts **/
    private ArrayList<Contact> list = new ArrayList();

    /** Boolean value to store whether the adapter will be used for selecting contacts or displaying contacts **/
    private boolean isSelectionAdapter;

    /** Simple constructor **/
    public ContactsAdapter(Context context, int resourceId, boolean isSelectionAdapter) {
        super(context, resourceId);
        this.isSelectionAdapter = isSelectionAdapter;
    }


    /** Getter method for isSelectionAdapter **/
    public boolean isSelectionAdapter() {
        return isSelectionAdapter;
    }


    /** Setter method for isSelectionAdapter **/
    public void setSelectionAdapter(boolean selectionAdapter) {
        isSelectionAdapter = selectionAdapter;
    }

    /** ViewHolder class will contain the view used to display data **/
   private static class ViewHolder {
        TextView nameView;      // display name of contact
        TextView phoneView;     // display phone of contact
        CheckBox checkBox;      // only for selecting contact

    }


    /** Setter method for list **/
    public void setList(ArrayList<Contact> contactList){
        this.list = contactList;
    }


    @Override
    public void add(Object object) {
        list.add((Contact)object);     // add the object to the list
        super.add(object);
    }


    @Override
    public int getCount() {
        return this.list.size();
    }

    /**
     * Returns the item stored in the list of adapter
     * @param position Position of item in the list.
     * @return  Object stored in the list. The list is of Contact type.
     */
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    /**
     * Clears the list of the adapter. Removes all the contacts saved in the list.
     */
    public void clear(){
        list.clear();
    }

    /**
     * The getView() works as ..
     * 1. Create a row object of View type using inflater.
     * 2. Create a holder object which contains widget objects to store data
     * 3. Initialize the handler members.
     * 4. Create a object of Contact.
     * 5. Set the handler member properties and methods like setText() using the Contact
     *
     */

    /**
     * The getView methods saves the data of the list and generates individual object for listView. The individual view contains
     * the different views like TextView, Checkbox which will be used to display data.
     * @param position  Position of view in listView, can also be used as index for list.
     * @param convertView  View to be displayed
     * @param parent    Parent View
     * @return  View after saving the data in it.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;         // Each row of the listView
        final ViewHolder holder;

        if(row == null){
            /** create holder and row **/
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(R.layout.select_contact_row, null);
            holder = new ViewHolder();

            /** Initialize holder **/
            holder.nameView = (TextView)row.findViewById(R.id.tv_contactname);
            holder.phoneView = (TextView)row.findViewById(R.id.tv_contactno);
            holder.checkBox = (CheckBox)row.findViewById(R.id.cb_selectcontact);
            row.setTag(holder);         // can be used to store data within a view
        }
        else        holder = (ViewHolder) row.getTag();

        /** storing data into the holder **/
            final Contact contact = (Contact) this.getItem(position); // get contact
            holder.nameView.setText(contact.getName());
            holder.phoneView.setText(contact.getPhoneNumber());     // save data into respective views

           /** If adapter will be used for selecting contacts then make checkbox visible else gone **/
            if(this.isSelectionAdapter)    holder.checkBox.setVisibility(CheckBox.VISIBLE);
            else                            holder.checkBox.setVisibility(CheckBox.GONE);   /** free up space on layout **/

            holder.checkBox.setSelected(contact.isSelected()); // set checkbox selection state according to contacts selected state
            holder.checkBox.setChecked(contact.isSelected());  // set checkbox checked state
            holder.checkBox.setOnCheckedChangeListener(null);  /** for resetting previous listeners **/



        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** reverse the state of selected contacts **/
                contact.setSelected(!contact.isSelected());
                holder.checkBox.setSelected(contact.isSelected()); // set checkbox selection state according to contacts selected state
                holder.checkBox.setChecked(contact.isSelected());   // set checkbox checked state


            }
        });

        return row;
    }



}// end of class
