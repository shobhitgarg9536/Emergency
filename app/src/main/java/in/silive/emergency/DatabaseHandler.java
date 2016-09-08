package in.silive.emergency;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import in.silive.emergency.TableData.ContactsInfo;

/**
 * Created by Aniket on 07-09-2016.
 */


/** Class created to handle saving and retrieval of saved contacts. Database stores contact names and their numbers.
 *
 *
 */
public class DatabaseHandler extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;

    /** String statement of Query to create a table --- insert semicolon at the last**/
    private String CREATE_QUERY = "CREATE TABLE " + ContactsInfo.TABLE_NAME +"(" + ContactsInfo._ID + " INTEGER, "+ ContactsInfo.CONTACT_NAME +" TEXT, " + ContactsInfo.CONTACT_NUMBER + " TEXT);";


    public DatabaseHandler(Context context) {
        super(context, ContactsInfo.DATABASE_NAME, null, DATABASE_VERSION ); // Creating database
        Log.d("Database Operations", "Database created successfully");
    }


    @Override
    public String getDatabaseName() {
        return ContactsInfo.DATABASE_NAME;  /** return database name **/
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
      try {
          sqLiteDatabase.execSQL(CREATE_QUERY); // executes a sqLite statement which doesn't return any data(select)
          Log.d("Database Operation", "Table created successfully");
      }
      catch(SQLException e){
        Log.e("Database Operations", "Cannot create table, invalid query", e);
      }
      }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    /** To handle database upgrade **/

    }


    public void putContact(DatabaseHandler handler, String contactName, String contactPhone) {
        SQLiteDatabase database = handler.getWritableDatabase();       // get database
        ContentValues cv = new ContentValues();         // used to store set of values
        cv.put(ContactsInfo.CONTACT_NAME, contactName);
        cv.put(ContactsInfo.CONTACT_NUMBER, contactPhone);
        /** cv object maps values to keys. They keys will act as column names for insert() method of database **/
        long id = database.insert(ContactsInfo.TABLE_NAME, null, cv);
        if (id == -1) {
            /** error in insertion **/
            Log.e("Database Operations", "Error in inserting data into the database");
        }
    }

    /** Note: Put spaces while concatenation string in selection statement **/

        public Cursor getData(DatabaseHandler handler){
            SQLiteDatabase database = handler.getReadableDatabase();
            String[] columns = {ContactsInfo.CONTACT_NAME, ContactsInfo.CONTACT_NUMBER };
            return database.query(ContactsInfo.TABLE_NAME, columns, null, null, null, null, null);
    }

        public void deleteContact(DatabaseHandler handler, String contactName, String contactPhone){
            SQLiteDatabase database = handler.getWritableDatabase();
            String selection = ContactsInfo.CONTACT_NAME + " LIKE ? AND " + ContactsInfo.CONTACT_NUMBER + " LIKE ?";
            String[] values = {contactName, contactPhone};
            database.delete(ContactsInfo.TABLE_NAME, selection, values); // table name, selection criteria and where to be deleted
    }

    /**
     * Deletes all the contacts in the database
     */
    public void clearDatabase(DatabaseHandler handler){
        SQLiteDatabase database = handler.getWritableDatabase();
        database.delete(ContactsInfo.TABLE_NAME, null, null); // deletes all rows
    }


    public void updateDatabase(DatabaseHandler handler, String oldContactName, String oldContactPhone, String newContactName, String newContactPhone){
        SQLiteDatabase database = handler.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String selection = ContactsInfo.CONTACT_NAME + " LIKE ? AND " + ContactsInfo.CONTACT_NUMBER + " LIKE ?"; // create selection statement
        String[] values = {oldContactName, oldContactPhone};  // need values for selection statement
        cv.put(ContactsInfo.CONTACT_NAME, newContactName); // create cv object for putting new values into the database
        cv.put(ContactsInfo.CONTACT_NUMBER, newContactPhone);
        database.update(ContactsInfo.TABLE_NAME, cv, selection, values); //update database
    }

}
