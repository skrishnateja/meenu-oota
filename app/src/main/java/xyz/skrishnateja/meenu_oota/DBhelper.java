package xyz.skrishnateja.meenu_oota;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String CONTACTS_TABLE_NAME = "meenuoota";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_DETAILS = "details";
    public static final String CONTACTS_COLUMN_QUANTITY = "quantity";
    public static final String CONTACTS_COLUMN_MARKATNAME = "marketname";
    public static final String CONTACTS_COLUMN_VENDORNAME = "vendorname";
    public static final String CONTACTS_COLUMN_PRICE = "price";
    private HashMap hp;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        try {
            db.execSQL(
                    "create table fav " +
                            "(id text)"
            );
        }
        catch (Exception e){
            Log.i("FAV TABLE",e.toString());
        }
        ContentValues contentValue = new ContentValues();

        contentValue.put("id", 1);
        db.insert("fav", null, contentValue);

        db.execSQL(
                "create table meenuoota " +
                        "(id integer, name text,details text,quantity text, marketname text,vendorname text,price text)"
        );

        ContentValues contentValues = new ContentValues();
        //SQLiteDatabase db = this.getWritableDatabase();
        contentValues.put("id", 1);
        contentValues.put("name", "Please");
        contentValues.put("details", "");
        contentValues.put("quantity", "");
        contentValues.put("marketname", "");
        contentValues.put("vendorname", "");
        contentValues.put("price", "");
        db.insert("meenuoota", null, contentValues);
        contentValues.put("id", 2);
        contentValues.put("name", "Wait");
        contentValues.put("details", "");
        contentValues.put("quantity", "");
        contentValues.put("marketname", "");
        contentValues.put("vendorname", "");
        contentValues.put("price", "");
        db.insert("meenuoota", null, contentValues);
        contentValues.put("id", 3);
        db.insert("meenuoota", null, contentValues);
        contentValues.put("id", 4);
        db.insert("meenuoota", null, contentValues);
        contentValues.put("id", 5);
        db.insert("meenuoota", null, contentValues);
        contentValues.put("id", 6);
        db.insert("meenuoota", null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS meenuoota");
        db.execSQL("DROP TABLE IF EXISTS fav");
        onCreate(db);
    }

    public boolean insertFish  (String id,String name, String details, String quantity, String marketname,String vendorname,String price)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("details", details);
        contentValues.put("quantity", quantity);
        contentValues.put("marketname", marketname);
        contentValues.put("vendorname", vendorname);
        contentValues.put("price", price);
        db.insert("meenuoota", null, contentValues);
        Log.i(":::::::::::::::::::", "DB");
        //Toast.makeText(getApplicationContext(), jo.getString("quantity"), Toast.LENGTH_SHORT).show();
        return true;
    }

    public Cursor getData(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from meenuoota where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateFish (String id, String name, String details, String quantity, String marketname,String vendorname,
                               String price)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("details", details);
        contentValues.put("quantity", quantity);
        contentValues.put("marketname", marketname);
        contentValues.put("vendorname", vendorname);
        contentValues.put("price", price);
        db.update("meenuoota", contentValues, "id = ? ", new String[]{id});
        return true;
    }

    public Integer deleteFish (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("meenuoota",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }
    public Integer deleteFav (String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("fav",
                "id = ? ",
                new String[] { id });
    }
    public Integer insertFav (String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        db.insert("fav",null, contentValues);
        return 1;
    }
    public int getFav (String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery("select * from fav where id=" + id + "",null);

        return res.getCount();
    }

    public ArrayList<String> getAllFish()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from meenuoota", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
           // array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_DETAILS)));
           // array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_MARKATNAME)));
           // array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_VENDORNAME)));
           // array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_QUANTITY)));
            res.moveToNext();
        }
        return array_list;
    }
    public ArrayList<String> getPriceFish()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from meenuoota", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            //array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            // array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_DETAILS)));
            // array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_MARKATNAME)));
            // array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_VENDORNAME)));
             array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_PRICE)));
            res.moveToNext();
        }
        return array_list;
    }
    public ArrayList<String> getoneFish(String id)
    {
        ArrayList<String> array_list = new ArrayList<String>();

        Log.i("-----------FISH",id);
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from meenuoota where id="+id, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_DETAILS)));
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_MARKATNAME)));
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_VENDORNAME)));
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_QUANTITY)));
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_PRICE)));
            res.moveToNext();
        }
        return array_list;
    }

}