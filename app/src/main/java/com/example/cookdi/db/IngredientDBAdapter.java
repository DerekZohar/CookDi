package com.example.cookdi.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.example.cookdi.retrofit2.entities.Ingredient;
import com.example.cookdi.retrofit2.entities.RecipeStep;
import com.example.cookdi.retrofit2.entities.SavedRecipeStep;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class IngredientDBAdapter {
    public static final String TABLE_NAME = "ingredient";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_INGREDIENT = "ingredient";
    public static final String COLUMN_RECIPE_ID = "recipe_id";

    public static final String SCRIPT_CREATE_TABLE = new StringBuilder("CREATE TABLE ")
            .append(TABLE_NAME).append("(")
            .append(COLUMN_ID).append(" TEXT NOT NULL UNIQUE , ")
            .append(COLUMN_RECIPE_ID).append(" INTEGER ,")
            .append(COLUMN_INGREDIENT).append(" TEXT , ")
            .append(" PRIMARY KEY (" + COLUMN_RECIPE_ID + "," + COLUMN_ID + " ) ")
            .append(" )")
            .toString();

    private static DatabaseManager mDatabaseManager = DatabaseManager.getInstance();
    private static IngredientDBAdapter ingredientDBAdapterInstance;

    private IngredientDBAdapter(DatabaseManager databaseManager) {}

    public static IngredientDBAdapter getIngredientDBAdapterInstance(DatabaseManager databaseManager){
        if(ingredientDBAdapterInstance == null){
            ingredientDBAdapterInstance = new IngredientDBAdapter(databaseManager);
        }
        return ingredientDBAdapterInstance;
    }

    public static long insertIngredients(final ArrayList<Ingredient> ingredients, int recipe_id)  {
        long res = 0;
        SQLiteDatabase database = mDatabaseManager.getWritableDatabase();
        for(Ingredient ingredient : ingredients){
            if (!isIngredientExist(ingredient, recipe_id)) {final ContentValues contentValues = new ContentValues();
                contentValues.put(IngredientDBAdapter.COLUMN_ID, ingredient.getId());
                contentValues.put(IngredientDBAdapter.COLUMN_INGREDIENT, ingredient.getIngredient());
                contentValues.put(IngredientDBAdapter.COLUMN_RECIPE_ID, recipe_id);
                res = database.insert(IngredientDBAdapter.TABLE_NAME, null, contentValues);
            }
        }
        database.close();
        return res;
    }

//    public static void deleteAllRecipeStep() {
//        SQLiteDatabase database = mDatabaseManager.getWritableDatabase();
//        database.delete(TABLE_NAME, null, null);
//    }

//    public static boolean deleteById(int id){
//        SQLiteDatabase db = DatabaseManager.getInstance().getReadableDatabase();
//        return db.delete(TABLE_NAME, COLUMN_STEP_ID + "=" + id, null) > 0;
//    }

    public static boolean isIngredientExist(Ingredient ingredient, int recipe_id) {
        SQLiteDatabase db = mDatabaseManager.getReadableDatabase();
        boolean result = false;
        String strSQL = "SELECT *"+ " FROM "+ IngredientDBAdapter.TABLE_NAME + " WHERE "
                +IngredientDBAdapter.COLUMN_ID+" = "+ ingredient.getId()
                +" AND "
                +IngredientDBAdapter.COLUMN_RECIPE_ID + " = " + recipe_id;
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(strSQL,null);;
        if (cursor.moveToFirst()) {
            result = true;
        }
        //databaseManager.closeDatabase();
        return result;
    }

//    public long getRowsRecipe() {
//        SQLiteDatabase db = mDatabaseManager.getReadableDatabase();
//
//        return DatabaseUtils.queryNumEntries(db, TABLE_NAME);
//    }

    public static ArrayList<Ingredient> getAllIngredientById(int id) {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        SQLiteDatabase db = mDatabaseManager.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME +" WHERE " + COLUMN_RECIPE_ID + "=" + id, null);

        if (cursor.moveToFirst()) {
            do {
                Ingredient ingredient = new Ingredient();
                ingredient.setId(cursor.getString(cursor.getColumnIndex(IngredientDBAdapter.COLUMN_ID)));
                ingredient.setIngredient(cursor.getString(cursor.getColumnIndex(IngredientDBAdapter.COLUMN_INGREDIENT)));

                ingredients.add(ingredient);
            } while (cursor.moveToNext());

        }
        //databaseManager.closeDatabase();
        return ingredients;

    }
}





