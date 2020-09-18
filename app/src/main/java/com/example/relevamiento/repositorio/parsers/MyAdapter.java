package com.example.relevamiento.repositorio.parsers;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.relevamiento.R;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<StatusAdapter> {

    public MyAdapter(Context context, ArrayList<StatusAdapter> records){
        super(context, 0, records);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_custom, parent, false);
        }

        TextView list_txt = (TextView) convertView.findViewById(R.id.List_txt);
        final Button list_but = (Button) convertView.findViewById(R.id.List_but);

        StatusAdapter item = getItem(position);
        list_txt.setText(item.getNombreElemento());
        int correctitud = item.getCorrecto();
        if (correctitud == 1){
            list_txt.setBackgroundColor(Color.GREEN);
        } else if (correctitud == 0){
            list_txt.setBackgroundColor(Color.RED);
        }

        list_but.setText(""+(position+1));
        list_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_but.setText("CLICKEADO");

            }
        });
        return convertView;
    }


}
