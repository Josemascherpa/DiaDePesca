package com.example.webscraping;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String urlWebRegistros = "https://contenidosweb.prefecturanaval.gob.ar/alturas/?page=historico&tiempo=7&id=240";

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
        ObtainDatesRegisters();
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
        for (int i=0;i<10;i++){
            int id = getResources().getIdentifier(String.format("tv_numRegistro%s", i), "id", getPackageName());
            TextView tv = (TextView)findViewById(id);
            tv_RegistrosNum.add(tv);

            int id1 = getResources().getIdentifier(String.format("tv_numRegistroFecha%s", i), "id", getPackageName());
            TextView tv1 = (TextView)findViewById(id1);
            tv_RegistrosFechas.add(tv1);

            int id2 = getResources().getIdentifier(String.format("tv_numRegistroMts%s", i), "id", getPackageName());
            TextView tv2= (TextView)findViewById(id2);
            tv_RegistrosMts.add(tv2);
        }

    }

    private void ObtainDatesRegisters(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(urlWebRegistros).get();
                    Element tabla = doc.select("table.fpTable").first();
                    Elements filas = tabla.select("tbody tr");
                    int max = Math.min(10, filas.size());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for(int i=0; i < max; i++) {
                                tv_RegistrosNum.get(i).setText(filas.get(i).child(0).text());
                                String fecha = filas.get(i).child(1).text().substring(5);//Giro de fecha, DD-MM
                                String twoFirstChar = fecha.substring(0,2);
                                String twoSecondChar = fecha.substring(3,5);
                                String time = fecha.substring(5,11);
                                tv_RegistrosFechas.get(i).setText(twoSecondChar+"-"+twoFirstChar+time);
                                tv_RegistrosMts.get(i).setText(filas.get(i).child(2).text());
                            }

                        }
                    });
                } catch (IOException e) {
                    Log.i("Error", e.getMessage() + " "+"ERRORRR");
                }
            }
        }).start();
    }



}



