package com.example.relevamiento;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.relevamiento.modelos.Formulario;
import com.example.relevamiento.repositorio.Repositorio;

import java.util.ArrayList;

public class PlanillaEditar extends AppCompatActivity {

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
    private int proyId, formId, cantAudios, cantFotos;
    private ArrayList<String> elem_seleccionados, nombreElementos;
    private ArrayAdapter<String> adapter, adapterSeleccion;
    private String nombreProyecto, diagramaActual;
    private TextView tv_cantAudios, tv_cantFotos;

    private androidx.appcompat.widget.SearchView searchView;
    private ListView lv_elementosSeleccionados, lv_todosElementos;
    private Button btn_agregar, btn_eliminarForm;
    private ArrayList<Integer> listaMarcas;
    private boolean correcto;
    private Formulario formulario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planilla);

        searchView = findViewById(R.id.search_bar);
        lv_elementosSeleccionados = findViewById(R.id.lv_listaElem);
        lv_todosElementos = findViewById(R.id.lv_todosElementos);
        btn_agregar = findViewById(R.id.btn_agregar);
        btn_eliminarForm = findViewById(R.id.btn_eliminarForm);
        btn_eliminarForm.setVisibility(View.VISIBLE);
        tv_cantAudios = findViewById(R.id.tv_cantAudios);
        tv_cantFotos = findViewById(R.id.tv_cantFotos);

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
            searchView.setClickable(false); //editar un unico formulario
            mostrarElementosMismoFormulario(elem, proyId);
            btn_agregar.setEnabled(false); //no se vuelve a usar
            btn_eliminarForm.setEnabled(true); //se habilita

        }
    }

    private void mostrarElementosMismoFormulario(String elem, int proyId) {
        formId = repo.getFormId(elem, proyId); // del elemento seleccionado obtengo su formulario
        ArrayList<String> elementosFormulario = repo.getElementosMismoFormulario(proyId, formId);  //lista de elementos mismo formulario
        for (String s: elementosFormulario) {
            elem_seleccionados.add(s);
            // los elimina de la lista inicial de elementos
            nombreElementos.remove(s);
            adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, nombreElementos);
            lv_todosElementos.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        lv_elementosSeleccionados.setAdapter(adapterSeleccion); //mostrarlos
        //obtener de la BD los datos del formulario (marcas, correctitud, diagrama)
        cargarDatosFormulario(formId);
        inicializarContadores();
    }

    private void cargarDatosFormulario(int formId) {
        formulario = repo.getFormulario(formId);
        diagramaActual = formulario.getDiagrama();
        listaMarcas = formulario.getMarcas();
        correcto = formulario.isEsCorrecto();
    }
    private void inicializarContadores() {
        cantAudios = formulario.getAudios().size();
        tv_cantAudios.setText(""+cantAudios);
        cantFotos = formulario.getFotos().size();
        tv_cantFotos.setText(""+cantFotos);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode){
                case ACTIVITY_GRABAR_AUDIO:
                    String pathAudio = data.getStringExtra(AUDIO);
                    if (pathAudio != null) {
                        formulario.agregarAudio(pathAudio);
                        cantAudios++;
                        tv_cantAudios.setText(""+cantAudios);
                    }
                    break;

                case ACTIVITY_PLANO:
                    listaMarcas = data.getIntegerArrayListExtra(MARCAS);
                    formulario.setMarcas(listaMarcas);
                    correcto = data.getBooleanExtra(SWITCH, true);
                    formulario.setEsCorrecto(correcto);
                    break;

                case ACTIVITY_CAMARA:
                    String pathFoto = data.getStringExtra(FOTO);
                    if (pathFoto != null) {
                        formulario.agregarFoto(pathFoto);
                        cantFotos++;
                        tv_cantFotos.setText(""+cantFotos);
                    }
                    break;

                case ACTIVITY_DOCUMENTO:
                    break;
            }
        }
    }


    public void agregarAudio(View view){
        Intent i = new Intent(this, GrabadoraAudio.class);
        startActivityForResult(i, ACTIVITY_GRABAR_AUDIO);
    }

    public void agregarFoto(View view) {
        if (repo.getProyecto(nombreProyecto).permite_fotos()) {
            Intent i = new Intent(this, Camara.class);
            startActivityForResult(i, ACTIVITY_CAMARA);
        }else {
            Toast.makeText(this, "Fotos: Opcion DESHABILITADA" , Toast.LENGTH_SHORT).show();
        }
    }

    public void verPlano(View view) {
        Intent i = new Intent(this, Marcar.class);
        i.putExtra(Marcar.DIAGRAMA, diagramaActual);
        i.putExtra(Marcar.MARCAS, listaMarcas);
        i.putExtra(Marcar.SWITCH, correcto);
        startActivityForResult(i, ACTIVITY_PLANO);
    }

    public void verDocumento(View view) {
        Intent i = new Intent(this, Documentos.class);
        startActivityForResult(i, ACTIVITY_DOCUMENTO);
    }


    public void guardarFormulario(View view){
        if (listaMarcas.size() < 4) {
            Toast.makeText(this, "Marcar region relevada", Toast.LENGTH_SHORT).show();
        }else{  //actualizar Formulario en BD: marcas - correctitud - audio - foto
             boolean exito = repo.actualizarFormulario(formulario);

            if (exito) {
                Toast.makeText(this, "Formulario guardado", Toast.LENGTH_SHORT).show();
                volverPrincipal();
            }
        }
    }

    public void eliminarformulario(View view){
        AlertDialog diaBox = confirmacionEliminar();
        diaBox.show();
    }

    private AlertDialog confirmacionEliminar() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Eliminar Relevamiento")
                .setMessage("¿Seguro desea eliminar?")

                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        boolean exito = repo.eliminarFormulario(formId);
                        if (exito) {
                            dialog.dismiss();
                            volverPrincipal();
                        }else{
                            Toast.makeText(getApplicationContext(), "error al eliminar", Toast.LENGTH_SHORT).show();
                        }
                    }

                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        return myQuittingDialogBox;
    }

    private void volverPrincipal(){
        Intent i = new Intent(this, Principal.class);
        i.putExtra(Planilla.NOMBRE_PROYECTO, nombreProyecto);
        i.putExtra(Planilla.DIAGRAMA, diagramaActual);
        startActivity(i);
        finish();
    }
}