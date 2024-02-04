package com.example.webscraping;

import androidx.appcompat.app.AppCompatActivity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    String urlWeb = "https://contenidosweb.prefecturanaval.gob.ar/alturas/";
    TextView ubicacion_tv;
    TextView altura_tv;
    TextView variacion_tv;
    TextView fecha_tv;
    TextView estado_tv;
    TextView alturaAnt_tv;
    TextView fechaAnt_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ubicacion_tv = (TextView) findViewById(R.id.ubicacion);
        altura_tv = (TextView) findViewById(R.id.altura);
        variacion_tv = (TextView) findViewById(R.id.variacion);
        fecha_tv = (TextView) findViewById(R.id.ultimaActua);
        estado_tv = (TextView) findViewById(R.id.estado);
        alturaAnt_tv = (TextView) findViewById(R.id.altAnterior);
        fechaAnt_tv = (TextView) findViewById(R.id.fechaAnterior);
        LoadData();
    }

    public void LoadData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //PODRIA MOSTRAR UNA PANTALLA CARGANDO Y LUEGO CAMBIAR DE PANTALLA UNA VEZ QUE CARGA

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
                            // actualizar UI
                            ubicacion_tv.setText(location);
                            altura_tv.setText(altura);
                            variacion_tv.setText(variacion);
                            fecha_tv.setText(fecha);
                            estado_tv.setText(estado);
                            alturaAnt_tv.setText(variacionAnterior);
                            fechaAnt_tv.setText(fechaAnterior);

                            if(Float.parseFloat(variacion)<0){
                                variacion_tv.setTextColor(Color.parseColor("#D24545"));
                            }else{
                                variacion_tv.setTextColor(Color.parseColor("#557C55"));
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