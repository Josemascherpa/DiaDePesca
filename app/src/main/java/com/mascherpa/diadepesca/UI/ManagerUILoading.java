package com.mascherpa.diadepesca.UI;

import android.content.Context;
import android.view.View;
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

    FrameLayout bottomSheetLogin;


    private FirebaseAnalytics mFirebaseAnalytics;
    public ManagerUILoading(LoadinguiBinding bindingLoading, Context context){
        this.binding = bindingLoading;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        SetsBottomSheets();

    }

    public void ClickButtonRegister(Button btnRegister){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SingUp();
            }
        });
    }



    private void SingUp(){
        bottomSheet.setVisibility(View.VISIBLE);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void SetsBottomSheets(){
        bottomSheet = binding.standardBottomSheet;
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
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

    public int ReturnStateBottomSheetsSignUp(){
        return bottomSheetBehavior.getState();
    }

    public BottomSheetBehavior GetBottomSheetsSign(){
        return bottomSheetBehavior;
    }






}
