package com.clean.cleaneral.ui.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.clean.cleaneral.databinding.DialogPermissionBinding;
import com.clean.cleaneral.databinding.DialogPermissionWriteSettingsBinding;
import com.clean.cleaneral.interfaces.FragmentInterface;
import com.clean.cleaneral.ui.activities.MainActivity;

public class DialogPermissionWriteSettingsFragment extends DialogFragment {
    FragmentInterface fragmentInterface;
     Context context;
    public DialogPermissionWriteSettingsFragment(FragmentInterface fragmentInterface, Context context){
        this.fragmentInterface = fragmentInterface;
        this.context = context;
    }
    @Override
    public void onStart()
    {
        super.onStart();

    }
DialogPermissionWriteSettingsBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DialogPermissionWriteSettingsBinding.inflate(inflater, container, false);
  View v = binding.getRoot();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding.tvPermite.setOnClickListener(v1 -> {
fragmentInterface.permission(true);
dismiss();
        });
        return v;
    }



    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);


    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

}