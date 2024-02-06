package com.example.webscraping;

import static android.content.ContentValues.TAG;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    String CHANNEL_ID="mi_canal_01";
    CharSequence CHANNEL_NAME = "Notificaciones";
    TextView ubicacion_tv;
    TextView altura_tv;
    TextView variacion_tv;
    TextView fecha_tv;
    TextView estado_tv;
    TextView alturaAnt_tv;
    TextView fechaAnt_tv;
    Timer timerNotification = new Timer();
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        timerNotification.schedule(new TimerTask() {
            @Override
            public void run() {
                //SendNotify();
            }
        },0,1* TimeUnit.MINUTES.toMillis(1));
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void SendNotify(){
        NotificationChannel canal = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        canal.enableLights(true);
        canal.setLightColor(Color.RED);
        canal.enableVibration(true);
        canal.setDescription("Notificaciones de mi app");

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(canal);

// 2. Crear la Notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.mipmap.ic_pesca);
        builder.setContentTitle("Notificación de Prueba");
        builder.setContentText("Mi primera notificación");
        builder.setAutoCancel(true);

        Notification notification = builder.build();

// 3. Lanzar la Notificación
        manager.notify(1, notification);
    }

}



