package com.example.webscraping;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.webscraping.data.Rio;
import com.example.webscraping.network.DataProvider;

import java.util.List;

public class Loading extends AppCompatActivity {

    private DataProvider recoveryData;

    TextView versionTV;
    String versionApp = "Version 1.0.0";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadingui);
        recoveryData = new DataProvider("https://contenidosweb.prefecturanaval.gob.ar/alturas/");
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(0xFF000000);

        RecoveryDataRios();
        versionTV = (TextView)findViewById(R.id.version);
        versionTV.setText(versionApp);

    }

    public void RecoveryDataRios(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Rio> data = recoveryData.LoadDataRio();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Actualizar la interfaz de usuario con los datos recuperados
                        if (data != null) {
                            // Aquí puedes hacer lo que necesites con los datos, por ejemplo, mostrarlos en la interfaz de usuario
                        } else {
                            // Manejar el caso de que no se pudieron recuperar los datos
                        }
                    }
                });
            }
        }).start();

    }

}
