package com.example.webscraping;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class Loading extends AppCompatActivity {
    String urlWeb = "https://contenidosweb.prefecturanaval.gob.ar/alturas/";

    TextView versionTV;
    String versionApp = "Version 1.0.0";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadingui);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(0xFF000000);

        LoadDataRio();
        versionTV = (TextView)findViewById(R.id.version);
        versionTV.setText(versionApp);

    }

    public void LoadDataRio(){
        new Thread(() -> {
            try {
                Document doc = Jsoup.connect(urlWeb).get();
                Element row = doc.select("tr:contains(SANTA FE):not(:contains(San Javier))").first();
                String altura = row.select("[data-label=Ultimo Registro:]").text(); // 4,00
                String variacion = row.select("[data-label=Variacion]").text(); // -0,05
                String periodo = row.select("[data-label=Periodo:]").text();
                String fecha = row.select("[data-label=Fecha Hora:]").text();
                String estado = row.select("[data-label=Estado:]").text();
                String variacionAnterior = row.select("[data-label=Registro Anterior:]").text();
                String fechaAnterior = row.select("[data-label=Fecha Anterior:]").text();

                runOnUiThread(() -> {

                    Class MainActivity = com.example.webscraping.MainActivity.class;
                    Intent intent = new Intent(Loading.this,MainActivity);
                    Bundle bundle = new Bundle();
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
                });

            } catch (IOException e) {
                Log.i("Error", e.getMessage() + " "+"ERRORRR");
            }
        }).start();
    }

}
