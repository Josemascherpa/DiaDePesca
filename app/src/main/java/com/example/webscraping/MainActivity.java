package com.example.webscraping;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    NotificationsManager notiManager = new NotificationsManager();
    String versionApp = "Version 0.5.3";
    TextView ubicacion_tv;
    TextView altura_tv;
    TextView variacion_tv;
    TextView fecha_tv;
    TextView estado_tv;
    TextView alturaAnt_tv;
    TextView fechaAnt_tv;
    TextView versionTV;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        versionTV = (TextView)findViewById(R.id.version);
        versionTV.setText(versionApp);
        ubicacion_tv = (TextView) findViewById(R.id.ubicacion);
        altura_tv = (TextView) findViewById(R.id.altura);
        variacion_tv = (TextView) findViewById(R.id.variacion);
        fecha_tv = (TextView) findViewById(R.id.ultimaActua);
        estado_tv = (TextView) findViewById(R.id.estado);
        alturaAnt_tv = (TextView) findViewById(R.id.altAnterior);
        fechaAnt_tv = (TextView) findViewById(R.id.fechaAnterior);
        ActualizarUI(bundle);
        notiManager.CreateChannelNotification("channel_id","channel_name","description_channel",this);
        notiManager.SendNotify("tituo","texto notificacion",1,this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ubicacion_tv = (TextView) findViewById(R.id.ubicacion);
        altura_tv = (TextView) findViewById(R.id.altura);
        variacion_tv = (TextView) findViewById(R.id.variacion);
        fecha_tv = (TextView) findViewById(R.id.ultimaActua);
        estado_tv = (TextView) findViewById(R.id.estado);
        alturaAnt_tv = (TextView) findViewById(R.id.altAnterior);
        fechaAnt_tv = (TextView) findViewById(R.id.fechaAnterior);
        ActualizarUI(bundle);

    }

    private void ActualizarUI(Bundle bundle){
        // actualizar UI
        ubicacion_tv.setText(bundle.getString("location"));
        altura_tv.setText(bundle.getString("altura"));
        variacion_tv.setText(bundle.getString("variacion"));
        fecha_tv.setText(bundle.getString("fecha"));
        estado_tv.setText(bundle.getString("estado"));
        alturaAnt_tv.setText(bundle.getString("variacionAnterior"));
        fechaAnt_tv.setText(bundle.getString("fechaAnterior"));

        if(Float.parseFloat(variacion_tv.getText().toString())<0){
            variacion_tv.setTextColor(Color.parseColor("#D24545"));
            estado_tv.setTextColor(Color.parseColor("#D24545"));
        }else{
            variacion_tv.setTextColor(Color.parseColor("#557C55"));
            estado_tv.setTextColor(Color.parseColor("#557C55"));
        }
    }



}



