package com.example.webscraping;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    String urlWebRegistros = "https://contenidosweb.prefecturanaval.gob.ar/alturas/?page=historico&tiempo=7&id=240";

    ArrayList<TextView> tv_RegistrosNum = new ArrayList<TextView>();
    ArrayList<TextView> tv_RegistrosFechas = new ArrayList<TextView>();
    ArrayList<TextView> tv_RegistrosMts = new ArrayList<TextView>();

    TextView altura_tv;
    TextView variacion_tv;
    TextView fecha_tv;
    ImageView flecha_iv;
    GraphView graph;





    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        IdsTextViewsRio();
        ObtainTvTableRegisters();
        ObtainDatesRegisters();
        ActualizarUI(bundle);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.getGridLabelRenderer().setNumHorizontalLabels(20);
        graph.addSeries(series);


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
        flecha_iv =(ImageView)findViewById(R.id.f_variacion);
        graph = (GraphView) findViewById(R.id.graphview);
        altura_tv = (TextView) findViewById(R.id.tv_altura);
        variacion_tv = (TextView) findViewById(R.id.tv_variacion);
        fecha_tv = (TextView) findViewById(R.id.tv_fechaUltimoRegistro);

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



