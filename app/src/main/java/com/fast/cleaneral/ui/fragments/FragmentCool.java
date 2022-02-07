package com.fast.cleaneral.ui.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fast.cleaneral.R;
import com.fast.cleaneral.app;
import com.fast.cleaneral.databinding.FragmentCoolBinding;
import com.fast.cleaneral.interfaces.FragmentInterface;
import com.fast.cleaneral.ui.activities.MainActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
   binding = FragmentCoolBinding.inflate(inflater, container, false);
   View view = binding.getRoot();
   binding.ivBack.setOnClickListener(v -> fragmentInterface.show(new FragmentMain(requireContext(),((MainActivity)requireActivity())),this));
        AdRequest adRequest = new AdRequest.Builder().build();
        if(!((app) requireActivity().getApplication()).getsubscribe()){

            binding.adBanner.loadAd(adRequest);
        }
        else {
            new CountDownTimer(2000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {

                    binding.clScan.setVisibility(View.GONE);
                    binding.clCool.setVisibility(View.VISIBLE);
                    binding.tvTemperature.setText(cpuTemperature() +"°C");
                }
            }.start();

        }
        binding.adBanner.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {


                binding.clScan.setVisibility(View.GONE);
                binding.clCool.setVisibility(View.VISIBLE);
                binding.tvTemperature.setText(cpuTemperature() +"°C");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {

                binding.clScan.setVisibility(View.GONE);
                binding.clCool.setVisibility(View.VISIBLE);
                binding.tvTemperature.setText(cpuTemperature() +"°C");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {
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
            binding.tvScan.setText(getResources().getString(R.string.cooling_system));
           fragmentInterface.show(new FragmentCleanFinished(((MainActivity)requireActivity()), getContext(),"",((MainActivity)requireActivity())), FragmentCool.this);

        }
    }.start();
});
        return view;
    }
}