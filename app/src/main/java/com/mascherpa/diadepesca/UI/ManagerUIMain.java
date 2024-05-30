package com.mascherpa.diadepesca.UI;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.mascherpa.diadepesca.CustomAutocompleteEditText.CustomAutoCompleteAdapter;
import com.mascherpa.diadepesca.FavouriteRio.MySharedPreferences;
import com.mascherpa.diadepesca.data.Rio;
import com.mascherpa.diadepesca.databinding.MainBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ManagerUIMain {
    Integer rioSave = null;
    private MainBinding binding;
    public List<Rio> _Rios = new ArrayList<>();
    private Context contextMain;

    public ManagerUIMain(MainBinding bindingMain, Context context){
        this.binding = bindingMain;
        contextMain=context;
    }
    public void AutocompleteFilling() {
        List<String> arrayNombreRios = new ArrayList<>();
        for (int i = 0; i < _Rios.size(); i++) {
            String nombreAutocompletado = _Rios.get(i).GetNombre() + " " + _Rios.get(i).GetPuerto();
            arrayNombreRios.add(nombreAutocompletado);
        }
        CustomAutoCompleteAdapter adapter = new CustomAutoCompleteAdapter(contextMain, arrayNombreRios);
        binding.buscaRio.setAdapter(adapter);
        binding.buscaRio.setThreshold(1); // Configura el número mínimo de caracteres antes de que se muestren sugerencias
        EditUIWithAutocomplete();

    }

    private void EditUIWithAutocomplete() {
        //If I haven't saved anything yet, the integer won't exist, which means GetRioFavs() will return 0, but since a river exists with 0, I set it to return 9999.
        /*if(MySharedPreferences.getRioFavs(this)!=9999){
            favButton.setVisibility(View.VISIBLE);
            for(int i=0;i<_Rios.size();i++){
                if(i==MySharedPreferences.getRioFavs(this)){
                    VisibilityDates();
                    Rio rio = _Rios.get(i);
                    if(!rio.GetPuerto().contains("PUERTO RUIZ")){
                        rio.ScrapperDate(rio.GetLinkDatesGraphs());
                    }

                    altura_tv.setText(rio.GetAltura());
                    variacion_tv.setText(rio.GetVariacion() + " Mts");
                    fecha_tv.setText(SortedDateTV(rio.GetFecha()));
                    nombreRio.setText(rio.GetNombre() + "(" + rio.GetPuerto() + ")");

                    if(!rio.GetEstado().contains("S/E")){
                        DirectionAndColorArrow();

                        Timer timer = new Timer();
                        // Programar la tarea para que se ejecute después de 5 segundos
                        timer.schedule(new TimerTask() {
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        if(!rio.GetPuerto().contains("PUERTO RUIZ")){
                                            List<Float> listFloatPoints = Arrays.asList(rio.arrayValues);
                                            Collections.reverse(listFloatPoints);
                                            rio.arrayValues = (Float[]) listFloatPoints.toArray();

                                            List<String> listDates = Arrays.asList(rio.arrayDates);
                                            Collections.reverse(listDates);
                                            rio.arrayDates = (String[])listDates.toArray();

                                            CreateGraphs(rio.arrayValues, rio.arrayDates);
                                            favButton.setProgress(favButton.getDuration());

                                        }
                                    }
                                });
                            }
                        }, 1500);
                    }else{
                        EmptyDates();
                    }


                }
            }
        }*/

        //Set the UI for when a river is selected from the Spinner.
        binding.buscaRio.setOnItemClickListener((parent, view, position, id) -> {
            String rioSeleccionado = (String) parent.getItemAtPosition(position);
//            favButton.setVisibility(View.VISIBLE);
            for (int i = 0; i < _Rios.size(); i++) {
                if (((_Rios.get(i).GetNombre()+ _Rios.get(i).GetPuerto()).replace(" ", "")).equals((rioSeleccionado.replace(" ", "")))) {
//                    VisibilityDates();
//                    if(i!=MySharedPreferences.getRioFavs(this)){
//
//                        favButton.setProgress(0);
//
//                    }else{
//                        favButton.setProgress(favButton.getDuration());
//                    }

                    rioSave = i;
                    Rio rio = _Rios.get(i);
                    if(!rio.GetPuerto().contains("PUERTO RUIZ")){
                        rio.ScrapperDate(rio.GetLinkDatesGraphs());
                    }
                    binding.alturaRio.setText(rio.GetAltura());
                    binding.variacionRio.setText(rio.GetVariacion() + " Mts");
                    binding.fechaTv.setText(SortedDateTV(rio.GetFecha()));
//                    nombreRio.setText(rio.GetNombre() + "(" + rio.GetPuerto() + ")");

                    /*if(!rio.GetEstado().contains("S/E")){
                        DirectionAndColorArrow();

                        Timer timer = new Timer();

                        // Programar la tarea para que se ejecute después de 5 segundos
                        timer.schedule(new TimerTask() {
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        if(!rio.GetPuerto().contains("PUERTO RUIZ")){
                                            List<Float> listFloatPoints = Arrays.asList(rio.arrayValues);
                                            Collections.reverse(listFloatPoints);
                                            rio.arrayValues = (Float[]) listFloatPoints.toArray();

                                            List<String> listDates = Arrays.asList(rio.arrayDates);
                                            Collections.reverse(listDates);
                                            rio.arrayDates = (String[])listDates.toArray();

                                            CreateGraphs(rio.arrayValues, rio.arrayDates);
                                        }
                                    }
                                });
                            }
                        }, 1500);
                    }else{
                        EmptyDates();
                    }*/

                }
            }

            //Clear focus and clear keyboard
            binding.buscaRio.clearFocus();
            InputMethodManager imm = (InputMethodManager) contextMain.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.buscaRio.getWindowToken(), 0);

        });
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
