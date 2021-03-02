package com.android_group10.needy.LocalDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalDatabaseHelper {
    public static final String IMAGE_ID = "id";
    public static final String IMAGE = "image";
    private final Context mContext;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    private static final String DATABASE_NAME = "Images.db";
    private static final int DATABASE_VERSION = 1;

    private static final String IMAGES_TABLE = "ImagesTable";

    private static final String CREATE_IMAGES_TABLE =
            "CREATE TABLE IF NOT EXISTS " + IMAGES_TABLE + " (" +
                    IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
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
    public void insertImage(byte[] imageBytes) {
        ContentValues cv = new ContentValues();
        cv.put(IMAGE, imageBytes);
        sqLiteDatabase.insert(IMAGES_TABLE, null, cv);
    }

    // Get the image from SQLite DB
    // We will just get the last image we just saved for convenience...
    public byte[] retreiveImageFromDB() {
        Cursor cur = sqLiteDatabase.query(false, IMAGES_TABLE, new String[]{IMAGE_ID, IMAGE},
                null, null, null, null,
                IMAGE_ID + " DESC", "1");
        if (cur.moveToFirst()) {
            byte[] blob = cur.getBlob(cur.getColumnIndex(IMAGE));
            cur.close();
            return blob;
        }
        cur.close();
        return null;
    }

    public boolean deleteImage() {
        try {
            int result = sqLiteDatabase.delete(IMAGES_TABLE, IMAGE + " = image", null);
            if (result > 0) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
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

