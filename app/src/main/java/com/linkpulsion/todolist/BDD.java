/*
 * Copyright (c) 2016 - Cyprien Aubry
 * Tous Droits Reservés
 */

package com.linkpulsion.todolist;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Classe de gestion de la base de données
 * @author Cyprien
 * @version 1.2
 */
public class BDD extends SQLiteOpenHelper {

    private final static int VERSION = 1;
    private final static String DB_NAME = "Todo.db";
    public final static String TABLE_NAME = "ToDo";
    public final static String KEY_ID = "id";
    public final static String KEY_DESC = "desc";
    public final static String KEY_DATE = "date";
    private final static String CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_DESC + " TEXT, " + KEY_DATE +
            " DATE);";

    /**
     * Constructeur
     * @param context Context - Contexte de lancement
     */
    public BDD(Context context){
        super(context,DB_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Méthode nécessaire au bon fonctionnement du debugger SQL
     * @param Query String
     * @return ArrayList<Cursor>
     */
    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }


    }
}
