package com.morajavier.practicacuatro.bd;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatosPago extends SQLiteOpenHelper {
    private static final String BDNAME = "Pagos";
    private String CONSULTA = "CREATE TABLE pagosdirectos(_id INTEGER PRIMARY KEY AUTOINCREMENT, notarjeta TEXT, nombrecompleto TEXT, monto TEXT)";
    private static final int VERSIONBD = 2;
    private String pathDB;
    public DatosPago(Context context) {
        super(context, BDNAME, null, VERSIONBD);
        this.pathDB = context.getDatabasePath(this.BDNAME).getAbsolutePath();
    }
    public boolean checkDataBase(){
        SQLiteDatabase checkDataBase = null;
        try{
            checkDataBase = SQLiteDatabase.openDatabase(this.pathDB, null, SQLiteDatabase.OPEN_READWRITE);

        }catch (Exception e){
            Log.i("SQL Error:", "Base de datos no existe");
        }
        if(checkDataBase != null){
            checkDataBase.close();
        }

        return checkDataBase != null ? true: false;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(this.CONSULTA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<String[]> mostrarDatos(){
        ArrayList<String[]> dataChida = null;
        SQLiteDatabase bd = getWritableDatabase();
        if(bd != null){
            Cursor c = bd.query("pagosdirectos", new String[]{"notarjeta", "nombrecompleto", "monto"}, null, null, null, null, null);
            if(c != null){
                if(c.moveToFirst()){
                    dataChida = new ArrayList<String[]>();
                    do{
                        dataChida.add(new String[]{c.getString(0), c.getString(1), c.getString(2)});
                    }while(c.moveToNext());
                }
            }
            c.close();
        }


        bd.close();
        return dataChida;
    }
}
