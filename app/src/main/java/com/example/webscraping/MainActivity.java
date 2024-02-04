package com.example.webscraping;

import androidx.appcompat.app.AppCompatActivity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

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