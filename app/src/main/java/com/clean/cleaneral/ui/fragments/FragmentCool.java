package com.clean.cleaneral.ui.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clean.cleaneral.R;
import com.clean.cleaneral.databinding.FragmentCoolBinding;
import com.clean.cleaneral.interfaces.FragmentInterface;
import com.clean.cleaneral.ui.activities.MainActivity;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentCool extends Fragment {

FragmentInterface fragmentInterface;
Context context;

    public FragmentCool(Context context, FragmentInterface fragmentInterface) {
        this.context = context;
        this.fragmentInterface = fragmentInterface;
        // Required empty public constructor
    }
    public static int cpuTemperature()
    {
        Process process;
        try {
            process = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone0/temp");
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            if(line!=null) {
                int temp = Integer.parseInt(line);
                return temp / 1000;
            }else{
                return 51;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    FragmentCoolBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
   binding = FragmentCoolBinding.inflate(inflater, container, false);
   View view = binding.getRoot();
   binding.ivBack.setOnClickListener(v -> fragmentInterface.show(new FragmentMain(requireContext(),((MainActivity)requireActivity())),this));
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {
binding.clScan.setVisibility(View.GONE);
binding.ivBack.setVisibility(View.GONE);
binding.clCool.setVisibility(View.VISIBLE);
                binding.tvTemperature.setText(cpuTemperature() +"°C");
            }
        }.start();

        SharedPreferences.Editor editor =requireContext().getSharedPreferences("whattoshow", MODE_PRIVATE).edit();

binding.ivClean.setOnClickListener(v -> {

    editor.putBoolean("cool", false).apply();
    binding.clCool.setVisibility(View.GONE);
    binding.clScan.setVisibility(View.VISIBLE);
    binding.ivBigCircle.setVisibility(View.GONE);
    Picasso.get().load(R.drawable.ic_cool_recommend).error(R.drawable.ic_cool_recommend).placeholder(R.drawable.ic_cool_recommend).into(binding.ivSmallCircle);
    new CountDownTimer(2000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            binding.tvScan.setText("Охлаждение системы");
           fragmentInterface.show(new FragmentCleanFinished(((MainActivity)requireActivity()), getContext(),""), FragmentCool.this);

        }
    }.start();
});
        return view;
    }
}