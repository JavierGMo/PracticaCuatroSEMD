package com.morajavier.practicacuatro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.morajavier.practicacuatro.bd.DatosPago;
import com.morajavier.practicacuatro.bd.DatosPayPal;
import com.morajavier.practicacuatro.listapagos.ListaPagos;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RadioGroup radioGroupMaster;
    private EditText edTxtNoCuenta, edTxtMailPayPal, edTxtNombreCompleto, edTxtMontoAPagar;
    private LinearLayout linearLayoutInsertar;
    private Button btnPagar;
    private ListView listaDePagos;
    private DatosPayPal pagoPayPal = new DatosPayPal(MainActivity.this);
    private DatosPago datosPago = new DatosPago(MainActivity.this);
    private SQLiteDatabase controlPagosDirectos = datosPago.getWritableDatabase(), controlPayPal = pagoPayPal.getWritableDatabase();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayoutInsertar = findViewById(R.id.linearLayoutCamposDinamicos);

        radioGroupMaster = findViewById(R.id.radioOpcionPago);

        edTxtNoCuenta = findViewById(R.id.noCuentaBanco);
        edTxtMailPayPal = findViewById(R.id.cuentaPayPal);
        edTxtNombreCompleto = findViewById(R.id.nombreCompleto);
        edTxtMontoAPagar = findViewById(R.id.montoAPagar);

        btnPagar = findViewById(R.id.buttonDepositar);

        listaDePagos = findViewById(R.id.listaPagos);

        radioGroupMaster.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int check) {
                //editTextDinamico = null;
                //listEditText = null;
                //linearLayoutInsertar = null;
                //linearLayoutInsertar.removeAllViews();
                SQLiteDatabase db = null;

                if(check == R.id.radioPagoBanco){
                    mostrarCampos(View.GONE, View.VISIBLE);
                    limpiarCampos();
                    Toast.makeText(MainActivity.this, "Pago en banco", Toast.LENGTH_SHORT).show();
                }else if(check == R.id.radioMostrarPagoBanco){
                    limpiarCampos();
                    desparecerCampos();


                    //db = datosPago.getReadableDatabase();
                    if(datosPago.checkDataBase()){
                        try{
                            ListaPagos listaPagos = new ListaPagos(MainActivity.this, datosPago.mostrarDatos());
                            listaDePagos.setAdapter(listaPagos);
                            Toast.makeText(MainActivity.this, "Pagos en banco", Toast.LENGTH_SHORT).show();
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }else{
                        Toast.makeText(MainActivity.this, "No esta disponible esta opcion", Toast.LENGTH_SHORT).show();
                    }

                }else if(check == R.id.radioPagoPayPal){
                    limpiarCampos();
                    mostrarCampos(View.VISIBLE, View.GONE);
                    Toast.makeText(MainActivity.this, "Va a pagar con PayPal", Toast.LENGTH_SHORT).show();
                }else if(check == R.id.radioMostarPagoPayPal){
                    limpiarCampos();
                    desparecerCampos();
                    ArrayList<String[]> dataChida;
                    //db = pagoPayPal.getReadableDatabase();
                    if(pagoPayPal.checkDataBase()){
                        try{
                            SQLiteDatabase dataB= pagoPayPal.getWritableDatabase();
                            if(dataB != null){
                                Cursor cu = db.query("pagospaypal", new String[]{"notarjeta", "nombrecompleto", "monto"}, null, null, null, null, null);
                                if(cu != null){
                                    if(cu.moveToFirst()){
                                        dataChida = new ArrayList<String[]>();
                                        do{
                                            dataChida.add(new String[]{cu.getString(0), cu.getString(1), cu.getString(2)});
                                        }while(cu.moveToNext());
                                    }
                                }
                                cu.close();
                            }


                            dataB.close();
                            /*ListaPagos listaPagos = new ListaPagos(MainActivity.this, pagoPayPal.mostrarDatos());
                            listaDePagos.setAdapter(listaPagos);*/
                            Toast.makeText(MainActivity.this, "Pagos en PayPal", Toast.LENGTH_SHORT).show();
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }else{
                        Toast.makeText(MainActivity.this, "No diponible pagos en PayPal", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = null;
                String nombreTabla = "";
                ContentValues cv = null;
                if(edTxtMailPayPal.getText() != null && edTxtMailPayPal.getText().toString().trim() != ""){
                    DatosPayPal pagoPayPal = new DatosPayPal(MainActivity.this);
                    db = pagoPayPal.getWritableDatabase();
                    cv = new ContentValues();
                    cv.put("correo", edTxtMailPayPal.getText().toString());
                    cv.put("nombrecompleto", edTxtNombreCompleto.getText().toString());
                    cv.put("monto", edTxtMontoAPagar.getText().toString());
                    nombreTabla = "pagospaypal";
                    if(insertarDatos(controlPayPal, cv, nombreTabla)){
                        Toast.makeText(MainActivity.this, "Registro creado", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this, "Registro no creado", Toast.LENGTH_SHORT).show();
                    }
                }else if(edTxtNoCuenta.getText() != null && edTxtNoCuenta.getText().toString().trim() != ""){
                    DatosPago pagoDirecto = new DatosPago(MainActivity.this);
                    db = pagoDirecto.getWritableDatabase();
                    cv = new ContentValues();
                    cv.put("notarjeta", edTxtNoCuenta.getText().toString());
                    cv.put("nombrecompleto", edTxtNombreCompleto.getText().toString());
                    cv.put("monto", edTxtMontoAPagar.getText().toString());
                    if(insertarDatos(controlPagosDirectos, cv, nombreTabla)){
                        Toast.makeText(MainActivity.this, "Registro creado", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this, "Registro no creado", Toast.LENGTH_SHORT).show();
                    }
                    nombreTabla = "pagosdirectos";
                }

            }
        });
    }
    private void desparecerCampos(){
        edTxtMailPayPal.setVisibility(View.GONE);
        edTxtNoCuenta.setVisibility(View.GONE);
        edTxtNombreCompleto.setVisibility(View.GONE);
        edTxtMontoAPagar.setVisibility(View.GONE);
        btnPagar.setVisibility(View.GONE);
    }
    private void mostrarCampos(int visibilidadCero, int visibilidadUno){
        edTxtMailPayPal.setText("");
        edTxtNoCuenta.setText("");
        edTxtNombreCompleto.setText("");
        edTxtMontoAPagar.setText("");

        edTxtMailPayPal.setVisibility(visibilidadCero);
        edTxtNoCuenta.setVisibility(visibilidadUno);
        edTxtNombreCompleto.setVisibility(View.VISIBLE);
        edTxtMontoAPagar.setVisibility(View.VISIBLE);
        btnPagar.setVisibility(View.VISIBLE);
    }
    private boolean insertarDatos(SQLiteDatabase db, ContentValues cv, String nombreTabla){

        if(db != null && cv != null && nombreTabla != ""){
            db.insert(nombreTabla, null, cv);
            return true;
        }
        return false;
    }
    private ArrayList<String[]> traerDatos(SQLiteDatabase db, String nombreTabla, String[] columnasConsulta){
        ArrayList<String[]> dataChida = new ArrayList<String[]>();
        Cursor c = db.query(
                nombreTabla,
                columnasConsulta,
                null,
                null,
                null,
                null,
                null);

        while (c.moveToNext()){
            String[] dataCruda = {c.getString(c.getColumnIndex(columnasConsulta[0])),
                    c.getString(c.getColumnIndex(columnasConsulta[1])),
                    c.getString(c.getColumnIndex(columnasConsulta[2]))};
            dataChida.add(dataCruda);
        }
        c.close();
        db.close();
        return dataChida;
    }
    private void limpiarCampos(){
        this.edTxtMailPayPal.setText("");
        this.edTxtNoCuenta.setText("");
        this.edTxtNombreCompleto.setText("");
        this.edTxtMontoAPagar.setText("");
    }
}
