package com.mascherpa.diadepesca.UI;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.mascherpa.diadepesca.databinding.LoadinguiBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class ManagerUILoading {
    private LoadinguiBinding binding;
    FrameLayout bottomSheet;
    BottomSheetBehavior<View> bottomSheetBehavior;

    FrameLayout bottomSheetLogin;
    BottomSheetBehavior<View> bottomSheetBehaviorLogin;

    public ManagerUILoading(LoadinguiBinding bindingLoading){
        this.binding = bindingLoading;
        SetsBottomSheets();

    }

    public void ClickButtonRegister(Button btnRegister){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SingUp();
            }
        });
    }

    public void ClickButtonLogin(Button btnLogin){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Login();
            }
        });
    }

    private void Login(){
        bottomSheetLogin.setVisibility(View.VISIBLE);
        bottomSheet.setVisibility(View.INVISIBLE);
        bottomSheetBehaviorLogin.setState(BottomSheetBehavior.STATE_EXPANDED);

    }

    private void SingUp(){
        bottomSheetLogin.setVisibility(View.INVISIBLE);
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
                    binding.ingresar.setEnabled(false);
                }else if(newState==BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_HIDDEN){
                    binding.comenzaraventura.setEnabled(true);
                    binding.ingresar.setEnabled(true);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        bottomSheetLogin = binding.loginBottomSheets;
        bottomSheetBehaviorLogin = BottomSheetBehavior.from(bottomSheetLogin);

        bottomSheetBehaviorLogin.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState==BottomSheetBehavior.STATE_EXPANDED){
                    binding.comenzaraventura.setEnabled(false);
                    binding.ingresar.setEnabled(false);
                }else if(newState==BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_HIDDEN){
                    binding.comenzaraventura.setEnabled(true);
                    binding.ingresar.setEnabled(true);
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
    public int ReturnStateBottomSheetsLogin(){
            return bottomSheetBehaviorLogin.getState();
    }
    public BottomSheetBehavior GetBottomSheetsSign(){
        return bottomSheetBehavior;
    }

    public BottomSheetBehavior GetBottomSheetsLogin(){
        return bottomSheetBehaviorLogin;
    }




}
