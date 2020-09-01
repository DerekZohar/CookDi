package com.example.cookdi.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.example.cookdi.helpers.TextHelper;
import com.example.cookdi.retrofit2.entities.RecipeStep;
import com.example.cookdi.retrofit2.entities.SavedRecipeStep;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class RecipeStepDBAdapter {
    public static final String TABLE_NAME = "recipe_step";

    public static final String COLUMN_RECIPE_ID = "recipe_id";
    public static final String COLUMN_STEP_ID = "step_id";
    public static final String COLUMN_DESC = "desc";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_STEP_IMG = "step_img";
    public static final String COLUMN_ORDER = "step_order";

    public static final String SCRIPT_CREATE_TABLE = new StringBuilder("CREATE TABLE ")
            .append(TABLE_NAME).append("(")
            .append(COLUMN_STEP_ID).append(" INTEGER , ")
            .append(COLUMN_RECIPE_ID).append(" INTEGER , ")
            .append(COLUMN_DESC).append(" TEXT , ")
            .append(COLUMN_DURATION).append(" INTEGER , ")
            .append(COLUMN_STEP_IMG).append(" BLOB , ")
            .append(COLUMN_ORDER).append(" INTEGER , ")
            .append(" PRIMARY KEY (" + COLUMN_RECIPE_ID + "," + COLUMN_STEP_ID + " ) ")
            .append(" )")
            .toString();

    private static DatabaseManager mDatabaseManager = DatabaseManager.getInstance();
    private static RecipeStepDBAdapter recipeStepDBAdapterInstance;

    private RecipeStepDBAdapter(DatabaseManager databaseManager) {}

    public static RecipeStepDBAdapter getRecipeStepDBAdapterInstance(DatabaseManager databaseManager){
        if(recipeStepDBAdapterInstance == null){
            recipeStepDBAdapterInstance = new RecipeStepDBAdapter(databaseManager);
        }
        return recipeStepDBAdapterInstance;
    }

    public static long insertRecipeSteps(final ArrayList<RecipeStep> recipeSteps)  {
        long res = 0;
        SQLiteDatabase database = mDatabaseManager.getWritableDatabase();

        for(RecipeStep recipeStep : recipeSteps){
            if (!isRecipeStepExist(recipeStep)) {

                final ContentValues contentValues = new ContentValues();
                contentValues.put(RecipeStepDBAdapter.COLUMN_STEP_ID, recipeStep.getStep_id());
                contentValues.put(RecipeStepDBAdapter.COLUMN_RECIPE_ID, recipeStep.getRecipe_id());
                contentValues.put(RecipeStepDBAdapter.COLUMN_DESC, recipeStep.getStep_description());
                contentValues.put(RecipeStepDBAdapter.COLUMN_DURATION, recipeStep.getDuration_minute());
                contentValues.put(RecipeStepDBAdapter.COLUMN_ORDER, recipeStep.getStep_Order());

                Target target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                        contentValues.put(RecipeStepDBAdapter.COLUMN_STEP_IMG, bos.toByteArray());
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                };
                if(!TextHelper.isTextEmpty(recipeStep.getStep_image_url()))
                    Picasso.get().load(recipeStep.getStep_image_url()).into(target);

                res = database.insert(RecipeStepDBAdapter.TABLE_NAME, null, contentValues);
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

    public static boolean isRecipeStepExist(RecipeStep recipeStep) {
        SQLiteDatabase db = mDatabaseManager.getReadableDatabase();
        boolean result = false;
        String strSQL = "SELECT *"+ " FROM "+ RecipeStepDBAdapter.TABLE_NAME + " WHERE "
                +RecipeStepDBAdapter.COLUMN_STEP_ID+" = "+ recipeStep.getStep_id()
                +" AND "
                +RecipeStepDBAdapter.COLUMN_RECIPE_ID+" = "+ recipeStep.getRecipe_id();
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

    public static ArrayList<SavedRecipeStep> getAllRecipeStepById(int id) {
        ArrayList<SavedRecipeStep> recipeSteps = new ArrayList<>();
        SQLiteDatabase db = mDatabaseManager.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME +" WHERE " + COLUMN_RECIPE_ID + "=" + id, null);
        if (cursor.moveToFirst()) {
            do {
                SavedRecipeStep recipeStep = new SavedRecipeStep();
                recipeStep.setRecipe_id(cursor.getInt(cursor.getColumnIndex(RecipeStepDBAdapter.COLUMN_RECIPE_ID)));
                recipeStep.setStep_id(cursor.getInt(cursor.getColumnIndex(RecipeStepDBAdapter.COLUMN_STEP_ID)));
                recipeStep.setStep_description(cursor.getString(cursor.getColumnIndex(RecipeStepDBAdapter.COLUMN_DESC)));
                recipeStep.setDuration_minute(cursor.getInt(cursor.getColumnIndex(RecipeStepDBAdapter.COLUMN_DURATION)));
                recipeStep.setStep_Order(cursor.getInt(cursor.getColumnIndex(RecipeStepDBAdapter.COLUMN_ORDER)));
                recipeStep.setImgStep(cursor.getBlob(cursor.getColumnIndex(RecipeStepDBAdapter.COLUMN_STEP_IMG)));

                recipeSteps.add(recipeStep);
            } while (cursor.moveToNext());

        }
        //databaseManager.closeDatabase();
        return recipeSteps;

    }
}




