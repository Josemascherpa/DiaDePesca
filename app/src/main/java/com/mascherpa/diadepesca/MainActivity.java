package com.mascherpa.diadepesca;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.mascherpa.diadepesca.UI.ManagerUIMain;
import com.mascherpa.diadepesca.data.Rio;
import com.mascherpa.diadepesca.databinding.MainBinding;
import com.mascherpa.diadepesca.firebase.FirebaseManager;
import com.mascherpa.diadepesca.load.Loading;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Rio> _Rios;
    String emailUser;
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

        setButtonDeleteAccount();
        setButtonSignOut();
        ChangeName();
        BarWindowBlack();

        //Revisar de hacerlo en el manager ui y lo de abajo
        InitAndCallsManagerUI();



    }
    private void ChangeName(){
        mainActivityBinding.changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseManager.changeNameDatabase(mainActivityBinding.editTextName.getText().toString());
            }
        });
    }
    private void setButtonSignOut(){
        mainActivityBinding.signuout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                firebaseManager.ClearCacheGoogle();
                finish();
                Intent intent = new Intent(getApplicationContext(), Loading.class);
                startActivity(intent);
            }
        });
    }

    private void setButtonDeleteAccount(){
        mainActivityBinding.deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseManager.deleteAccount(FirebaseAuth.getInstance().getUid());
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finishAffinity();
//    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Intent intent = new Intent(this, Loading.class);
//        startActivity(intent);
//    }

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
    private void BarWindowBlack() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(MainActivity.this.getResources().getColor(R.color.black));
    }

    private void InitAndCallsManagerUI(){
        managerUI = new ManagerUIMain(mainActivityBinding,getApplicationContext());
        RecoveryIntentLoading();
        managerUI.AutocompleteFilling();


    }
    private void RecoveryIntentLoading() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            managerUI._Rios = (List<Rio>) bundle.getSerializable("listaRios");
        }
    }








}




