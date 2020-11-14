package com.example.relevamiento.repositorio.parsers;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.relevamiento.R;

import java.util.ArrayList;

public class AdapterElementosPrincipal extends BaseAdapter {

    private ArrayList<StatusAdapter> status;
    private Context context;

    public AdapterElementosPrincipal(Context context, ArrayList<StatusAdapter> records){
        this.context = context;
        status = records;
    }

    @Override
    public int getCount() {
        return status.size();
    }

    @Override
    public StatusAdapter getItem(int position) {
        return  status.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View view, ViewGroup parent){
        View view2 =  LayoutInflater.from(context).inflate(R.layout.listview_elementos_principal, null);

        TextView name = (TextView) view2.findViewById(R.id.tv_viewholder);
        name.setText(getItem(position).getNombreElemento());

        int correctitud = getItem(position).getCorrecto();
        if (correctitud == 1){
            name.setTextColor(Color.GREEN);
        }else if (correctitud == 0) {
            name.setTextColor(Color.RED);
        }
        return view2;
    }



}