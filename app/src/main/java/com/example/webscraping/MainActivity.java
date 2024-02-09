package com.example.webscraping;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    NotificationsManager notiManager = new NotificationsManager();
    ArrayList<TextView> tv_RegistrosNum = new ArrayList<TextView>();
    ArrayList<TextView> tv_RegistrosFechas = new ArrayList<TextView>();
    ArrayList<TextView> tv_RegistrosMts = new ArrayList<TextView>();
    TextView ubicacion_tv;
    TextView altura_tv;
    TextView variacion_tv;
    TextView fecha_tv;
    TextView estado_tv;
    TextView alturaAnt_tv;
    TextView fechaAnt_tv;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        IdsTextViewsRio();
        ObtainTvTableRegisters();
        ActualizarUI(bundle);
        //notiManager.CreateChannelNotification("channel_id","channel_name","description_channel",this);
        //.SendNotify("tituo","texto notificacion",1,this);
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
        fecha_tv.setText(bundle.getString("fecha").replace(" ",""));
        estado_tv.setText(bundle.getString("estado"));
        alturaAnt_tv.setText(bundle.getString("variacionAnterior"));
        fechaAnt_tv.setText(bundle.getString("fechaAnterior").replace(" ",""));

        if(Float.parseFloat(variacion_tv.getText().toString())<0){
            variacion_tv.setTextColor(Color.parseColor("#D24545"));
            estado_tv.setTextColor(Color.parseColor("#D24545"));
        }else{
            variacion_tv.setTextColor(Color.parseColor("#557C55"));
            estado_tv.setTextColor(Color.parseColor("#557C55"));
        }
    }
    private void IdsTextViewsRio(){
        ubicacion_tv = (TextView) findViewById(R.id.ubicacion);
        altura_tv = (TextView) findViewById(R.id.altura);
        variacion_tv = (TextView) findViewById(R.id.variacion);
        fecha_tv = (TextView) findViewById(R.id.ultimaActua);
        estado_tv = (TextView) findViewById(R.id.estado);
        alturaAnt_tv = (TextView) findViewById(R.id.altAnterior);
        fechaAnt_tv = (TextView) findViewById(R.id.fechaAnterior);
    }

    private void ObtainTvTableRegisters(){
        int id;
        TextView tv;
        for (int i=0;i<10;i++){
            id = getResources().getIdentifier(String.format("tv_numRegistro%s", i), "id", getPackageName());
            tv = (TextView)findViewById(id);
            tv_RegistrosNum.add(tv);

            id = getResources().getIdentifier(String.format("tv_numRegistroFecha%s", i), "id", getPackageName());
            tv = (TextView)findViewById(id);
            tv_RegistrosFechas.add(tv);

            id = getResources().getIdentifier(String.format("tv_numRegistroMts%s", i), "id", getPackageName());
            tv= (TextView)findViewById(id);
            tv_RegistrosMts.add(tv);
        }

    }



}



