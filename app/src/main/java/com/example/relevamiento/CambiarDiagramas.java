package com.example.relevamiento;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class CambiarDiagramas extends AppCompatActivity {

    private String pathDiagrama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_diagramas);
    }



    public void aceptar(){
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Principal.DIAGRAMA, pathDiagrama);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }


}