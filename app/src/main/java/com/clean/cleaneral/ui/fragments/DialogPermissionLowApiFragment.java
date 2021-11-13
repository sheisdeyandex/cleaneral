package com.clean.cleaneral.ui.fragments;


import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.clean.cleaneral.databinding.DialogPermissionBinding;
import com.clean.cleaneral.databinding.DialogPermissionLowApiBinding;
import com.clean.cleaneral.ui.activities.MainActivity;

public class DialogPermissionLowApiFragment extends DialogFragment {
    public DialogPermissionLowApiFragment(){
    }
    @Override
    public void onStart()
    {
        super.onStart();

    }
    private static final int PERMISSION_REQUEST_CODE = 1;
DialogPermissionLowApiBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DialogPermissionLowApiBinding.inflate(inflater, container, false);
  View v = binding.getRoot();

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding.tvPermite.setOnClickListener(v1 -> {
            if (checkPermission())
            {
                dismiss();
            } else {
                requestPermission(); // Code for permission
            }
        });
        return v;
    }



    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);


    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(requireContext(), "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }
}