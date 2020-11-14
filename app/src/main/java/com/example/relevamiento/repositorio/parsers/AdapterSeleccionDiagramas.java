package com.example.relevamiento.repositorio.parsers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.relevamiento.R;

import java.io.File;
import java.util.ArrayList;

public class AdapterSeleccionDiagramas extends BaseAdapter {

    private Context context;
    private ArrayList<String> diagramas;
    private Bitmap myBitmap;

    public AdapterSeleccionDiagramas(Context context, ArrayList<String> diagramas) {
        this.context = context;
        this.diagramas = diagramas;
    }

    @Override
    public int getCount() {
        return diagramas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view1 = LayoutInflater.from(context).inflate(R.layout.row_selecciondiagramas, null);

        TextView name = view1.findViewById(R.id.nombreDiagramas);
        ImageView image = view1.findViewById(R.id.diagramas);

        name.setText("Diagrama "+(position+1));

        String current = diagramas.get(position);
        File imgFile = new File(current);
        if (imgFile.exists()) {
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            image.setImageBitmap(myBitmap); //setea el diagrama seleccionado
        }
        return view1;
    }

}
