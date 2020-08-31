package com.example.cookdi.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.cookdi.retrofit2.entities.SavedUser;
import com.example.cookdi.retrofit2.entities.User;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class UserListDBAdapter {
    private static final String TAG = "CHEF_MODEL";
    public static final String TABLE_NAME = "user";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_IMG = "img";



    public static final String SCRIPT_CREATE_TABLE = new StringBuilder("CREATE TABLE ")
            .append(TABLE_NAME).append("(")
            .append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
            .append(COLUMN_NAME).append(" TEXT , ")
            .append(COLUMN_IMG).append(" BLOB")
            .append(" )")
            .toString();

    private static DatabaseManager mDatabaseManager = DatabaseManager.getInstance();
    private static UserListDBAdapter userListDBAdapterInstance;

    private UserListDBAdapter(DatabaseManager databaseManager) {}

    public static UserListDBAdapter getUserListAdapterInstance(DatabaseManager databaseManager){
        if(userListDBAdapterInstance == null){
            userListDBAdapterInstance = new UserListDBAdapter(databaseManager);
        }
        return userListDBAdapterInstance;
    }

    public static long insertUser(User user) {

        if (!isUserExist(user)) {
            SQLiteDatabase database = mDatabaseManager.getWritableDatabase();

            final ContentValues contentValues = new ContentValues();
            contentValues.put(UserListDBAdapter.COLUMN_ID, user.getId());
            contentValues.put(UserListDBAdapter.COLUMN_NAME, user.getName());

            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                    contentValues.put(UserListDBAdapter.COLUMN_IMG, bos.toByteArray());
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            Picasso.get().load(user.getAvatar()).into(target);

            long res = database.insert(UserListDBAdapter.TABLE_NAME, null, contentValues);
            database.close();
            return res;
        }
        return 0;
    }

    public static void deleteAllUser() {
        SQLiteDatabase database = mDatabaseManager.getWritableDatabase();
        database.delete(TABLE_NAME, null, null);
    }

    public static boolean isUserExist(User user) {
        SQLiteDatabase db = mDatabaseManager.getReadableDatabase();
        boolean result = false;
        String strSQL = "SELECT "+ UserListDBAdapter.COLUMN_NAME+ " FROM "+ UserListDBAdapter.TABLE_NAME + " WHERE "+ UserListDBAdapter.COLUMN_ID+" = "+ user.getId();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(strSQL,null);;
        if (cursor.moveToFirst()) {
            result = true;
        }
        //databaseManager.closeDatabase();
        return result;
    }

    public long getRowsUser() {
        SQLiteDatabase db = mDatabaseManager.getReadableDatabase();

        return DatabaseUtils.queryNumEntries(db, TABLE_NAME);
    }

    public static SavedUser getUserById(int id){
        SQLiteDatabase db = DatabaseManager.getInstance().getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME +" WHERE " + COLUMN_ID + "=" + id, null);
        if (cursor != null) {
            cursor.moveToFirst();
            SavedUser user = new SavedUser();
            user.setId(cursor.getInt(cursor.getColumnIndex(UserListDBAdapter.COLUMN_ID)));
            user.setName(cursor.getString(cursor.getColumnIndex(UserListDBAdapter.COLUMN_NAME)));
            user.setImage(cursor.getBlob(cursor.getColumnIndex(UserListDBAdapter.COLUMN_IMG)));
            return user;
        }
        return null;
    }

    public ArrayList<SavedUser> getAllUsers() {
        ArrayList<SavedUser> userModelList = new ArrayList<>();
        SQLiteDatabase db = mDatabaseManager.getReadableDatabase();

        String[] projection = {
                UserListDBAdapter.COLUMN_ID,
                UserListDBAdapter.COLUMN_NAME,
                UserListDBAdapter.COLUMN_IMG
        };
        Cursor cursor = db.query(TABLE_NAME, projection, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                SavedUser user = new SavedUser();
                user.setId(cursor.getInt(cursor.getColumnIndex(UserListDBAdapter.COLUMN_ID)));
                user.setName(cursor.getString(cursor.getColumnIndex(UserListDBAdapter.TABLE_NAME)));
                user.setImage(cursor.getBlob(cursor.getColumnIndex(UserListDBAdapter.COLUMN_IMG)));
                userModelList.add(user);
            } while (cursor.moveToNext());

        }
        //databaseManager.closeDatabase();
        return userModelList;
    }
}



