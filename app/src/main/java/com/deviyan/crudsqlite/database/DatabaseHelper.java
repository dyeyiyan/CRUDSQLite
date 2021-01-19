package com.deviyan.crudsqlite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;
;
import com.deviyan.crudsqlite.models.ProductModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Constants.CREATE_TABLE);

        Log.d("Table is created: " , Constants.CREATE_TABLE);
        Log.d("DB", "DB is created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        //drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);

        //create tables again
        onCreate(sqLiteDatabase);
    }

    //insert product
    public long insertProduct(String name, String unit, String price, String dataOfExpiry, String availableInventory, String availableInventoryCost, String imagePath) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.C_NAME, name);
        values.put(Constants.C_UNIT, unit);
        values.put(Constants.C_PRICE, price);
        values.put(Constants.C_DATE_OF_EXPIRE, dataOfExpiry);
        values.put(Constants.C_AVAIL_INV, availableInventory);
        values.put(Constants.C_AVAIL_INV_COST, availableInventoryCost);
        values.put(Constants.C_IMAGE, imagePath);

        long id = db.insert(Constants.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    //update product
    public void updateProduct(String id, String name, String unit, String price, String dataOfExpiry, String availableInventory, String availableInventoryCost, String imagePath) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.C_NAME, name);
        values.put(Constants.C_UNIT, unit);
        values.put(Constants.C_PRICE, price);
        values.put(Constants.C_DATE_OF_EXPIRE, dataOfExpiry);
        values.put(Constants.C_AVAIL_INV, availableInventory);
        values.put(Constants.C_AVAIL_INV_COST, availableInventoryCost);
        values.put(Constants.C_IMAGE, imagePath);

        db.update(Constants.TABLE_NAME, values, Constants.C_ID + " = ?", new String[]{id});
        db.close();
    }

    //delete product
    public void deleteProduct(String id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.C_ID + " = ? ", new String[]{id});
        db.close();
    }



    public List<ProductModel> getAll(String orderBy) {

        List<ProductModel> productModels = new ArrayList<>();

        //get all product
        String query = "SELECT * FROM " + Constants.TABLE_NAME + " ORDER BY " + orderBy;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToNext()) {

            do {
                ProductModel model = new ProductModel(
                        "" + cursor.getInt(cursor.getColumnIndex(Constants.C_ID)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_NAME)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_UNIT)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_PRICE)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_DATE_OF_EXPIRE)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_AVAIL_INV)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_AVAIL_INV_COST)),
                        "" + cursor.getString( cursor.getColumnIndex(Constants.C_IMAGE))
                );
                productModels.add(model);
            } while (cursor.moveToNext());

        }

        db.close();
        return productModels;

    }

}
