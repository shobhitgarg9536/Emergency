package in.silive.emergency;

/**
 * Created by Aniket on 06-09-2016.
 */

/** A class defined to create Contact Objects. The objects will be used by ContactsAdapter to display the contacts **/
public class ContactsDataProvider {

    private String name;
    private String phoneNumber;

    public ContactsDataProvider(String name, String phoneNumber){
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

}
