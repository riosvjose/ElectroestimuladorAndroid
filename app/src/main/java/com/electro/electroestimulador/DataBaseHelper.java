package com.electro.electroestimulador;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "person.db";
    public static final String TABLE_NAME = "person";
    //.....
    public static final String COL1 = "id_persona";
    public static final String COL2 = "firstName";
    public static final String COL3 = "lastName";
    public static final String COL4 = "userid";
    public static final String COL5 = "userac";
    //public static final String COL6 = "email";
    public static final String COL7 = "active";






    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("+COL1+" text," +
                COL2+" text,"+COL3+" text,"+COL4+" text,"+COL5+" text,"+COL7+" text ) ");// +
                //"CREATE TABLE "+ TABLENAME+"( "+Col1+" INTEGER PRIMARY KEY AUTOINCREMENT,"+Col2+" text,"+Col3+" text,"+Col4+" text)");
        //db.execSQL("CREATE TABLE "+ TABLENAME+" ("+Col1+" INTEGER PRIMARY KEY AUTOINCREMENT,"+Col2+" text,"+Col3+" text,"+Col4+" text ) ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        //db.execSQL("DROP TABLE IF EXISTS "+ TABLENAME);
        onCreate(db);
    }



    public boolean insertData(int i, String id, String FirstName, String LastName, String User,
                              String State)
    {//boolean isInsterted= mydb.insertData(id,name,last,user,email,message);
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL1,id);
        contentValues.put(COL2,FirstName);
        contentValues.put(COL3,LastName);
        contentValues.put(COL4,User);
        //contentValues.put(COL5,Pass);
        //contentValues.put(COL6,Email);
        contentValues.put(COL7,State);




        long res=db.insert(TABLE_NAME,null,contentValues);
        if (res== -1)
        {
            return false;
        }
        else
        {
            return true;
        }

    }

    public Cursor getAllData()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+ TABLE_NAME,null);
        //Log.d("awe","obteniendo todo===================================================>");
        return res;


    }
    public boolean updateData(String id_persona, String nombres, String ap_paterno, String ap_materno )
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL1,id_persona);
        contentValues.put(COL2,nombres);
        contentValues.put(COL3,ap_paterno);
        contentValues.put(COL4,ap_materno);

        db.update(TABLE_NAME,contentValues,"id_persona = ?",new String[] {id_persona});
        return true;
    }

    public Integer deleteData(String id_persona)
    {
       SQLiteDatabase db=this.getWritableDatabase();
       return db.delete(TABLE_NAME,"id_persona=?",new String[]{id_persona});
    }
    public Integer deleteAll(int id_persona)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(TABLE_NAME,"id_persona=?",new String[]{String.valueOf(id_persona)});
    }

    public Cursor searchData(String state)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res =db.rawQuery("select * from person ",null);
        return res;
    }

}
