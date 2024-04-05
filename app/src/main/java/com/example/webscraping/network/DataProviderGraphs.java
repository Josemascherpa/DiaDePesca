package com.example.webscraping.network;

import android.util.Log;

import com.example.webscraping.data.Rio;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DataProviderGraphs {
    Rio rio;

    String url = "https://contenidosweb.prefecturanaval.gob.ar";
    public DataProviderGraphs(Rio rio) {
        this.rio = rio;
    }

    public void ObtainHistorialDataGraphs() {

        CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            try {
                Document doc = Jsoup.connect(url+rio.GetLinkDatesGraphs()).get();
                Element tabla = doc.select("table.fpTable").first();
                Elements filas = tabla.select("tbody tr");
                for (int i = 0; i < 10; i++) {
                    //0 numero de colas 1 numero de fecha 2 altuas
                    String date = filas.get(i).child(1).text();
                    if (date.contains("00:00")) {
                        //dates[i] = date.substring(5, 10) + "am";
                    } else {
                        //dates[i] = date.substring(5, 10) + "pm";
                    }
                    //2024-02-16 12:00
                    //floatPoints[i] = Float.valueOf(filas.get(i).child(2).text().substring(0, 4));
                }

                //List<Float> list = Arrays.asList(floatPoints);
                //Collections.reverse(list);
                //floatPoints = (Float[]) list.toArray();

                //List<String> listDates = Arrays.asList(dates);
                //Collections.reverse(listDates);
                //dates = (String[]) listDates.toArray();
                latch.countDown();

            } catch (IOException e) {
                Log.e("Error", e.getMessage() + " " + "ERRORRR");
            }
        }).start();
        try {
            latch.await();


        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Log.i("Error", e.getMessage() + " " + "ERRORRR");
        }

    }
}
