package com.example.cookdi.db;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteBlobTooBigException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.example.cookdi.helpers.TextHelper;
import com.example.cookdi.retrofit2.entities.Recipe;
import com.example.cookdi.retrofit2.entities.SavedRecipe;
import com.example.cookdi.retrofit2.entities.SavedUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class RecipeListDBAdapter {
    private static final String TAG = "RECIPE_MODEL";
    public static final String TABLE_NAME = "recipe";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_USERID = "user_id";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_IMG = "img";



    public static final String SCRIPT_CREATE_TABLE = new StringBuilder("CREATE TABLE ")
            .append(TABLE_NAME).append("(")
            .append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
            .append(COLUMN_NAME).append(" TEXT , ")
            .append(COLUMN_USERID).append(" INTEGER , ")
            .append(COLUMN_IMG).append(" BLOB , ")
            .append(COLUMN_RATING).append(" INTEGER , ")
            .append(COLUMN_TIME).append(" INTEGER")
            .append(" )")
            .toString();

    private static DatabaseManager mDatabaseManager = DatabaseManager.getInstance();
    private static RecipeListDBAdapter recipeListDBAdapterInstance;

    private RecipeListDBAdapter(DatabaseManager databaseManager) {}

    public static RecipeListDBAdapter getRecipeListAdapterInstance(DatabaseManager databaseManager){
        if(recipeListDBAdapterInstance == null){
            recipeListDBAdapterInstance = new RecipeListDBAdapter(databaseManager);
        }
        return recipeListDBAdapterInstance;
    }

    public static long insertRecipe(final Recipe recipe)  {
        if (!isRecipeExist(recipe)) {
            SQLiteDatabase database = mDatabaseManager.getWritableDatabase();
            final ContentValues contentValues = new ContentValues();
            contentValues.put(RecipeListDBAdapter.COLUMN_ID, recipe.getRecipeId());
            contentValues.put(RecipeListDBAdapter.COLUMN_NAME, recipe.getRecipeName());
            contentValues.put(RecipeListDBAdapter.COLUMN_USERID, recipe.getUserId());
            contentValues.put(RecipeListDBAdapter.COLUMN_RATING, recipe.getRating());
            contentValues.put(RecipeListDBAdapter.COLUMN_TIME, recipe.getTime());

            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    if(bitmap.getHeight() >= 1500 || bitmap.getWidth() >= 1500)
                        bitmap = bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*0.7), (int)(bitmap.getHeight()*0.7), true);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, bos);
                   contentValues.put(RecipeListDBAdapter.COLUMN_IMG, bos.toByteArray());
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            if(!TextHelper.isTextEmpty(recipe.getImageUrl()))
                Picasso.get().load(recipe.getImageUrl()).into(target);


            long res = database.insert(RecipeListDBAdapter.TABLE_NAME, null, contentValues);
            database.close();
            return res;
        }
        return 0;
    }

    public static void deleteAllRecipe() {
        SQLiteDatabase database = mDatabaseManager.getWritableDatabase();
        database.delete(TABLE_NAME, null, null);
    }

    public static boolean deleteById(int id){
        SQLiteDatabase db = DatabaseManager.getInstance().getReadableDatabase();
        return db.delete(TABLE_NAME, COLUMN_ID + "=" + id, null) > 0;
    }

    public static boolean isRecipeExist(Recipe recipe) {
        SQLiteDatabase db = mDatabaseManager.getReadableDatabase();
        boolean result = false;
        String strSQL = "SELECT "+ RecipeListDBAdapter.COLUMN_NAME+ " FROM "+ RecipeListDBAdapter.TABLE_NAME + " WHERE "+RecipeListDBAdapter.COLUMN_ID+" = "+ recipe.getRecipeId();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(strSQL,null);;
        if (cursor.moveToFirst()) {
            result = true;
        }
        //databaseManager.closeDatabase();
        return result;
    }

    public static boolean isRecipeSaved(int recipeId) {
        SQLiteDatabase db = mDatabaseManager.getReadableDatabase();
        boolean result = false;
        String strSQL = "SELECT "+ RecipeListDBAdapter.COLUMN_NAME+ " FROM "+ RecipeListDBAdapter.TABLE_NAME + " WHERE "+RecipeListDBAdapter.COLUMN_ID+" = "+ recipeId;
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(strSQL,null);;
        if (cursor.moveToFirst()) {
            result = true;
        }
        //databaseManager.closeDatabase();
        return result;
    }

    public long getRowsRecipe() {
        SQLiteDatabase db = mDatabaseManager.getReadableDatabase();

        return DatabaseUtils.queryNumEntries(db, TABLE_NAME);
    }

    public static ArrayList<SavedRecipe> getAllRecipes() {
        ArrayList<SavedRecipe> recipeModelList = new ArrayList<>();
        SQLiteDatabase db = mDatabaseManager.getReadableDatabase();

        String[] projection = {
                RecipeListDBAdapter.COLUMN_ID,
                RecipeListDBAdapter.COLUMN_NAME,
                RecipeListDBAdapter.COLUMN_USERID,
                RecipeListDBAdapter.COLUMN_IMG,
                RecipeListDBAdapter.COLUMN_RATING,
                RecipeListDBAdapter.COLUMN_TIME
        };
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                SavedRecipe recipe = new SavedRecipe();
                recipe.setRecipeId(cursor.getInt(cursor.getColumnIndex(RecipeListDBAdapter.COLUMN_ID)));
                recipe.setRecipeName(cursor.getString(cursor.getColumnIndex(RecipeListDBAdapter.COLUMN_NAME)));
                recipe.setUserId(cursor.getInt(cursor.getColumnIndex(RecipeListDBAdapter.COLUMN_USERID)));
                recipe.setRating(cursor.getInt(cursor.getColumnIndex(RecipeListDBAdapter.COLUMN_RATING)));
                recipe.setTime(cursor.getInt(cursor.getColumnIndex(RecipeListDBAdapter.COLUMN_TIME)));
                recipe.setImage(cursor.getBlob(cursor.getColumnIndex(RecipeListDBAdapter.COLUMN_IMG)));

                recipeModelList.add(recipe);
            } while (cursor.moveToNext());
        }
        //databaseManager.closeDatabase();
        return recipeModelList;
    }

    public static SavedRecipe getRecipeById(int id){
        SQLiteDatabase db = DatabaseManager.getInstance().getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME +" WHERE " + COLUMN_ID + "=" + id, null);

        if (cursor != null) {
            cursor.moveToFirst();
            SavedRecipe savedRecipe = new SavedRecipe();
            savedRecipe.setRecipeId(cursor.getInt(cursor.getColumnIndex(RecipeListDBAdapter.COLUMN_ID)));
            savedRecipe.setRecipeName(cursor.getString(cursor.getColumnIndex(RecipeListDBAdapter.COLUMN_NAME)));
            savedRecipe.setUserId(cursor.getInt(cursor.getColumnIndex(RecipeListDBAdapter.COLUMN_USERID)));
            savedRecipe.setRating(cursor.getInt(cursor.getColumnIndex(RecipeListDBAdapter.COLUMN_RATING)));
            savedRecipe.setTime(cursor.getInt(cursor.getColumnIndex(RecipeListDBAdapter.COLUMN_TIME)));
            savedRecipe.setImage(cursor.getBlob(cursor.getColumnIndex(RecipeListDBAdapter.COLUMN_IMG)));
            return savedRecipe;
        }
        return null;
    }
}



