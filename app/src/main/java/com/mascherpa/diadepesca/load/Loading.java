package com.mascherpa.diadepesca.load;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.mascherpa.diadepesca.UI.ManagerUILoading;
import com.mascherpa.diadepesca.data.Rio;
import com.mascherpa.diadepesca.databinding.LoadinguiBinding;
import com.mascherpa.diadepesca.network.CheckInternet;
import com.mascherpa.diadepesca.network.DataProvider;

import java.io.Serializable;
import java.util.List;

public class Loading extends AppCompatActivity {

    private DataProvider recoveryData;

    TextView versionTV;
    String versionApp = "Version 1.0.0";
    CheckInternet checkInternet = new CheckInternet();

    Button comenzarAventurabtn;

    Button ingresarBtn;
    private LoadinguiBinding binding;

    private ManagerUILoading managerUI;


    //Firebase

    private FirebaseAuth mAuth;
    String email;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = LoadinguiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managerUI = new ManagerUILoading(binding, this);
        managerUI.ClickButtonRegister(binding.comenzaraventura);
        managerUI.ClickButtonLogin(binding.ingresar);

        BarBackgroundsBlack();

        mAuth = FirebaseAuth.getInstance();


        // Set OnClickListener for login button
        binding.loginGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = binding.emailLogin.getEditText().getText().toString();

                if (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    // Registrar al usuario utilizando el método definido anteriormente
                    registerUserWithEmailAndPassword(email);
                } else {
                    // Mostrar un mensaje de error si el email es inválido
                    showMessage("Enter a valid email address");
                }


            }
        });



//        recoveryData = new DataProvider("https://contenidosweb.prefecturanaval.gob.ar/alturas/");

//        if( checkInternet.isOnline()){
//            RecoveryDataRios();
//        }else{
//            Toast.makeText(this,"no hay internet",Toast.LENGTH_LONG).show();
//        }

    }

    private void registerUserWithEmailAndPassword(String email) {
        mAuth.sendSignInLinkToEmail(email, createSignInEmailLinkSettings())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Guardar el email en SharedPreferences o en otro lugar seguro
                            // El usuario debe abrir su correo electrónico y hacer clic en el enlace de registro para completar el proceso de registro
                            // Aquí puedes mostrar un mensaje al usuario para que revise su correo electrónico
                            showMessage("Check your email for the registration link");
                        } else {
                            // Registro fallido
                            Exception e = task.getException();
                            if (e != null) {
                                // Obtener el mensaje de error
                                String errorMessage = e.getMessage();
                                // Mostrar el mensaje de error
                                showMessage("Failed to send registration email: " + errorMessage);
                                Log.i("hola",errorMessage);
                            } else {
                                // Si no hay excepción, mostrar un mensaje genérico
                                showMessage("Failed to send registration email");
                            }
                        }
                    }
                });
    }

    private ActionCodeSettings createSignInEmailLinkSettings() {
        return ActionCodeSettings.newBuilder()
                .setUrl("https://diadepesca.page.link/authenticationApp")
                .setHandleCodeInApp(true)
                .setAndroidPackageName(
                        "com.mascherpa.diadepesca", /* ID del paquete de tu aplicación */
                        true, /* Instalar la aplicación si no está instalada */
                        "12" /* Mínimo de versión de la aplicación requerida */)
                .build();
    }

















    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }



    private void BarBackgroundsBlack(){
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(0xFF000000);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (managerUI.ReturnStateBottomSheetsSignUp()== BottomSheetBehavior.STATE_EXPANDED) {

                Rect outRect = new Rect();
                binding.standardBottomSheet.getGlobalVisibleRect(outRect);

                if(!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    managerUI.GetBottomSheetsSign().setState(BottomSheetBehavior.STATE_COLLAPSED);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }
            }

            if (managerUI.ReturnStateBottomSheetsLogin()== BottomSheetBehavior.STATE_EXPANDED) {
                Rect outRect = new Rect();
                binding.loginBottomSheets.getGlobalVisibleRect(outRect);

                if(!outRect.contains((int)event.getRawX(), (int)event.getRawY())){
                    managerUI.GetBottomSheetsLogin().setState(BottomSheetBehavior.STATE_COLLAPSED);

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }



            }

        }

        return super.dispatchTouchEvent(event);
    }


    public void RecoveryDataRios(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Rio> data = recoveryData.LoadDataRio();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Class MainActivity = com.mascherpa.diadepesca.MainActivity.class;
                        Intent intent = new Intent(Loading.this,MainActivity);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("listaRios",(Serializable)data);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }
                });
            }
        }).start();


    }

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








}

