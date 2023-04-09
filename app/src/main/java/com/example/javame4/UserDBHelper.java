package com.example.javame4;

import android.annotation.SuppressLint;
import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.widget.Toast;

import java.util.ArrayList;

public class UserDBHelper extends SQLiteOpenHelper {
    private Context context;
    private OnDatabaseChangeListener onDatabaseChangeListener = null;
    private static final String DATABASE_NAME = "users.db";
    private static final int SCHEMA = 1;
    private static final String TABLE = "user_table";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_SURNAME = "surname";
    private static final String COLUMN_PHONE = "phone_number";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_PHOTO = "photo";

    public UserDBHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME
                + " TEXT, " + COLUMN_SURNAME + " TEXT, " + COLUMN_PHONE + " TEXT, "
                + COLUMN_EMAIL + " TEXT, " + COLUMN_ADDRESS + " TEXT, "
                + COLUMN_PHOTO + " BLOB);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);
    }

    public void insertData(String name, String surname, String phone, String email,
                           String address, byte[] photo) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME,name);
        values.put(COLUMN_SURNAME,surname);
        values.put(COLUMN_PHONE,phone);
        values.put(COLUMN_EMAIL,email);
        values.put(COLUMN_ADDRESS,address);
        values.put(COLUMN_PHOTO,photo);

        SQLiteDatabase db = this.getWritableDatabase();
        long res = db.insert(TABLE,null,values);
        if (res != -1) {
            Toast.makeText(context,"Insertion successful.",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"Insertion failed.",Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    @SuppressLint("Range")
    public UserInfo getUserInfoById (int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE + " WHERE " + COLUMN_ID + " = ?;",
                new String[] {Integer.toString(id)});

        if (cursor.moveToFirst()) {
            UserInfo user = new UserInfo(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_SURNAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS)),
                    cursor.getBlob(cursor.getColumnIndex(COLUMN_PHOTO))
            );
            cursor.close();
            db.close();
            return user;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }

    @SuppressLint("Range")
    public ArrayList<UserInfo> getAllUsers (String sortOption) {
        String sortOrderBy = switch(sortOption) {
            case "Name" -> COLUMN_NAME;
            case "Surname" -> COLUMN_SURNAME;
            case "Email" -> COLUMN_EMAIL;
            default -> COLUMN_ID;
        };
        ArrayList<UserInfo> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE + " ORDER BY " + sortOrderBy + ";",
                null);
        if (cursor.moveToFirst()) {
            do {
                users.add(new UserInfo(
                        cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_SURNAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS)),
                        cursor.getBlob(cursor.getColumnIndex(COLUMN_PHOTO)))
                );
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return users;
    }

    public void cleanTable () {
        SQLiteDatabase db = this.getWritableDatabase();
        long res = db.delete(TABLE,null,null);
        if (res != -1) {
            Toast.makeText(context,"Deletion successful.",Toast.LENGTH_SHORT).show();
            db.delete("sqlite_sequence","name = ?", new String[]{TABLE});
        } else {
            Toast.makeText(context,"Deletion failed.",Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    public void removeUser (int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long res = db.delete(TABLE,COLUMN_ID + " = ?",
                new String[] {Integer.toString(id)});
        if (res != -1) {
            Toast.makeText(context,"Deletion of user successful.",Toast.LENGTH_SHORT).show();
            ContentValues values = new ContentValues();
            values.put("seq",0);
            db.update("sqlite_sequence",values,"name = ?", new String[]{TABLE});
            SQLiteDatabase db1 = this.getReadableDatabase();
            Cursor cursor = db1.rawQuery("SELECT * FROM " + TABLE
                    + " ORDER BY " + COLUMN_ID + ";", null);
            int item_id = 1;
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") int temp_id = cursor.getInt(
                            cursor.getColumnIndex(COLUMN_ID));
                    values = new ContentValues();
                    values.put(COLUMN_ID,item_id);
                    db1.update(TABLE,values,COLUMN_ID + " = ?",
                            new String[] {Integer.toString(temp_id)});
                    item_id++;
                } while (cursor.moveToNext());
            }
            cursor.close();
            db1.close();
        } else {
            Toast.makeText(context,"Deletion of user failed.",Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    public void updateUser (int id, String name, String surname, String phone, String email,
                            String address, byte[] photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME,name);
        values.put(COLUMN_SURNAME,surname);
        values.put(COLUMN_PHONE,phone);
        values.put(COLUMN_EMAIL,email);
        values.put(COLUMN_ADDRESS,address);
        values.put(COLUMN_PHOTO,photo);

        long res = db.update(TABLE, values,COLUMN_ID + " = ?",
                new String[] {Integer.toString(id)});
        if (res != -1) {
            Toast.makeText(context,"Updating successful.",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"Updating failed.",Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    void setOnDatabaseChangeListener(OnDatabaseChangeListener listener) {
        onDatabaseChangeListener = listener;
    }

    private void notifyDatabaseChanged() {
        onDatabaseChangeListener.onDatabaseChanged();
    }

    interface OnDatabaseChangeListener {
        void onDatabaseChanged();
    }
}