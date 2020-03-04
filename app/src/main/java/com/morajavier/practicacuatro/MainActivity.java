package com.morajavier.practicacuatro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RadioGroup radioGroupMaster;
    private EditText editTextDinamico;
    private LinearLayout linearLayoutInsertar;
    private ArrayList<EditText> listEditText = new ArrayList<EditText>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayoutInsertar = findViewById(R.id.linearLayoutCamposDinamicos);
        radioGroupMaster = findViewById(R.id.radioGrupoMasterSiNo);
        /*
        * Si selecciono si, tres campos
        * Si slecciona no, que agregue cuatro campos
        *
        * */
        radioGroupMaster.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int check) {
                /*editTextDinamico = null;
                listEditText = null;
                linearLayoutInsertar = null;*/
                linearLayoutInsertar.removeAllViews();
                if(check == R.id.radioSi){
                    for(int i=0; i<3; ++i){
                        editTextDinamico = new EditText(MainActivity.this);
                        editTextDinamico.setContentDescription("Idnetificador1");
                        editTextDinamico.setHint("Caja "+ (i+1));
                        listEditText.add(editTextDinamico);
                        linearLayoutInsertar.addView(editTextDinamico);
                    }
                    Toast.makeText(MainActivity.this, "Presiono Si", Toast.LENGTH_SHORT).show();
                }else if(check == R.id.radioNo){
                    for(int i=0; i<2; ++i){
                        editTextDinamico = new EditText(MainActivity.this);
                        editTextDinamico.setContentDescription("Idnetificador1");
                        editTextDinamico.setHint("Caja "+ (i+1));
                        listEditText.add(editTextDinamico);
                        linearLayoutInsertar.addView(editTextDinamico);
                    }
                    Toast.makeText(MainActivity.this, "Presiono No", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
