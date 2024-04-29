package com.mascherpa.diadepesca.load;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.mascherpa.diadepesca.MainActivity;
import com.mascherpa.diadepesca.R;
import com.mascherpa.diadepesca.UI.ManagerUILoading;
import com.mascherpa.diadepesca.data.Rio;
import com.mascherpa.diadepesca.databinding.LoadinguiBinding;
import com.mascherpa.diadepesca.network.CheckInternet;
import com.mascherpa.diadepesca.network.DataProvider;

import java.io.Serializable;
import java.util.HashMap;
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

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private GoogleSignInClient mGoogleSignInClient;

    String email;
    private static final int RC_SIGN_IN = 20;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = LoadinguiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managerUI = new ManagerUILoading(binding, this);
        managerUI.ClickButtonRegister(binding.comenzaraventura);

        BarBackgroundsBlack();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                        .requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(),gso);

        // Set OnClickListener for login button
        binding.signupGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });


//        recoveryData = new DataProvider("https://contenidosweb.prefecturanaval.gob.ar/alturas/");

//        if( checkInternet.isOnline()){
//            RecoveryDataRios();
//        }else{
//            Toast.makeText(this,"no hay internet",Toast.LENGTH_LONG).show();
//        }

    }

    private void googleSignIn() {

        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken());
            }catch (Exception e){
                showMessage(e.getMessage());
            }
        }
    }

    private void firebaseAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();
                            HashMap<String,Object> map = new HashMap<>();
                            map.put("id",user.getUid());
                            map.put("name",user.getDisplayName());
                            map.put("profile",user.getPhotoUrl().toString());
                            database.getReference().child("users").child(user.getUid()).setValue(map);
                            Intent intent = new Intent(Loading.this,MainActivity.class);
                            startActivity(intent);

                        }else{
                            showMessage("error");
                        }
                    }
                });
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

