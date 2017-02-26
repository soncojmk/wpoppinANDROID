package com.wpoppin.whatspoppin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Abby on 2/25/2017.
 */
public class SQLSaveEvents extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "BookDB";

    // Books table name
    private static final String TABLE_BOOKS = "books";

    // Books Table Columns names
    private static final String KEY_SECTION = "section";
    private static final String KEY_JSON = "words";

    private static final String[] COLUMNS = {KEY_SECTION,KEY_JSON};

    public SQLSaveEvents(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_BOOK_TABLE = "CREATE TABLE books ( " +
                "section TEXT, " +
                "words TEXT)";

        // create books table
        db.execSQL(CREATE_BOOK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS books");

        // create fresh books table
        this.onCreate(db);
    }

    public void addRequest(String section, String json){
        //for logging
        Log.d("addRequest " + section, json);

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_SECTION, section); // get title
        values.put(KEY_JSON, json); // get author

        // 3. insert
        db.insert(TABLE_BOOKS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public String getRequest(String section){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_BOOKS, // a. table
                        COLUMNS, // b. column names
                        " section = ?", // c. selections
                        new String[] { String.valueOf(section) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        try {
            // 4. build book object
            String json;
            json = (cursor.getString(1));

            //log
            Log.d("getBook(" + section + ")", json);

            // 5. return book
            return json;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public void deleteRequest(String section) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        try {
            db.delete(TABLE_BOOKS, //table name
                    KEY_SECTION + " = ?",  // selections
                    new String[]{section}); //selections args
        }
        catch (Exception e) {
            return;
        }

        // 3. close
        db.close();

        //log
        Log.d("deleteBook",section);

    }

}