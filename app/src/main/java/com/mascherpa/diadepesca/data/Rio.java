package com.mascherpa.diadepesca.data;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;

public class Rio implements Serializable {
    String nombreRio;
    String puerto;
    String altura;
    String variacion;
    String fechaUltimaActualizacion;
    String estado;
    String linkDatesGraphs;
    String url = "https://contenidosweb.prefecturanaval.gob.ar";
    public String[] arrayDates = new String[10];

    public Float[] arrayValues= new Float[10];

    public Rio(String nombreRio,String puerto, String altura, String variacion, String fechaUltimaActualizacion, String estado,String linkDatesGraphs) {
        this.nombreRio = "RIO "+nombreRio;
        this.puerto = puerto;
        this.altura = altura;
        this.variacion = variacion;
        this.fechaUltimaActualizacion = fechaUltimaActualizacion;
        this.estado = estado;
        this.linkDatesGraphs = linkDatesGraphs;
    }

    public String GetNombre(){
        return nombreRio;
    }
    public String GetPuerto(){
        return puerto;
    }
    public String GetAltura(){
        return altura;
    }
    public String GetVariacion(){
        return variacion;
    }
    public String GetFecha(){
        return fechaUltimaActualizacion;
    }
    public String GetEstado(){
        return estado;
    }
    public String GetLinkDatesGraphs(){
        return linkDatesGraphs;
    }




    public void ScrapperDate(String link) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(!estado.contains("S/E")){
                    try {
                        Document doc = Jsoup.connect(url + link).get();
                        Element tabla = doc.select("table.fpTable").first();
                        Elements filas = tabla.select("tbody tr");

                        for (int i = 0; i < 10; i++) {
                            //0 numero de colas 1 numero de fecha 2 altuas

                            String date = filas.get(i).child(1).text();
                            if (date.contains("00:00")) {
                                arrayDates[i] = date.substring(5, 10) + "am";
                            } else {
                                arrayDates[i] = date.substring(5, 10) + "pm";
                            }
                            String mts = filas.get(i).child(2).text().replaceAll("\\s|Mts", "");
                            arrayValues[i] = (Float.valueOf(mts));

                        }

                    }catch(IOException e){
                        Log.i("Error", e.getMessage() + " " + "ERRORRR");
                    }

                }

            }
        });
        thread.start();
    }



}
