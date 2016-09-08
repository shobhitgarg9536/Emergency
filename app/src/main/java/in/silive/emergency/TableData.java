package in.silive.emergency;

import android.provider.BaseColumns;

/**
 * Created by Aniket on 06-09-2016.
 */
public class TableData {

    public TableData(){}

    public static abstract class ContactsInfo implements BaseColumns{
        public static final String KEY_ID = "_id";
        public static final String CONTACT_NAME = "name";
        public static final String CONTACT_NUMBER = "number";
        public static final String DATABASE_NAME = "contacts_database";
        public static final String TABLE_NAME = "contacts_table";

    }
}
