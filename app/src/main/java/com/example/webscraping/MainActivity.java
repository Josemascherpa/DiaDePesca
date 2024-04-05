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
import com.example.webscraping.CustomAutocompleteEditText.CustomAutoCompleteAdapter;
import com.example.webscraping.data.Rio;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    TextView altura_tv;
    TextView variacion_tv;
    TextView fecha_tv;
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
    List<Rio> _Rios = new ArrayList<>();


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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            final DisplayMetrics displayMetrics = newBase.getResources().getDisplayMetrics();
            if (displayMetrics.densityDpi != DisplayMetrics.DENSITY_DEVICE_STABLE) {
                // Current density is different from Default Density. Override it
                configuration.densityDpi = DisplayMetrics.DENSITY_DEVICE_STABLE;
            }
        }
        configuration.fontScale = 1.0f;
        Context newContext = newBase.createConfigurationContext(configuration);
        super.attachBaseContext(newContext);
    }


    private void ActualizarUI(Bundle bundle) {
        // actualizar UI

    }

    private void IdsTextViewsRio() {
        plot = (XYPlot) findViewById(R.id.plot);
        flecha_iv = (ImageView) findViewById(R.id.f_variacion);
        altura_tv = (TextView) findViewById(R.id.tv_altura);
        variacion_tv = (TextView) findViewById(R.id.tv_variacion);
        fecha_tv = (TextView) findViewById(R.id.tv_fechaUltimoRegistro);
        compartirAltura = (ImageView) findViewById(R.id.compartir_altura);
        buscaRios_ATV = (AutoCompleteTextView) findViewById(R.id.ac_tv);
        nombreRio = (TextView) findViewById(R.id.tv_NombreRio);

    }

    private void CreateGraphs(Float[] flpoints, String[] fechaBottom) {
        plot.clear();
        //puntos en el grafico
        XYSeries s1 = new SimpleXYSeries(SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "", flpoints);

        //tamaño etiqueta en los puntos
        PointLabelFormatter plf = new PointLabelFormatter();
        plf.getTextPaint().setTextSize(PixelUtils.spToPix(12));

        //Seteo de lineas colores y puntos e colores
        LineAndPointFormatter format = new LineAndPointFormatter(Color.rgb(16, 157, 249), Color.rgb(255, 255, 255), null, null);
        format.setPointLabelFormatter(plf);
        format.getLinePaint().setStrokeWidth(3);

        //los agrego al grafico
        plot.addSeries(s1, format);

        //seteo de rangos de etiquetas
        plot.setRangeBoundaries(0, BoundaryMode.FIXED, 10, BoundaryMode.FIXED);
        plot.setDomainBoundaries(0, 9.5, BoundaryMode.FIXED);
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
        plot.getTitle().getLabelPaint().setTextSize(PixelUtils.spToPix(13));
        plot.getTitle().getLabelPaint().setTypeface(Typeface.DEFAULT_BOLD);

        //lineas del grafico y transparencia en grilla
        plot.getGraph().getDomainOriginLinePaint().setColor(Color.rgb(255, 255, 255));
        plot.getGraph().getDomainOriginLinePaint().setStrokeWidth(1.5f);
        plot.getGraph().getRangeOriginLinePaint().setColor(Color.rgb(255, 255, 255));
        plot.getGraph().getRangeOriginLinePaint().setStrokeWidth(1.5f);
        plot.getGraph().setRangeGridLinePaint(null);
        plot.getGraph().setDomainGridLinePaint(null);

        plot.redraw();


    }


    private void ButtonSharedFriends() {
        compartirAltura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String infoEnviar = "Rio Parana(Santa Fe)\n" + "Altura Actual: " + altura_tv.getText() + "Mts" + " \nVariacion: " + variacion_tv.getText() + " \nUltima Actualizacion: " + fecha_tv.getText();
                sendIntent.putExtra(Intent.EXTRA_TEXT, infoEnviar);
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });

    }

    private void BarWindowBlack() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(MainActivity.this.getResources().getColor(R.color.black));
    }

    private void RecoveryIntentLoading() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            _Rios = (List<Rio>) bundle.getSerializable("listaRios");
        }
    }

    private void AutocompleteFilling() {
        List<String> arrayNombreRios = new ArrayList<>();
        for (int i = 0; i < _Rios.size(); i++) {
            String nombreAutocompletado = _Rios.get(i).GetNombre() + " " + _Rios.get(i).GetPuerto();
            arrayNombreRios.add(nombreAutocompletado);
        }
        CustomAutoCompleteAdapter adapter = new CustomAutoCompleteAdapter(this, arrayNombreRios);
        buscaRios_ATV.setAdapter(adapter);
        buscaRios_ATV.setThreshold(1); // Configura el número mínimo de caracteres antes de que se muestren sugerencias
    }

    private void EditUIWithAutocomplete() {
        buscaRios_ATV.setOnItemClickListener((parent, view, position, id) -> {
            String rioSeleccionado = (String) parent.getItemAtPosition(position);
            for (int i = 0; i < _Rios.size(); i++) {
                if (((_Rios.get(i).GetNombre()+ _Rios.get(i).GetPuerto()).replace(" ", "")).equals((rioSeleccionado.replace(" ", "")))) {
                    Rio rio = _Rios.get(i);
                    rio.ScrapperDate(rio.GetLinkDatesGraphs());
                    altura_tv.setText(rio.GetAltura());
                    variacion_tv.setText(rio.GetVariacion() + " Mts");
                    fecha_tv.setText(SortedDateTV(rio.GetFecha()));
                    nombreRio.setText(rio.GetNombre() + "(" + rio.GetPuerto() + ")");
                    DirectionAndColorArrow();

                    Timer timer = new Timer();

                    // Programar la tarea para que se ejecute después de 5 segundos
                    timer.schedule(new TimerTask() {
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    CreateGraphs(rio.arrayValues, rio.arrayDates);
                                }
                            });
                        }
                    }, 1000);
                }
            }
            buscaRios_ATV.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(buscaRios_ATV.getWindowToken(), 0);
        });
    }

    private void DirectionAndColorArrow(){
        if(!variacion_tv.getText().toString().equals("- Mts") && Float.parseFloat(variacion_tv.getText().toString().substring(0,5))<0){
            variacion_tv.setTextColor(Color.parseColor("#D24545"));
            flecha_iv.setImageResource(R.drawable.fbajando);
            flecha_iv.setImageAlpha(255);
        }else if(!variacion_tv.getText().toString().equals("- Mts") && Float.parseFloat(variacion_tv.getText().toString().substring(0,5))>0){//
            variacion_tv.setTextColor(Color.parseColor("#557C55"));
            flecha_iv.setImageResource(R.drawable.fsubiendo);
            flecha_iv.setImageAlpha(255);
        }else if(variacion_tv.getText().toString().equals("- Mts")){
            variacion_tv.setTextColor(Color.parseColor("#DAD6D6"));
            flecha_iv.setImageAlpha(0);
        }
    }

    private String SortedDateTV(String fecha){
        String fechaReturn = fecha;
        String hora = fechaReturn.substring(fechaReturn.length()-4);
        hora = hora.substring(0,2)+":"+hora.substring(2);
        fechaReturn = fechaReturn.substring(0,fechaReturn.length()-4)+hora;
        fechaReturn = fechaReturn.replaceAll("/24-", " ");
        fechaReturn = fechaReturn.replace("/","-");
        if(fechaReturn.contains("12:00")){
            fechaReturn=fechaReturn+" pm";
        }else{
            fechaReturn=fechaReturn+" am";
        }

        return fechaReturn;
    }


}




