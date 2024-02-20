package com.example.webscraping;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String urlWebRegistros = "https://contenidosweb.prefecturanaval.gob.ar/alturas/?page=historico&tiempo=7&id=240";

    ArrayList<String> arrayDate = new ArrayList<String>();
    ArrayList<TextView> tv_RegistrosNum = new ArrayList<TextView>();
    ArrayList<TextView> tv_RegistrosFechas = new ArrayList<TextView>();
    ArrayList<TextView> tv_RegistrosMts = new ArrayList<TextView>();

    TextView altura_tv;
    TextView variacion_tv;
    TextView fecha_tv;
    TextView muestraAltura;
    ImageView flecha_iv;

    String arraysFecha[] = new String[10];
    Float arraysAlturas[] = new Float[10];
    Float[] floatPoints = new Float[10];

    private XYPlot plot;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        IdsTextViewsRio();
        ActualizarUI(bundle);

        ObtainDatesRegisters();

    }


    @Override
    protected void onResume() {
        super.onResume();
        //Intent intent = getIntent();
        //Bundle bundle = intent.getExtras();
        //altura_tv = (TextView) findViewById(R.id.tv_altura);
        //variacion_tv = (TextView) findViewById(R.id.tv_variacion);
        //fecha_tv = (TextView) findViewById(R.id.tv_fechaUltimoRegistro);
        //ActualizarUI(bundle);

    }

    private void ActualizarUI(Bundle bundle){
        // actualizar UI

        altura_tv.setText(bundle.getString("altura"));
        variacion_tv.setText(bundle.getString("variacion"));


        String fecha = bundle.getString("fecha").replace(" ","");
        String hora = fecha.substring(fecha.length()-4);
        hora = hora.substring(0,2)+":"+hora.substring(2);
        fecha = fecha.substring(0,fecha.length()-4)+hora;
        fecha_tv.setText(fecha);

        if(Float.parseFloat(variacion_tv.getText().toString())<0){
            variacion_tv.setTextColor(Color.parseColor("#D24545"));
            flecha_iv.setImageResource(R.drawable.fbajando);
        }else{
            variacion_tv.setTextColor(Color.parseColor("#557C55"));
            flecha_iv.setImageResource(R.drawable.fsubiendo);
        }
    }
    private void IdsTextViewsRio(){
        plot = (XYPlot) findViewById(R.id.plot);
        flecha_iv =(ImageView)findViewById(R.id.f_variacion);
        altura_tv = (TextView) findViewById(R.id.tv_altura);
        variacion_tv = (TextView) findViewById(R.id.tv_variacion);
        fecha_tv = (TextView) findViewById(R.id.tv_fechaUltimoRegistro);
        muestraAltura = (TextView) findViewById(R.id.muestraAltura);

    }



    private void ObtainDatesRegisters(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(urlWebRegistros).get();
                    Element tabla = doc.select("table.fpTable").first();
                    Elements filas = tabla.select("tbody tr");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for(int i=0; i < 10; i++) {
                                //0 numero de colas 1 numero de fecha 2 altuas
                                floatPoints[i] = Float.valueOf(filas.get(i).child(2).text().substring(0,4));
                            }

                            List<Float> list = Arrays.asList(floatPoints);
                            Collections.reverse(list);
                            floatPoints = (Float[]) list.toArray();
                            CreateGraphs(floatPoints);
                        }
                    });
                } catch (IOException e) {
                    Log.i("Error", e.getMessage() + " "+"ERRORRR");
                }
            }
        }).start();
    }

    private void CreateGraphs(Float[] flpoints){
        //puntos en el grafico
        XYSeries s1=new SimpleXYSeries(SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,"",flpoints);
        //tamaño etiqueta en los puntos
        PointLabelFormatter plf = new PointLabelFormatter();
        plf.getTextPaint().setTextSize(PixelUtils.spToPix(12));
        //Seteo de lineas colores y puntos e colores
        LineAndPointFormatter format = new LineAndPointFormatter(Color.rgb(16,157,249),Color.rgb(255,255,255),null,null);
        format.setPointLabelFormatter(plf);
        format.getLinePaint().setStrokeWidth(3);

        //los agrego al grafico
        plot.addSeries(s1,format);
        //seteo de rangos de etiquetas
        plot.setRangeBoundaries(0, BoundaryMode.FIXED, 7, BoundaryMode.FIXED);
        plot.setDomainBoundaries(0,10,BoundaryMode.FIXED);

        //modificaciones titulo
        plot.setTitle("Historial Alturas");
        plot.getTitle().getLabelPaint().setTextSize( PixelUtils.spToPix(13));
        plot.getTitle().getLabelPaint().setTypeface(Typeface.DEFAULT_BOLD);

        //lineas del grafico y transparencia en grilla
        plot.getGraph().getDomainOriginLinePaint().setColor(Color.rgb(255,255,255));
        plot.getGraph().getDomainOriginLinePaint().setStrokeWidth(1.5f);
        plot.getGraph().getRangeOriginLinePaint().setColor(Color.rgb(255,255,255));
        plot.getGraph().getRangeOriginLinePaint().setStrokeWidth(1.5f);
        plot.getGraph().setRangeGridLinePaint(null);
        plot.getGraph().setDomainGridLinePaint(null);
    }




}




