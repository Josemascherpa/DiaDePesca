package com.mascherpa.diadepesca.network;

import android.util.Log;

import com.mascherpa.diadepesca.data.Rio;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataProvider {
    String url;

    public DataProvider(String url) {
        this.url = url;
    }

    public List<Rio> loadDataRio() {

        List<Rio> listRios = new ArrayList<Rio>();
        try {//Screapeo la url, buscando filas, recorriendo las celdas con un for y guardando los datos para crear dps un objeto rio y agregarlos a la lista y retornarla para ser usada
            Document doc = Jsoup.connect(url).get();
            Elements filas = doc.select("tbody tr");
            for (Element fila : filas) {
                // Agarro las celdas de las filas
                Elements celdas = fila.select("th,td");

                String puerto = "", rio = "", ultimoRegistro = "", variacion = "", fechaHora = "", alturaAnterior="";
                for (int i = 0; i < celdas.size(); i++) {
                    String contenido = celdas.get(i).text();
                    //voy corriendo la columna para ver cada una
                    switch (i) {
                        case 0:  // Columna "Puerto"
                            puerto = contenido;
                            break;
                        case 1:  // Columna "Río"
                            rio = contenido;
                            break;
                        case 2:  // Columna "Último registro"
                            ultimoRegistro = contenido;
                            break;
                        case 3:  // Columna "Variación"
                            variacion = contenido;
                            break;
                        case 5:  // Columna "Fecha/Hora"
                            fechaHora = contenido;

                            break;

                        default:
                            break;
                    }
                }
                Rio rioData = new Rio(rio,puerto,ultimoRegistro,variacion,fechaHora);
                listRios.add(rioData);
            }
            return listRios;
        } catch (IOException e) {
            Log.i("Error", e.getMessage() + " " + "ERRORRR");
            return null;
        }

    }





}