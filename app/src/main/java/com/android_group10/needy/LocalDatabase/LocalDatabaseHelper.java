package com.android_group10.needy.LocalDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LocalDatabaseHelper {
    public static long newImage;
    public static final String IMAGE_ID = "id";
    public static final String IMAGE = "image";
    public static final String USER_ID = "user_id";
    private final Context mContext;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    private static final String DATABASE_NAME = "Images.db";
    private static final int DATABASE_VERSION = 1;

    private static final String IMAGES_TABLE = "ImagesTable";

    private static final String CREATE_IMAGES_TABLE =
            "CREATE TABLE IF NOT EXISTS " + IMAGES_TABLE + " ("
                    + USER_ID + " TEXT, "
                    + IMAGE + " BLOB NOT NULL );";


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_IMAGES_TABLE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //db.execSQL("DROP TABLE IF EXISTS " + CREATE_IMAGES_TABLE);
            onCreate(db);
        }
    }


    public LocalDatabaseHelper(Context ctx) {
        mContext = ctx;
        databaseHelper = new DatabaseHelper(mContext);
    }

    public LocalDatabaseHelper open() throws SQLException {
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        databaseHelper.close();
    }

    // Insert the image to the Sqlite DB
    public void insertImage(byte[] imageBytes, String userId) {
        ContentValues cv = new ContentValues();
        cv.put(USER_ID, userId);
        cv.put(IMAGE, imageBytes);
        Log.i("LocalDatabaseHelper", "Inserted image");
        // It will check if the user has already an image so will update it otherwise it will insert  a new one.
        int id = (int) sqLiteDatabase.insertWithOnConflict(IMAGES_TABLE, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
            sqLiteDatabase.update(IMAGES_TABLE, cv, "user_id=?", new String[]{userId});  // number 1 is the _id here, update to variable for your code
        }
        newImage = sqLiteDatabase.insert(IMAGES_TABLE, null, cv);
    }

    // Get the image from SQLite DB
    // We will just get the last image we just saved for convenience...
    public byte[] retreiveImageFromDB(String userId) {

        Cursor cur = sqLiteDatabase.query(IMAGES_TABLE, new String[]{USER_ID, IMAGE}, USER_ID + "=?",
                new String[]{String.valueOf(userId)}, null, null, null, null);

        if (cur.moveToFirst()) {
            byte[] blob = cur.getBlob(cur.getColumnIndex(IMAGE));
            cur.close();
            return blob;
        }
        cur.close();
        return null;

    }

    // Delete an user image with a specific userId
    public void deleteImage (String userId){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        sqLiteDatabase.delete(
                IMAGES_TABLE,  // Where to delete
                USER_ID+" = ?",
                new String[]{userId});  // What to delete
        sqLiteDatabase.close();
    }


    public boolean checkTableEmpty() {
        boolean flag;

        try {
            sqLiteDatabase = databaseHelper.getWritableDatabase();
        } catch (SQLException ex) {
            sqLiteDatabase = databaseHelper.getReadableDatabase();
        }
        String count = "SELECT * FROM ImagesTable";
        Cursor cursor = sqLiteDatabase.rawQuery(count, null);
        if (cursor.moveToFirst()) {
            flag = false;
        } else {
            flag = true;
        }
        cursor.close();
        sqLiteDatabase.close();

        return flag;
    }
}

