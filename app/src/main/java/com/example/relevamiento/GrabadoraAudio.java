package com.example.relevamiento;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class GrabadoraAudio extends AppCompatActivity {

    private MediaRecorder grabacion;
    private String archivoSalida = null;
    private Button btn_recorder, btn_guardar;
    private TextView tv_grabacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grabadora_audio);

        btn_recorder = findViewById(R.id.btn_rec);
        btn_guardar = findViewById(R.id.btn_guardar);
        btn_guardar.setEnabled(false);
        tv_grabacion = findViewById(R.id.tv_grabacion);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1000);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void Recorder(View view){
        if(grabacion == null){

            btn_guardar.setEnabled(false);

            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/Grabaciones_relevamiento");
            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            archivoSalida = myDir + "/grabacion_" + System.currentTimeMillis() + ".mp3";
            ////////////File file = new File (archivoSalida);

            //if (file.exists ()) {
            //    file.delete();
            //}

            // Set the file location for the audio ---- COMPARAR!!!!
            //mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
            //mFileName += "/audiorecordtest.3gp";

            //Log.e("Archivo salida (String)", archivoSalida);
            //Log.e("Archivo salida (file)", ""+file);

            grabacion = new MediaRecorder();
            grabacion.setAudioSource(MediaRecorder.AudioSource.MIC);
            grabacion.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            grabacion.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            grabacion.setOutputFile(archivoSalida);

            try{
                grabacion.prepare();
                grabacion.start();
            } catch (IOException e){
                Toast.makeText(getApplicationContext(), "error de grabadora", Toast.LENGTH_SHORT).show();
            }
            btn_recorder.setBackgroundResource(R.drawable.rec);
            Toast.makeText(getApplicationContext(), "Grabando...", Toast.LENGTH_SHORT).show();
        } else if(grabacion != null){
            grabacion.stop();
            grabacion.release();
            grabacion = null;
            btn_recorder.setBackgroundResource(R.drawable.stop_rec);
            Toast.makeText(getApplicationContext(), "Grabacion finalizada", Toast.LENGTH_SHORT).show();
            btn_guardar.setEnabled(true);
            tv_grabacion.setText("Grabacion finalizada");
        }
    }

    public void reproducir(View view) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(archivoSalida);
            mediaPlayer.prepare();
        } catch (IOException e){
        }
        mediaPlayer.start();
        Toast.makeText(getApplicationContext(), "Reproduciendo audio", Toast.LENGTH_SHORT).show();
    }

    /*private void guardar() {
        //If you wanted to add this audio to the media library on the phone:
        ContentValues values = new ContentValues(4);
        long current = System.currentTimeMillis();
        values.put(MediaStore.Audio.Media.TITLE, "audio file");
        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
        values.put(MediaStore.Audio.Media.DATA, archivoSalida);
        ContentResolver contentResolver = getContentResolver();
        // Construct uris
        Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri newUri = contentResolver.insert(base, values);
        // Trigger broadcast to add
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
        Toast.makeText(this, "Added File " + newUri, Toast.LENGTH_LONG).show();
    }
     */


    public void guardar (View view){
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Planilla.AUDIO, archivoSalida);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
    





}