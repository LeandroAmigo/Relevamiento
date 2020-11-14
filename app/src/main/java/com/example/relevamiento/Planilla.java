package com.example.relevamiento;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.relevamiento.modelos.Elemento;
import com.example.relevamiento.repositorio.Repositorio;
import com.example.relevamiento.repositorio.parsers.AdapterElemSeleccionadosPlanilla;

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
    private int proyId, cantAudios, cantFotos;
    private ArrayList<String> elem_seleccionados, nombreElementos, audios, fotos;
    private ArrayAdapter<String> adapter, adapterSeleccion;
    private String nombreProyecto, diagramaActual;
    private TextView tv_cantAudios, tv_cantFotos;
    private Button btn_eliminarForm;

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
        tv_cantAudios = findViewById(R.id.tv_cantAudios);
        tv_cantFotos = findViewById(R.id.tv_cantFotos);
        btn_eliminarForm = findViewById(R.id.btn_eliminarForm);
        btn_eliminarForm.setVisibility(View.INVISIBLE);

        nombreElementos = new ArrayList<>();
        listaMarcas = new ArrayList<>();
        audios = new ArrayList<>();
        fotos = new ArrayList<>();

        repo = new Repositorio(this);

        //inicializa lista de los ya seleccionados
        elem_seleccionados = new ArrayList<>();
        

        if (getIntent().hasExtra(NOMBRE_PROYECTO)) {
            nombreProyecto = getIntent().getStringExtra(NOMBRE_PROYECTO);
            proyId = repo.getIdProyecto(nombreProyecto);
        }
        if (getIntent().hasExtra(DIAGRAMA)) {
            diagramaActual = getIntent().getStringExtra(DIAGRAMA);
        }

        mostrarListaElementos();
        inicializarContadores();

        Intent intent = new Intent("com.realwear.wearhf.intent.action.MOUSE_COMMANDS");
        intent.putExtra("com.realwear.wearhf.intent.extra.MOUSE_ENABLED", false);
        sendBroadcast(intent);
    }

    private void inicializarContadores() {
        cantAudios = 0;
        tv_cantAudios.setText(""+cantAudios);
        cantFotos = 0;
        tv_cantFotos.setText(""+cantFotos);
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
        adapter = new ArrayAdapter<>(this, R.layout.tv_listadiagramas_crearcargar, nombreElementos);
        adapter.setNotifyOnChange(true);
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
            if ( !nombreElementos.contains(elem)){ // se agrega a la BD si no existia
                repo.agregarNuevoElemento(proyId, elem);
            }else{ // si existia se elimina de la lista donde fue seleccionado y actualiza la vista
                nombreElementos.remove(elem);
                adapter = new ArrayAdapter(this, R.layout.tv_listadiagramas_crearcargar, nombreElementos);
                lv_todosElementos.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            elem_seleccionados.add(elem); //lista de elementos relevados
            elementosSeleccionados.setAdapter(new AdapterElemSeleccionadosPlanilla(this, elem_seleccionados));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case ACTIVITY_GRABAR_AUDIO:
                    String pathAudio = data.getStringExtra(AUDIO);
                    if (pathAudio != null) {
                        audios.add(pathAudio);
                        cantAudios++;
                        tv_cantAudios.setText(""+cantAudios);
                    }
                    break;

                case ACTIVITY_PLANO:
                    listaMarcas = data.getIntegerArrayListExtra(MARCAS);
                    correcto = data.getBooleanExtra(SWITCH, true);
                    break;

                case ACTIVITY_CAMARA:
                    String pathFoto = data.getStringExtra(FOTO);
                    if (pathFoto != null) {
                        fotos.add(pathFoto);
                        cantFotos++;
                        tv_cantFotos.setText(""+cantFotos);
                    }
                    break;

                case ACTIVITY_DOCUMENTO:
                    break;
            }
        }
    }

    public void agregarFoto(View view) {
        if (repo.getProyecto(nombreProyecto).permite_fotos()) {
            Intent i = new Intent(this, Camara.class);
            startActivityForResult(i, ACTIVITY_CAMARA);
        }else {
            Toast.makeText(this, "Fotos: Opcion DESHABILITADA" , Toast.LENGTH_SHORT).show();
        }
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

                if ( !audios.isEmpty()) { //guardar Audio
                    exito = repo.agregarAudioFormulario(formId, audios);
                }

                if ( !fotos.isEmpty()) { //guardar Foto
                    exito = repo.agregarFotoFormulario(formId, fotos);
                }

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
        }
        return exito;
    }


}