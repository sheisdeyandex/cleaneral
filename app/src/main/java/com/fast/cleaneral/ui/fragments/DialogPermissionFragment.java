package com.fast.cleaneral.ui.fragments;


import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.fragment.app.DialogFragment;

import com.fast.cleaneral.databinding.DialogPermissionBinding;
import com.fast.cleaneral.ui.activities.MainActivity;

public class DialogPermissionFragment extends DialogFragment {
    public DialogPermissionFragment(){
    }
    @Override
    public void onStart()
    {
        super.onStart();

    }
DialogPermissionBinding binding;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DialogPermissionBinding.inflate(inflater, container, false);
  View v = binding.getRoot();

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding.tvPermite.setOnClickListener(v1 -> {
            if(((MainActivity)getActivity()).hasManageExternalStoragePermission()){
                dismiss();
            }
            else {
            try {
                ((MainActivity)getActivity()).hasManageExternalStoragePermission();
            } catch (Exception e) {

                e.printStackTrace();
            }}
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