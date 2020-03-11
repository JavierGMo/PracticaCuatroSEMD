package com.morajavier.practicacuatro.listapagos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.morajavier.practicacuatro.R;

import java.util.ArrayList;

public class ListaPagos extends BaseAdapter {
    private Context context;
    private ArrayList<String[]> data;
    private static LayoutInflater inflater = null;
    public ListaPagos(Context context, ArrayList<String[]> data){
        this.context = context;
        this.data = data;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        v = layoutInflater.inflate(R.layout.pagos, null);

        TextView tipoCuenta = v.findViewById(R.id.txtTipoCuenta),
                nombreCompleto = v.findViewById(R.id.txtnombreCompleto),
                monto = v.findViewById(R.id.txtMonto);

        if(this.data != null){
            tipoCuenta.setText(this.data.get(position)[0]);
            nombreCompleto.setText(this.data.get(position)[1]);
            monto.setText(this.data.get(position)[2]);
        }else{
            tipoCuenta.setText("ninguna data");
            nombreCompleto.setText("Ninguna data");
            monto.setText("ninguan data");
        }
        return v;
    }
}
