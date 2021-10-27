package com.example.lab04;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This is a specialized form of the SQLiteOpenHelper which
 * will enable us to read from and write to a database in SQLite
 */
public class MyDBHandler extends SQLiteOpenHelper {

    // Defining the schema (database name, table names, column names)
    private static final int DATABASE_VERSION  = 1;
    private static final String DATABASE_NAME = "products.db";
    private static final String TABLE_PRODUCTS = "products";
    // We must use _id instead of id or any other name or else some parts
    // of the database won't work
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_PRODUCT_NAME = "name";
    private static final String COLUMN_PRICE = "price";

    /**
     * Constructor for the database handler
     * @param context is the context of the application.
     *                When in an activity, it's usually "this"
     *                When in a fragment, it's usually "getActivity()"
     */
    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called by the super constructor the first time that the
     * database is created and never again.
     * If there are any values to prepopulate in the database, they will
     * go here as well.
     * @param db is the SQLiteDatabase with the DATABASE_NAME given
     *           in the constructor
     */
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE "+TABLE_PRODUCTS+" ("+
                COLUMN_ID+" INTEGER PRIMARY KEY, "+
                COLUMN_PRODUCT_NAME+" TEXT, "+
                COLUMN_PRICE+" DOUBLE)");
    }

    /**
     * If a major update happens that reworks the database, this will destroy
     * the old database and recreate it
     * @param db is the SQLiteDatabase with the name DATABASE_NAME
     * @param oldVersion is the old version number saved in the database
     * @param newVersion is the version number of the new database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_PRODUCTS);
        onCreate(db);
    }

    /**
     * Adds a new product to the database
     * @param product is the product object to be saved
     */
    public void addProduct(Product product){
        // Opens a database to write to
        // "this" means to open the database with DATABASE_NAME
        SQLiteDatabase db = this.getWritableDatabase();
        // This will hold the values to put in the database
        ContentValues values = new ContentValues();
        // Each put statement maps the column with the value to add
        values.put(COLUMN_PRODUCT_NAME, product.getProductName());
        values.put(COLUMN_PRICE, product.getPrice());
        // This inserts the values into the table
        db.insert(TABLE_PRODUCTS, null, values);
        // Finally, we close the database
        db.close();
    }

    /**
     * Finds a product in the database
     * @param productName is the name of the product to search for
     * @return the Product object or null
     */
    public Product findProduct(String productName){
        // Opens the database, but we only need to read
        SQLiteDatabase db = this.getReadableDatabase();
        // Starts a cursor to search the database
        // A cursor searches one line at a time given a rawQuery command
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_PRODUCTS+
                " WHERE "+COLUMN_PRODUCT_NAME+
                " = \""+productName+"\"", null);
        // Make the product object
        Product product = new Product();
        // If there is a first to move to, it means there is a product of
        // that name in the database
        if ( cursor.moveToFirst() ){
            // Sets all the data in the Product object by getting it
            // from the corresponding columns (0, 1, 2)
            product.setID(Integer.parseInt(cursor.getString(0)));
            product.setProductName(cursor.getString(1));
            product.setPrice(Double.parseDouble(cursor.getString(2)));
            // Close the cursor
            cursor.close();
        } else {
            // If there is no first, then the product is not in the database
            // The product will be null
            product = null;
        }
        // Close the database
        db.close();
        return product;
    }

    /**
     * Deletes a product from the database if possible
     * @param productName is the name of the product to delete
     * @return true if the product was deleted or false if it
     *         could not be deleted
     */
    public boolean deleteProduct(String productName){
        // Initialize the result to a failure
        boolean result = false;
        // Since we are changing the database, we open a writable version
        SQLiteDatabase db = this.getWritableDatabase();
        // Search for the item to be deleted
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_PRODUCTS+
                " WHERE "+COLUMN_PRODUCT_NAME+
                " = \""+productName+"\"", null);
        // If there is at least one item that the cursor found....
        if (cursor.moveToFirst()){
            // Get the unique id of the item
            String idStr = cursor.getString(0);
            // Delete the item with that unique id
            db.delete(TABLE_PRODUCTS, COLUMN_ID+" = "+idStr, null);
            // Close the cursor
            cursor.close();
            // Declare the deletion a success
            result = true;
        }
        // Close the database
        db.close();
        // Return the success / failure
        return result;
    }

    /**
     * Reads all the data from a database
     * @return a cursor that can scan all lines of the database
     */
    public Cursor viewData(){
        // Since we are just reading and not changing it, we get a readable database
        SQLiteDatabase db = this.getReadableDatabase();
        // Returns the rawQuery (a cursor) which selects all (* = all) from the
        // table with no "where" conditions
        return db.rawQuery("SELECT * FROM "+TABLE_PRODUCTS, null);
    }

}
