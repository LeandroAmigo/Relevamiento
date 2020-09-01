package com.example.relevamiento;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.relevamiento.db.DataBaseHelper;



public class MainActivity extends AppCompatActivity {

    private Button btn_abrir, btn_nuevo;
    private static DataBaseHelper dbHelper;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_abrir = (Button) findViewById(R.id.btn_abrir);
        btn_nuevo = (Button) findViewById(R.id.btn_nuevo);

        conexionBD(); //onCreate. equivalente a patron Singleton.

    }

    private void conexionBD() {
        dbHelper = new DataBaseHelper(this);
    }

    public void abrir (View view) {
        intent = new Intent(this, ListadoProyectos.class);
        startActivity(intent);

    }

    public void nuevo (View view){
        intent = new Intent(this, CrearCargarProyecto.class);
        startActivity(intent);
    }

    public static DataBaseHelper getBD(){
        return dbHelper;
    }


}