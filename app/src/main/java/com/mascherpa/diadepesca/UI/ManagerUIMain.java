package com.mascherpa.diadepesca.UI;

import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.mascherpa.diadepesca.databinding.LoadinguiBinding;
import com.mascherpa.diadepesca.databinding.MainBinding;

public class ManagerUIMain {
    private MainBinding binding;

    public ManagerUIMain(MainBinding bindingMain, Context context){
        this.binding = bindingMain;

    }

    public void setNameUser(String name){
        binding.tvNombreUser.setText(name);
    }

}
