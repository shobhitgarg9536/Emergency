package in.silive.emergency;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Aniket on 06-09-2016.
 */

/***
 * ContactAdapter converts the data to be displayed in each slot into a suitable View Object. The adapter will be used for
 * multiple displayList views. It contains a nested ViewHolder class containing the widgets used to display information.
 * 1. Store the ContactDataProvider in displayList. Use this displayList to getItem() and getCount()
 * 2. Method getView() is to return the View object which will display the required data
 *
 */
public class ContactsAdapter extends ArrayAdapter {

    /** List to store the contacts **/
    private ArrayList<Contact> displayList = new ArrayList();
    private ArrayList<Contact> originalList = new ArrayList();

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

    public int getOriginalListCount(){
        return originalList.size();
    }

    public Contact getOriginalListItem(int index){
        return originalList.get(index);
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


    /** Setter method for displayList **/
    public void setList(ArrayList<Contact> contactList){
        this.originalList = contactList;
    }


    @Override
    public void add(Object object) {
        originalList.add((Contact)object);
        displayList.add((Contact)object);     // add the object to the displayList
    }


    @Override
    public int getCount() {
        return this.displayList.size();
    }

    /**
     * Returns the item stored in the displayList of adapter
     * @param position Position of item in the displayList.
     * @return  Object stored in the displayList. The displayList is of Contact type.
     */
    @Override
    public Object getItem(int position) {
        return displayList.get(position);
    }

    /**
     * Clears the displayList of the adapter. Removes all the contacts saved in the displayList.
     */
    public void clear(){
        originalList.clear();
        displayList.clear();
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
     * The getView methods saves the data of the displayList and generates individual object for listView. The individual view contains
     * the different views like TextView, Checkbox which will be used to display data.
     * @param position  Position of view in listView, can also be used as index for displayList.
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
        final Contact contact = displayList.get(position); // get contact
        holder.nameView.setText(contact.getName());
        holder.phoneView.setText(contact.getPhoneNumber());     // save data into respective views

        /** If adapter will be used for selecting contacts then make checkbox visible else gone **/
        if(this.isSelectionAdapter)    holder.checkBox.setVisibility(CheckBox.VISIBLE);
        else                            holder.checkBox.setVisibility(CheckBox.GONE);   /** free up space on layout **/

        holder.checkBox.setSelected(contact.isSelected()); // set checkbox selection state according to contacts selected state
        holder.checkBox.setChecked(contact.isSelected());  // set checkbox checked state
        // holder.checkBox.setOnCheckedChangeListener(null);  /** for resetting previous listeners **/


        if(this.isSelectionAdapter) {
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /** reverse the state of selected contacts **/
                    contact.setSelected(!contact.isSelected());
                    holder.checkBox.setSelected(contact.isSelected()); // set checkbox selection state according to contacts selected state
                    holder.checkBox.setChecked(contact.isSelected());   // set checkbox checked state


                }
            });


            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /** reverse the state of selected contacts **/
                    contact.setSelected(!contact.isSelected());
                    holder.checkBox.setSelected(contact.isSelected()); // set checkbox selection state according to contacts selected state
                    holder.checkBox.setChecked(contact.isSelected());   // set checkbox checked state


                }
            });
        }

        return row;
    }

    /**
     * Updates the display list and filters the contacts based on the name
     * @param filterString      string used for filtering
     */
    public void filter(String filterString){
        ArrayList<Contact> filterList = new ArrayList();

        if(filterString == null || filterString.length() == 0){
            filterList = originalList;
        }
        else {
            String name = filterString.toLowerCase().trim();
            for(int index = 0; index < originalList.size(); index++){
                String contactName =  originalList.get(index).getName().toLowerCase();
                if(contactName.startsWith( name) ){
                    filterList.add(originalList.get(index));
                }
            }
        }

        displayList = filterList;
        notifyDataSetChanged();
    }


}// end of class