package com.example.relevamiento.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "relevamiento";


    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase BaseDeDatos) {
        BaseDeDatos.execSQL("CREATE TABLE IF NOT EXISTS proyectos (proyecto_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                                                   "proyecto_nombre TEXT NOT NULL, " +
                                                   "proyecto_diagramas TEXT NOT NULL, " +
                                                   "proyecto_elementos TEXT, " +
                                                   "proyecto_fotos INTEGER NOT NULL)"
                            );

        BaseDeDatos.execSQL("CREATE TABLE elementos (elemento_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                    "elemento_numero INTEGER, " +
                                                    "elemento_marcas TEXT, " +
                                                    "elemento_correcto INTEGER, " +
                                                    "elemento_diagrama TEXT, " +
                                                    "proyecto_id INTEGER NOT NULL, " +
                                                    "FOREIGN KEY (proyecto_id) " +
                                                    "REFERENCES proyectos(proyecto_id) " +
                                                            "ON UPDATE CASCADE " +
                                                            "ON DELETE CASCADE)"
                            );

        BaseDeDatos.execSQL("CREATE TABLE formularios (formulario_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                     "formulario_observacion TEXT, " +
                                                     "formulario_foto TEXT, " +
                                                     "formulario_audio TEXT," +
                                                     "elemento_id INTEGER NOT NULL, " +
                                                     "FOREIGN KEY (elemento_id) " +
                                                     "REFERENCES elementos(elemento_id) " +
                                                            "ON UPDATE CASCADE " +
                                                            "ON DELETE CASCADE)"
                            );
    }

    @Override
    public void onUpgrade(SQLiteDatabase BaseDeDatos, int i, int i1) {
        BaseDeDatos.execSQL("drop table if exists proyectos");
        BaseDeDatos.execSQL("drop table if exists elementos");
        BaseDeDatos.execSQL("drop table if exists formularios");
        onCreate(BaseDeDatos);
    }
}
