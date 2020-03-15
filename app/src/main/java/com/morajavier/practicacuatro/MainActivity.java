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

    private SQLiteDatabase controlPagosDirectos, controlPayPal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final DatosPayPal pagoPayPal = new DatosPayPal(this);
        final DatosPago datosPago = new DatosPago(this);

        controlPagosDirectos = datosPago.getWritableDatabase();
        controlPayPal = pagoPayPal.getWritableDatabase();



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

                if(check == R.id.radioPagoBanco){
                    limpiarCampos();
                    mostrarCampos(View.GONE, View.VISIBLE);
                    Toast.makeText(MainActivity.this, "Pago en banco", Toast.LENGTH_SHORT).show();
                }else if(check == R.id.radioMostrarPagoBanco){
                    limpiarCampos();
                    desparecerCampos();

                    if(datosPago.checkDataBase()){
                        Cursor c = controlPagosDirectos.rawQuery("SELECT notarjeta, nombrecompleto, monto FROM pagosdirectos;", null);
                        ArrayList<String[]> dataControl = new ArrayList<>();
                        try{
                            if(c != null){
                                if(c.moveToFirst()){
                                    do {
                                        dataControl.add(new String[]{c.getString(0), c.getString(1), c.getString(2)});
                                    }while (c.moveToNext());
                                }
                                c.close();
                            }
                            ListaPagos controladorPagos = new ListaPagos(MainActivity.this, dataControl);
                            listaDePagos.setAdapter(controladorPagos);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }else if(check == R.id.radioPagoPayPal){
                    limpiarCampos();
                    mostrarCampos(View.VISIBLE, View.GONE);
                    Toast.makeText(MainActivity.this, "Va a pagar con PayPal", Toast.LENGTH_SHORT).show();
                }else if(check == R.id.radioMostarPagoPayPal){
                    limpiarCampos();
                    desparecerCampos();
                    if(pagoPayPal.checkDataBase()){
                        Cursor c = controlPayPal.rawQuery("SELECT correo, nombrecompleto, monto FROM pagospaypal;", null);
                        ArrayList<String[]> dataControl = new ArrayList<>();
                        try{
                            if(c != null){
                                if(c.moveToFirst()){
                                    do {
                                        dataControl.add(new String[]{c.getString(0), c.getString(1), c.getString(2)});
                                    }while (c.moveToNext());
                                }
                                c.close();
                            }
                            ListaPagos controladorPagos = new ListaPagos(MainActivity.this, dataControl);
                            listaDePagos.setAdapter(controladorPagos);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        //Para guardar datos
        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = null;
                String nombreTabla = "";
                ContentValues cv = null;
                if(edTxtMailPayPal.getText() != null && edTxtMailPayPal.getText().toString().trim() != ""){
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
                    cv = new ContentValues();
                    cv.put("notarjeta", edTxtNoCuenta.getText().toString());
                    cv.put("nombrecompleto", edTxtNombreCompleto.getText().toString());
                    cv.put("monto", edTxtMontoAPagar.getText().toString());
                    if(insertarDatos(controlPagosDirectos, cv, "pagosdirectos")){
                        Toast.makeText(MainActivity.this, "Registro creado", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this, "Registro no creado", Toast.LENGTH_SHORT).show();
                    }
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
        boolean ok = false;
        if(db != null && cv != null && nombreTabla != ""){
            if(db.insert(nombreTabla, null, cv) != SQLiteDatabase.CONFLICT_FAIL){
                ok = true;
            }else {
                ok = false;
            }
        }
        return ok;
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
