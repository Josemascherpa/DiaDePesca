package com.mascherpa.diadepesca;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.mascherpa.diadepesca.UI.ManagerUIMain;
import com.mascherpa.diadepesca.data.Rio;
import com.mascherpa.diadepesca.databinding.MainBinding;
import com.mascherpa.diadepesca.firebase.FirebaseManager;
import com.mascherpa.diadepesca.load.Loading;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ManagerUIMain managerUI;
    private MainBinding mainActivityBinding;
    private FirebaseManager firebaseManager;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivityBinding = MainBinding.inflate(getLayoutInflater());
        setContentView(mainActivityBinding.getRoot());
        firebaseManager = new FirebaseManager(getApplicationContext(),getString(R.string.client_id),mainActivityBinding);
        barWindowBlack();
        initAndCallsManagerUI();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = new Intent(this, Loading.class);
        startActivity(intent);
    }

    //ZOOM FIXED
    @Override
    protected void attachBaseContext(Context newBase) {//ajusto config de densiidad de pantalla y la escala de la fuente, por si el telefono la tiene editada y se vea mal
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
    private void barWindowBlack() {//Pinto de negro la barra de notif
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(MainActivity.this.getResources().getColor(R.color.black));
    }

    private void initAndCallsManagerUI(){//Inicializo manager ui y relleno el buscador
        managerUI = new ManagerUIMain(mainActivityBinding,getApplicationContext(),firebaseManager);
        recoveryIntentLoading();
        managerUI.autocompleteFilling();
    }
    private void recoveryIntentLoading() {//Obtengo datos traidos de la anterior activity
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            managerUI._Rios = (List<Rio>) bundle.getSerializable("listaRios");
        }
    }

}




