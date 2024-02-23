package com.example.webscraping;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

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
    String[] dates = new String[10];

    private XYPlot plot;

    ImageView compartirAltura;




    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        BarWindowBlack();
        IdsTextViewsRio();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ActualizarUI(bundle);
        ButtonSharedFriends();
        ObtainDatesRegisters();

    }


    @Override
    protected void onResume() {
        super.onResume();
        IdsTextViewsRio();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ActualizarUI(bundle);
        ButtonSharedFriends();
        ObtainDatesRegisters();

    }

    private void ActualizarUI(Bundle bundle){
        // actualizar UI
        altura_tv.setText(bundle.getString("altura"));
        variacion_tv.setText(bundle.getString("variacion")+ " Mts");
        String fecha = bundle.getString("fecha").replace(" ","");
        String hora = fecha.substring(fecha.length()-4);
        hora = hora.substring(0,2)+":"+hora.substring(2);
        fecha = fecha.substring(0,fecha.length()-4)+hora;
        fecha = fecha.replaceAll("/24-", " ");
        fecha = fecha.replace("/","-");
        if(fecha.contains("12:00")){
            fecha=fecha+" pm";
        }else{
            fecha=fecha+" am";
        }
        fecha_tv.setText(fecha);

        parseFloat();

    }
    private void IdsTextViewsRio(){
        plot = (XYPlot) findViewById(R.id.plot);
        flecha_iv =(ImageView)findViewById(R.id.f_variacion);
        altura_tv = (TextView) findViewById(R.id.tv_altura);
        variacion_tv = (TextView) findViewById(R.id.tv_variacion);
        fecha_tv = (TextView) findViewById(R.id.tv_fechaUltimoRegistro);
        compartirAltura = (ImageView) findViewById(R.id.compartir_altura);
    }



    private void ObtainDatesRegisters(){
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(()->{
            try{
                Document doc = Jsoup.connect(urlWebRegistros).get();
                Element tabla = doc.select("table.fpTable").first();
                Elements filas = tabla.select("tbody tr");
                for(int i=0; i < 10; i++) {
                    //0 numero de colas 1 numero de fecha 2 altuas
                    String date = filas.get(i).child(1).text();
                    if(date.contains("00:00")){
                        dates[i]  = date.substring(5,10)+"am";
                    }else{
                        dates[i]  = date.substring(5,10)+"pm";
                    }
                    //2024-02-16 12:00
                    floatPoints[i] = Float.valueOf(filas.get(i).child(2).text().substring(0,4));
                }

                List<Float> list = Arrays.asList(floatPoints);
                Collections.reverse(list);
                floatPoints = (Float[]) list.toArray();
                latch.countDown();

            }catch (IOException e){
                Log.e("Error", e.getMessage() + " "+"ERRORRR");
            }
        }).start();
        try {
             latch.await();
             CreateGraphs(floatPoints,dates);
        } catch (InterruptedException e ) {
            Thread.currentThread().interrupt();
            Log.i("Error", e.getMessage() + " "+"ERRORRR");
        }
    }


    private void CreateGraphs(Float[] flpoints, String[] fechaBottom){

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
        plot.setRangeBoundaries(0, BoundaryMode.FIXED, 10, BoundaryMode.FIXED);
        plot.setDomainBoundaries(0,9.5,BoundaryMode.FIXED);
        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setRotation(10);
        plot.getGraph().getLineLabelInsets().setBottom(PixelUtils.dpToPix(4));

        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object object, StringBuffer buffer, FieldPosition field) {
                int index = ((Number) object).intValue();
                return buffer.append(fechaBottom[index]);
            }
            @Override
            public Object parseObject(String string, ParsePosition position) {
                return null;
            }

        });

        //modificaciones titulo
        plot.setTitle("Historial Alturas");
        plot.getTitle().setMarginTop(55f);
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

    private void parseFloat(){
        if(Float.parseFloat(variacion_tv.getText().toString().substring(0,5))<0){
            variacion_tv.setTextColor(Color.parseColor("#D24545"));
            flecha_iv.setImageResource(R.drawable.fbajando);
        }else{
            variacion_tv.setTextColor(Color.parseColor("#557C55"));
            flecha_iv.setImageResource(R.drawable.fsubiendo);
        }
    }

    private void ButtonSharedFriends(){
        compartirAltura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String infoEnviar = "Rio Parana(Santa Fe)\n"+"Altura Actual:0. "+altura_tv.getText()+"Mts"+" \nVariacion: "+variacion_tv.getText()+" \nUltima Actualizacion: "+fecha_tv.getText();
                sendIntent.putExtra(Intent.EXTRA_TEXT,infoEnviar);
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent,null);
                startActivity(shareIntent);
            }
        });
    }

    private void BarWindowBlack(){
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(MainActivity.this.getResources().getColor(R.color.black));
    }






}




