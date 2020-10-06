package com.example.relevamiento;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.relevamiento.modelos.Elemento;
import com.example.relevamiento.repositorio.Repositorio;

import java.util.ArrayList;

public class PlanillaEditar extends AppCompatActivity {

    public static final String NOMBRE_PROYECTO = "proyecto_nombre";
    public static final String DIAGRAMA = "diagrama";
    public static final String MARCAS = "marcas";
    public static final String SWITCH = "switch";

    public static final String AUDIO = "audio";
    public static final String FOTO = "foto";

    private static final int ACTIVITY_GRABAR_AUDIO = 1;
    private static final int ACTIVITY_FOTO = 2;
    private static final int ACTIVITY_PLANO = 33;


    private Repositorio repo;
    private int proyId, formId;
    private ArrayList<String> elem_seleccionados, nombreElementos;
    private ArrayAdapter<String> adapter, adapterSeleccion;
    private String pathAudio, pathFoto, nombreProyecto, diagramaActual;

    private AutoCompleteTextView atv_nombreElem;
    private ListView lv_elementosSeleccionados;
    private ArrayList<Integer> listaMarcas;
    private boolean correcto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planilla);


        atv_nombreElem =  findViewById(R.id.atv_nombreElementos);
        lv_elementosSeleccionados = findViewById(R.id.lv_listaElem);

        nombreElementos = new ArrayList<>();
        listaMarcas = new ArrayList<>();

        repo = new Repositorio(this);

        //inicializa lista
        elem_seleccionados = new ArrayList<>();
        adapterSeleccion = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, elem_seleccionados);

        if (getIntent().hasExtra(NOMBRE_PROYECTO)) {
            nombreProyecto = getIntent().getStringExtra(NOMBRE_PROYECTO);
            proyId = repo.getIdProyecto(nombreProyecto);
        }
        if (getIntent().hasExtra(DIAGRAMA)) {
            diagramaActual = getIntent().getStringExtra(DIAGRAMA);
        }

        mostrarListaElementos();
    }

    private void mostrarListaElementos() {
        int formId;
        ArrayList<Elemento> listaElementos = repo.getElementos(proyId);
        for (Elemento e: listaElementos) {
            formId = e.getFormId();
            if (formId != 0) //tiene formulario asociado
                nombreElementos.add(e.getNombre());
        }
        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, nombreElementos);
        atv_nombreElem.setAdapter(adapter);
    }

    public void agregarElementos(View view){
        String elem = atv_nombreElem.getText().toString();
        if (elem.isEmpty()) {
            Toast.makeText(this, "Ingresar Elemento relevado", Toast.LENGTH_SHORT).show();
        }else {
            atv_nombreElem.setText("");
            atv_nombreElem.setFocusable(false);
            atv_nombreElem.setEnabled(false);
            mostrarElementosMismoFormulario(elem, proyId);
        }
    }

    private void mostrarElementosMismoFormulario(String elem, int proyId) {
        formId= repo.getFormId(elem, proyId);
        ArrayList<String> elementosFormulario = repo.getElementosMismoFormulario(proyId, formId);  //lista de elementos mismo formulario
        for (String s: elementosFormulario) {
            elem_seleccionados.add(s);
        }
        lv_elementosSeleccionados.setAdapter(adapterSeleccion); //mostrarlos
    }

    public void agregarAudio(View view){
        Intent i = new Intent(this, GrabadoraAudio.class);
        startActivityForResult(i, ACTIVITY_GRABAR_AUDIO);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_GRABAR_AUDIO) {
            if (resultCode == Activity.RESULT_OK) {
                pathAudio = data.getStringExtra(AUDIO);
            }
        } else if (requestCode == ACTIVITY_PLANO){
            if (resultCode == Activity.RESULT_OK) {
                listaMarcas = data.getIntegerArrayListExtra(MARCAS);
                correcto = data.getBooleanExtra(SWITCH, true);
            }
        }
    }

    public void agregarFoto(View view) {
        Intent i = new Intent(this, GrabadoraAudio.class);
        startActivityForResult(i, ACTIVITY_FOTO);
    }

    public void verPlano(View view) {
        Intent i = new Intent(this, Marcar.class);
        i.putExtra(Marcar.DIAGRAMA, diagramaActual);
        i.putExtra(Marcar.MARCAS, listaMarcas);
        i.putExtra(Marcar.SWITCH, correcto);
        startActivityForResult(i, ACTIVITY_PLANO);
    }



    public void guardarFormulario(View view){
        if (listaMarcas.size() < 4) {
            Toast.makeText(this, "Marcar region relevada", Toast.LENGTH_SHORT).show();
        }else{  //actualizar Formulario en BD... marcas - correctitud - audio - foto -


            boolean exito;
            //int formId = repo.crearFormulario(proyId, diagramaActual, listaMarcas, correcto);

            if (formId != -1){ //correcto
                //exito = actualizarElementos(formId);

                restaurarPreferencias();

                Intent i = new Intent(this, Principal.class);
                i.putExtra(Planilla.NOMBRE_PROYECTO, nombreProyecto);
                i.putExtra(Planilla.DIAGRAMA, diagramaActual);
                startActivity(i);
                finish();
            }
            /////solo guarda AUDIO para probar
            // exito = repo.agregarDatosFormulario(formId, pathAudio);

        }
    }

   /* private boolean actualizarElementos(int formId) {
        boolean exito= false;
        for (String s: elem_seleccionados) {
            int elemId = repo.getIdElemento(s, proyId);
            exito = repo.actualizarElemento(elemId, formId);
            Log.e("EXITO ACTULIZAR ELEM", ""+exito);
        }
        return exito;
    }*/

    private void restaurarPreferencias(){
        SharedPreferences sp = getSharedPreferences("coordenadas", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().apply();
    }

}