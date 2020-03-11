package com.morajavier.practicacuatro.bd;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatosPayPal extends SQLiteOpenHelper {
    private static final String BDNAME = "Pagos";
    private String CONSULTA = "CREATE TABLE pagospaypal(_id INTEGER PRIMARY KEY AUTOINCREMENT, correo TEXT, nombrecompleto TEXT, monto TEXT)";
    private static final int VERSIONBD = 2;
    private String pathDB;

    public DatosPayPal(Context context) {
        super(context, BDNAME, null, VERSIONBD);
        this.pathDB = context.getDatabasePath(this.BDNAME).getAbsolutePath();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(this.CONSULTA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

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
    public ArrayList<String[]> mostrarDatos(){
        ArrayList<String[]> dataChida = null;
        SQLiteDatabase bd = getWritableDatabase();
        Cursor c = bd.query("pagospaypal", new String[]{"correo", "nombrecompleto", "monto"}, null, null, null, null, null);
        if(bd != null && c != null){
            if(c.moveToFirst()){
                dataChida = new ArrayList<String[]>();
                do{
                    dataChida.add(new String[]{c.getString(0), c.getString(1), c.getString(2)});
                }while(c.moveToNext());
            }
        }

        bd.close();
        return dataChida;
    }
}
