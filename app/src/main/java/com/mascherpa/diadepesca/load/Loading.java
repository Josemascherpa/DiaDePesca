package com.mascherpa.diadepesca.load;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
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
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mascherpa.diadepesca.UI.ManagerUILoading;
import com.mascherpa.diadepesca.data.Rio;
import com.mascherpa.diadepesca.databinding.LoadinguiBinding;
import com.mascherpa.diadepesca.network.CheckInternet;
import com.mascherpa.diadepesca.network.DataProvider;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;

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

    private static final int REQUEST_CODE_PERMISSION = 100;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo = null;
    private String email;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = LoadinguiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managerUI = new ManagerUILoading(binding,this);
        managerUI.ClickButtonRegister(binding.comenzaraventura);
        managerUI.ClickButtonLogin(binding.ingresar);

        BarBackgroundsBlack();

        mAuth = FirebaseAuth.getInstance();
        Executor executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = createBiometricPrompt(this, executor);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.USE_BIOMETRIC}, REQUEST_CODE_PERMISSION);
        } else {
            promptInfo = createPromptInfo();
        }

        binding.loginGmail.setOnClickListener(v -> {
            email = binding.emailLogin.getEditText().getText().toString();
            onRegisterButtonClick(v);
        });

//        recoveryData = new DataProvider("https://contenidosweb.prefecturanaval.gob.ar/alturas/");

//        if( checkInternet.isOnline()){
//            RecoveryDataRios();
//        }else{
//            Toast.makeText(this,"no hay internet",Toast.LENGTH_LONG).show();
//        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (biometricPrompt != null && promptInfo != null) {
                    biometricPrompt.authenticate(promptInfo);
                }
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private BiometricPrompt.PromptInfo createPromptInfo() {
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login")
                .setSubtitle("Authenticate using your biometric credential")
                .setNegativeButtonText("Cancel")
                .build();
    }
    private void onRegisterButtonClick(View view) {
        if (biometricPrompt != null && promptInfo != null) {
            biometricPrompt.authenticate(promptInfo);
        }
    }

    private void checkAndRegisterUser() {
        // Verificar si el usuario está registrado en Firebase
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // El usuario ya está registrado, iniciar sesión
            Toast.makeText(this, "User already logged in", Toast.LENGTH_SHORT).show();
            // Aquí puedes redirigir al usuario a la siguiente actividad
        } else {
            // El usuario no está registrado, registrar en Firebase
            registerWithFirebase();
        }
    }
    private void authenticateBiometric() {
        biometricPrompt.authenticate(promptInfo);
    }


    private BiometricPrompt createBiometricPrompt(FragmentActivity activity, Executor executor) {
        return new BiometricPrompt(activity, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    Toast.makeText(Loading.this, "Authentication successful", Toast.LENGTH_SHORT).show();
                    // Si el usuario ya está autenticado, simplemente redirige a la pantalla principal aquí
                } else {
                    // Si el usuario no está autenticado, registra con Firebase
                    registerWithFirebase();
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(Loading.this, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerWithFirebase() {
        // Generar un UID único para el usuario
        String uid = generateUniqueUID();
        // Registrar al usuario en Firebase con el UID generado

        mAuth.signInWithCustomToken(uid)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                        // Aquí puedes redirigir al usuario a la siguiente actividad
                    } else {
                        Log.i("hola",task.getException().getMessage());
                        Toast.makeText(this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(this, e -> {
                    Log.i("hola",e.getMessage());
                    Toast.makeText(this, "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private String generateUniqueUID() {
        // Aquí puedes implementar lógica para generar un UID único
        // Por ejemplo, puedes usar un UUID aleatorio
        return UUID.randomUUID().toString();
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

