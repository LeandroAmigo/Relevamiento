package com.example.relevamiento;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ListView;
import android.widget.Toast;
import com.example.relevamiento.modelos.Elemento;
import com.example.relevamiento.repositorio.Repositorio;
import java.util.ArrayList;

public class Planilla extends AppCompatActivity {

    public static final String NOMBRE_PROYECTO = "proyecto_nombre";
    public static final String DIAGRAMA = "diagrama";
    public static final String MARCAS = "marcas";
    public static final String SWITCH = "switch";

    public static final String AUDIO = "audio";
    public static final String FOTO = "foto";

    private static final int ACTIVITY_GRABAR_AUDIO = 3461;
    private static final int ACTIVITY_CAMARA = 1888;
    private static final int ACTIVITY_PLANO = 3543;
    private static final int ACTIVITY_DOCUMENTO = 6302;


    private Repositorio repo;
    private int proyId;
    private ArrayList<String> elem_seleccionados, nombreElementos;
    private ArrayAdapter<String> adapter, adapterSeleccion;
    private String pathAudio, pathFoto, nombreProyecto, diagramaActual;

    private androidx.appcompat.widget.SearchView searchView;
    private ListView elementosSeleccionados, lv_todosElementos;
    private ArrayList<Integer> listaMarcas;
    private boolean correcto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planilla);

        searchView = findViewById(R.id.search_bar);
        elementosSeleccionados =  findViewById(R.id.lv_listaElem);
        lv_todosElementos = findViewById(R.id.lv_todosElementos);

        nombreElementos = new ArrayList<String>();
        listaMarcas = new ArrayList<Integer>();

        repo = new Repositorio(this);
        //inicializa lista de los ya seleccionados
        elem_seleccionados = new ArrayList<String>();
        adapterSeleccion = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, elem_seleccionados);

        if (getIntent().hasExtra(NOMBRE_PROYECTO)) {
            nombreProyecto = getIntent().getStringExtra(NOMBRE_PROYECTO);
            proyId = repo.getIdProyecto(nombreProyecto);
        }
        if (getIntent().hasExtra(DIAGRAMA)) {
            diagramaActual = getIntent().getStringExtra(DIAGRAMA);
        }

        mostrarListaElementos();

        Intent intent = new Intent("com.realwear.wearhf.intent.action.MOUSE_COMMANDS");
        intent.putExtra("com.realwear.wearhf.intent.extra.MOUSE_ENABLED", false);
        sendBroadcast(intent);
    }

    private void mostrarListaElementos() {
        int formId;
        ArrayList<Elemento> listaElementos = repo.getElementos(proyId);
        for (Elemento e: listaElementos) {
            formId = e.getFormId();
            if (formId == 0) { // no tiene formulario asociado
                nombreElementos.add(e.getNombre());
            }
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nombreElementos);
        lv_todosElementos.setAdapter(adapter);

        lv_todosElementos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               searchView.setQuery(parent.getItemAtPosition(position).toString(), false);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    public void agregarElementos(View view){
        String elem = searchView.getQuery().toString();
        if (elem.isEmpty()) {
            Toast.makeText(this, "Ingresar Elemento relevado", Toast.LENGTH_SHORT).show();
        }else {
            searchView.setQuery("", false);
            if ( !nombreElementos.contains(elem)){
                repo.agregarNuevoElemento(proyId, elem);
            }
            elem_seleccionados.add(elem); //lista de elementos relevados
            elementosSeleccionados.setAdapter(adapterSeleccion); //mostrarlos
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case ACTIVITY_GRABAR_AUDIO:
                    pathAudio = data.getStringExtra(AUDIO);
                    Log.e("en PLanilla", pathAudio);
                    break;

                case ACTIVITY_PLANO:
                    listaMarcas = data.getIntegerArrayListExtra(MARCAS);
                    correcto = data.getBooleanExtra(SWITCH, true);
                     Intent intent = new Intent("com.realwear.wearhf.intent.action.MOUSE_COMMANDS");
                     intent.putExtra("com.realwear.wearhf.intent.extra.MOUSE_ENABLED", false);
                     sendBroadcast(intent);
                    break;

                case ACTIVITY_CAMARA:
                    pathFoto = data.getStringExtra(FOTO);
                    Log.e("en PLanilla", pathFoto);
                    break;

                case ACTIVITY_DOCUMENTO:
                    break;
            }
        }
    }

    public void agregarFoto(View view) {
        Intent i = new Intent(this, Camara.class);
        startActivityForResult(i,ACTIVITY_CAMARA);
    }

    public void agregarAudio(View view){
        Intent i = new Intent(this, GrabadoraAudio.class);
        startActivityForResult(i, ACTIVITY_GRABAR_AUDIO);
    }

    public void verDocumento(View view) {
        Intent i = new Intent(this, Documentos.class);
        startActivityForResult(i, ACTIVITY_DOCUMENTO);
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
        }else{  //crear Formulario en BD
            boolean exito;
            int formId = repo.crearFormulario(proyId, diagramaActual, listaMarcas, correcto);

            if (formId != -1){ //correcto
                exito = actualizarElementos(formId);

                //guardar AUDIO
                //exito = repo.agregarAudioFormulario(formId, pathAudio);


                Intent i = new Intent(this, Principal.class);
                i.putExtra(Planilla.NOMBRE_PROYECTO, nombreProyecto);
                i.putExtra(Planilla.DIAGRAMA, diagramaActual);
                startActivity(i);
                finish();
            }


        }
    }

    private boolean actualizarElementos(int formId) {
        boolean exito= false;
        for (String s: elem_seleccionados) {
            int elemId = repo.getIdElemento(s, proyId);
            exito = repo.actualizarElemento(elemId, formId);
            Log.e("EXITO ACTULIZAR ELEM", ""+exito);
        }
        return exito;
    }


}