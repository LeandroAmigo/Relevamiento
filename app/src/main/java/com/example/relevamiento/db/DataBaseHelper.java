package com.example.relevamiento.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;


public class DataBaseHelper extends SQLiteOpenHelper {

    private static DataBaseHelper instance = null;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "relevamiento";

    public static DataBaseHelper getInstance(Context ctx) {
        if (instance == null) {
            instance = new DataBaseHelper(ctx.getApplicationContext());
        }
        return instance;
    }

    private DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase BaseDeDatos) {
        BaseDeDatos.execSQL("CREATE TABLE IF NOT EXISTS proyectos (proyecto_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                                                                        "proyecto_nombre TEXT NOT NULL, " +
                                                                        "proyecto_diagramas TEXT NOT NULL, " +
                                                                        "proyecto_permiteFotos INTEGER NOT NULL)"
                            );

        BaseDeDatos.execSQL("CREATE TABLE IF NOT EXISTS formularios (formulario_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                                                                        "formulario_diagrama TEXT NOT NULL, " +
                                                                        "formulario_marcas TEXT NOT NULL, " +
                                                                        "formulario_correcto INTEGER NOT NULL, " +
                                                                        "formulario_observacion TEXT, " +
                                                                        "formulario_foto TEXT, " +
                                                                        "formulario_audio TEXT," +
                                                                        "formulario_imagen TEXT, " +
                                                                        "proyecto_id INTEGER NOT NULL, " +
                                                                        "FOREIGN KEY (proyecto_id) " +
                                                                        "REFERENCES proyectos(proyecto_id) " +
                                                                            "ON UPDATE CASCADE " +
                                                                            "ON DELETE CASCADE)"
                            );

        BaseDeDatos.execSQL("CREATE TABLE IF NOT EXISTS elementos (elemento_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                                                                       "elemento_nombre TEXT NOT NULL, " +
                                                                       "proyecto_id INTEGER NOT NULL, " +
                                                                       "formulario_id INTEGER, " +
                                                                        "FOREIGN KEY (proyecto_id) " +
                                                                        "REFERENCES proyectos(proyecto_id) " +
                                                                            "ON UPDATE CASCADE " +
                                                                            "ON DELETE CASCADE, " +
                                                                        "FOREIGN KEY (formulario_id)" +
                                                                        "REFERENCES formularios(formulario_id) " +
                                                                                "ON UPDATE CASCADE "  +
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

    @Override
    public void onConfigure(SQLiteDatabase BaseDeDatos) {
        BaseDeDatos.setForeignKeyConstraintsEnabled(true);
    }
}
