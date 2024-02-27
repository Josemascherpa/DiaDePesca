package com.example.webscraping;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Implementation of App Widget functionality.
 */
public class AlturaRioWidget extends AppWidgetProvider {

    String url="https://contenidosweb.prefecturanaval.gob.ar/alturas/";
    static String alturaNueva;
    static String variacionNueva;
    static String fechaNueva;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        super.onReceive(context, intent);
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), AlturaRioWidget.class.getName());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            LoadDataRio(context,appWidgetManager,appWidgetId);
            Log.i("hola","update");
            RemoteViews remoteV = new RemoteViews(context.getPackageName(), R.layout.altura_rio_widget);
            Intent intentSync = new Intent(context, AlturaRioWidget.class);
            intentSync.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE); //You need to specify the action for the intent. Right now that intent is doing nothing for there is no action to be broadcasted.
            PendingIntent pendingSync = PendingIntent.getBroadcast(context,0, intentSync, PendingIntent.FLAG_UPDATE_CURRENT); //You need to specify a proper flag for the intent. Or else the intent will become deleted.
            remoteV.setOnClickPendingIntent(R.id.refresh,pendingSync);
            appWidgetManager.updateAppWidget(appWidgetId, remoteV);

            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public void LoadDataRio(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(url).get();
                    Element valor = doc.getElementsByClass("SANTA FE").first();
                    Element row = doc.select("tr:contains(SANTA FE):not(:contains(San Javier))").first();
                    alturaNueva= row.select("[data-label=Ultimo Registro:]").text(); // 4,00
                    variacionNueva = row.select("[data-label=Variacion]").text(); // -0,05
                    fechaNueva = row.select("[data-label=Fecha Hora:]").text();
                    Log.i("hola","data");
                    latch.countDown();
                } catch (IOException e) {
                    Log.i("Error", e.getMessage() + " "+"ERRORRR");
                }
            }
        }).start();
        try {
            latch.await();
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.altura_rio_widget);
            views.setTextViewText(R.id.altura_tv, alturaNueva);
            views.setTextViewText(R.id.variacion_tv, variacionNueva);
            String replaceFecha = fechaNueva.replace("/24","");
            String replaceFecha2 = replaceFecha.replace("/","-").replace(" ","");
            String fechaEdit = replaceFecha2.substring(0,9)+":"+replaceFecha2.substring(9,11);
            views.setTextViewText(R.id.fecha_tv, fechaEdit);
            appWidgetManager.updateAppWidget(appWidgetId, views);
            Log.i("hola","seteos");

        } catch (InterruptedException e ) {
            Thread.currentThread().interrupt();
            Log.i("Error", e.getMessage() + " "+"ERRORRR");
        }

    }

}