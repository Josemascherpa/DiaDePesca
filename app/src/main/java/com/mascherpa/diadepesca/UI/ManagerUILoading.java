package com.mascherpa.diadepesca.UI;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mascherpa.diadepesca.databinding.LoadinguiBinding;

public class ManagerUILoading {
    private LoadinguiBinding binding;
    FrameLayout bottomSheet;
    BottomSheetBehavior<View> bottomSheetBehavior;
    private FirebaseAnalytics mFirebaseAnalytics;
    public ManagerUILoading(LoadinguiBinding bindingLoading, Context context, Activity activity){
        this.binding = bindingLoading;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        setsBottomSheets();
        barBackgroundsBlack(activity);
        clickButtonRegister(bindingLoading.comenzaraventura);
    }

    public void clickButtonRegister(Button btnRegister){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                singUp();
            }
        });
    }

    private void barBackgroundsBlack(Activity activity){//Pinto de oclor negro la barra de notif
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(0xFF000000);
    }

    private void singUp(){//expando bottom sheets
        bottomSheet.setVisibility(View.VISIBLE);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void setsBottomSheets(){
        bottomSheet = binding.standardBottomSheet;
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {//oculto o muestro boton de comenzar
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState==BottomSheetBehavior.STATE_EXPANDED){
                    binding.comenzaraventura.setEnabled(false);

                }else if(newState==BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_HIDDEN){
                    binding.comenzaraventura.setEnabled(true);

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    public void closeBottomSheetBehavior(){
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public int returnStateBottomSheetsSignUp(){
        return bottomSheetBehavior.getState();
    }

    public BottomSheetBehavior getBottomSheetsSign(){
        return bottomSheetBehavior;
    }






}
