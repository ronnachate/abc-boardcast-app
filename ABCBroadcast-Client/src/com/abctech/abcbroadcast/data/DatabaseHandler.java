package com.abctech.abcbroadcast.data;

import java.util.List;
import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	 // Database Version
    private static final int DATABASE_VERSION = 1;
  
    // Database Name
    private static final String DATABASE_NAME = "ABCBroadcastReciever";
	  // Labels table name
    private static final String TABLE_MSG = "message";
  
    // Labels Table Columns names
    private static final String KEY_ID = "id";
    private static final String MSG_DETAIL = "msgDetail";
  
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
  
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Category table create query
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_MSG + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + MSG_DETAIL + " TEXT, date DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(CREATE_CATEGORIES_TABLE);
    }
  
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MSG);
  
        // Create tables again
        onCreate(db);
    }
     
    /**
     * Inserting new lable into lables table
     * */
    public void insertMsg(String msg){
        SQLiteDatabase db = this.getWritableDatabase();
         
        ContentValues values = new ContentValues();
        values.put(MSG_DETAIL, msg);
          
        // Inserting Row
        db.insert(TABLE_MSG, null, values);
        db.close(); // Closing database connection
    }
     
    /**
     * Getting all labels
     * returns list of labels
     * */
    public List<String> getAllMsg(){
        List<String> msg = new ArrayList<String>();
         
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MSG;
      
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
      
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                msg.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
         
        // closing connection
        cursor.close();
        db.close();
         
        // returning lables
        return msg;
    }
}
