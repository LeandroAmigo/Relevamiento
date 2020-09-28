package com.example.relevamiento;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GrabadoraAudio extends AppCompatActivity {

    private MediaRecorder grabacion;
    private String archivoSalida = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grabadora_audio);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1000);
        }
    }

    public void Recorder(View view){
        if(grabacion == null){
            // archivoSalida = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Grabacion.mp3";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());
            archivoSalida = "mnt/sdcard/Music/Audio/Grabacion - " +currentDateandTime+ ".mp3";
            /////   archivoSalida = "/sdcard/Documents/Grabacion - " +currentDateandTime+ ".mp3";
            Log.e("PATH", archivoSalida);
            grabacion = new MediaRecorder();
            grabacion.setAudioSource(MediaRecorder.AudioSource.MIC);
            grabacion.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            grabacion.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            grabacion.setOutputFile(archivoSalida);

            try{
                grabacion.prepare();
                grabacion.start();
            } catch (IOException e){
            }

            Toast.makeText(getApplicationContext(), "Grabando...", Toast.LENGTH_SHORT).show();
        } else if(grabacion != null){
            grabacion.stop();
            grabacion.release();
            grabacion = null;
            Toast.makeText(getApplicationContext(), "Grabacion finalizada", Toast.LENGTH_SHORT).show();
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

    public void guardar (View view){
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Planilla.AUDIO, archivoSalida);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }






}