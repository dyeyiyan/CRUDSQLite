package com.deviyan.crudsqlite.database;

public class Constants {

    //db name
    public static final String DB_NAME = "PRODUCT_DB";
    // db version
    public static final int DB_VERSION = 1;
    // db table
    public static final String TABLE_NAME = "PRODUCT_TABLE";

    //table column
    public static final String C_ID = "ID";
    public static final String C_NAME = "PRODUCT_NAME";
    public static final String C_UNIT = "UNIT";
    public static final String C_PRICE = "PRICE";
    public static final String C_DATE_OF_EXPIRE = "DATE_EXPIRE";
    public static final String C_AVAIL_INV = "AVAIL_INVENTORY";
    public static final String C_AVAIL_INV_COST = "AVAIL_INVENTORY_COST";
    public static final String C_IMAGE = "PRODUCT_IMAGE";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + C_NAME + " TEXT,"
            + C_UNIT + " TEXT,"
            + C_PRICE + " TEXT,"
            + C_DATE_OF_EXPIRE + " TEXT,"
            + C_AVAIL_INV + " TEXT,"
            + C_AVAIL_INV_COST + " TEXT,"
            + C_IMAGE + " TEXT4"
            + ");";


}
