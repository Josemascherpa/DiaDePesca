package com.example.webscraping;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.example.webscraping.data.Rio;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    String urlWebRegistros = "https://contenidosweb.prefecturanaval.gob.ar/alturas/?page=historico&tiempo=7&id=240";
    TextView altura_tv;
    TextView variacion_tv;
    TextView fecha_tv;
    TextView muestraAltura;
    ImageView flecha_iv;
    AutoCompleteTextView buscaRios_ATV;

    TextView nombreRio;

    String arraysFecha[] = new String[10];
    Float arraysAlturas[] = new Float[10];
    Float[] floatPoints = new Float[10];
    String[] dates = new String[10];

    private XYPlot plot;

    ImageView compartirAltura;


//////////////////////////////////
    List<Rio> _Rios = new ArrayList<Rio>();


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        BarWindowBlack();
        IdsTextViewsRio();
        RecoveryIntentLoading();
        ButtonSharedFriends();

        AutocompleteFilling();
        EditUIWithAutocomplete();

    }
    @Override
    protected void onResume() {
        super.onResume();
        //IdsTextViewsRio();
        //RecoveryIntentLoading();
        //ButtonSharedFriends();
//        ObtainDatesRegisters();
    }

    //ZOOM FIXED
    @Override
    protected void attachBaseContext(Context newBase) {
        final Configuration configuration = newBase.getResources().getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            final DisplayMetrics displayMetrics = newBase.getResources().getDisplayMetrics();
            if (displayMetrics.densityDpi != DisplayMetrics.DENSITY_DEVICE_STABLE)
            {
                // Current density is different from Default Density. Override it
                configuration.densityDpi = DisplayMetrics.DENSITY_DEVICE_STABLE;
            }
        }
        configuration.fontScale = 1.0f;
        Context newContext = newBase.createConfigurationContext(configuration);
        super.attachBaseContext(newContext);
    }


    private void ActualizarUI(Bundle bundle){
        // actualizar UI

    }
    private void IdsTextViewsRio(){
        plot = (XYPlot) findViewById(R.id.plot);
        flecha_iv =(ImageView)findViewById(R.id.f_variacion);
        altura_tv = (TextView) findViewById(R.id.tv_altura);
        variacion_tv = (TextView) findViewById(R.id.tv_variacion);
        fecha_tv = (TextView) findViewById(R.id.tv_fechaUltimoRegistro);
        compartirAltura = (ImageView) findViewById(R.id.compartir_altura);
        buscaRios_ATV = (AutoCompleteTextView) findViewById(R.id.ac_tv);
        nombreRio = (TextView) findViewById(R.id.tv_NombreRio);

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

    private void RecoveryIntentLoading(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            _Rios = (List<Rio>) bundle.getSerializable("listaRios");
        }
    }

    private void AutocompleteFilling(){
        List<String> arrayNombreRios = new ArrayList<>();
        for (int i=0;i<_Rios.size();i++) {
            String nombreAutocompletado = _Rios.get(i).GetNombre()+"("+ _Rios.get(i).GetPuerto()+")";
            arrayNombreRios.add(nombreAutocompletado);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,arrayNombreRios);
        buscaRios_ATV.setAdapter(adapter);
    }

    private void EditUIWithAutocomplete(){
        buscaRios_ATV.setOnItemClickListener((parent, view, position, id) -> {
            String rioSelec = (String)parent.getItemAtPosition(position);
            String puertoRio = rioSelec.substring(rioSelec.indexOf("(")+1,rioSelec.indexOf(")"));
            String ubicacion = null;
            if(puertoRio.contains("(")){
                int indiceParent = puertoRio.indexOf("(");
                ubicacion = puertoRio.substring(indiceParent+1,puertoRio.length());
                ubicacion = "("+ubicacion+")";
                puertoRio = puertoRio.substring(0,indiceParent-1);
            }
//Guardar en una string lo que este entre parentesis para cuando comparo sumarselo
            for(int i=0;i<_Rios.size();i++){
                if(ubicacion!=null){
                    if(_Rios.get(i).GetPuerto().equals(puertoRio+" "+ubicacion)){
                        Rio rioSelecionado = _Rios.get(i);
                        altura_tv.setText(rioSelecionado.GetAltura());
                        variacion_tv.setText(rioSelecionado.GetVariacion()+" Mts");
                        fecha_tv.setText(rioSelecionado.GetFecha());
                        nombreRio.setText(rioSelecionado.GetNombre()+"("+rioSelecionado.GetPuerto()+")");
                    }
                }else{
                    if(_Rios.get(i).GetPuerto().equals(puertoRio)){
                        Rio rioSelecionado = _Rios.get(i);
                        altura_tv.setText(rioSelecionado.GetAltura());
                        variacion_tv.setText(rioSelecionado.GetVariacion()+" Mts");
                        fecha_tv.setText(rioSelecionado.GetFecha());
                        nombreRio.setText(rioSelecionado.GetNombre()+"("+rioSelecionado.GetPuerto()+")");

                    }
                }
            }
            buscaRios_ATV.clearFocus();
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(buscaRios_ATV.getWindowToken(), 0);
        });
    }






}




