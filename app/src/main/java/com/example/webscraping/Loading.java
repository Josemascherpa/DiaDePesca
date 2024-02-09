package com.example.webscraping;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Loading extends AppCompatActivity {
    String urlWeb = "https://contenidosweb.prefecturanaval.gob.ar/alturas/";
    TextView versionTV;
    String versionApp = "Version 0.5.4";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadingui);
        LoadDataRio();
        versionTV = (TextView)findViewById(R.id.version);
        versionTV.setText(versionApp);

    }

    private void LoadDataWind(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect("https://www.windguru.cz/105315").get();
                    Element td = doc.getElementsByClass("tcell day1").first();




                } catch (IOException e) {
                    Log.i("Error", e.getMessage() + " "+"ERRORRR");
                }
            }

        }).start();
    }


    public void LoadDataRio(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(urlWeb).get();
                    Element valor = doc.getElementsByClass("SANTA FE").first();
                    Element row = doc.select("tr:contains(SANTA FE):not(:contains(San Javier))").first();
                    String location = row.select("[data-label=Puerto:]").text();
                    String rio = row.select("[data-label=Río:]").text(); // Paraná
                    String altura = row.select("[data-label=Ultimo Registro:]").text(); // 4,00
                    String variacion = row.select("[data-label=Variacion]").text(); // -0,05
                    String periodo = row.select("[data-label=Periodo:]").text();
                    String fecha = row.select("[data-label=Fecha Hora:]").text();
                    String estado = row.select("[data-label=Estado:]").text();
                    String variacionAnterior = row.select("[data-label=Registro Anterior:]").text();
                    String fechaAnterior = row.select("[data-label=Fecha Anterior:]").text();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Class MainActivity = com.example.webscraping.MainActivity.class;
                            Intent intent = new Intent(Loading.this,MainActivity);
                            Bundle bundle = new Bundle();
                            bundle.putString("location", location);
                            bundle.putString("rio", rio);
                            bundle.putString("altura", altura);
                            bundle.putString("variacion", variacion);
                            bundle.putString("periodo", periodo);
                            bundle.putString("fecha", fecha);
                            bundle.putString("estado", estado);
                            bundle.putString("variacionAnterior", variacionAnterior);
                            bundle.putString("fechaAnterior", fechaAnterior);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }
                    });

                } catch (IOException e) {
                    Log.i("Error", e.getMessage() + " "+"ERRORRR");
                }
            }

        }).start();
    }

}
