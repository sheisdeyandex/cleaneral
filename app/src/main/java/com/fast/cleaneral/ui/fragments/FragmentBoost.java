package com.fast.cleaneral.ui.fragments;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.app.ActivityManager;
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
import com.fast.cleaneral.databinding.FragmentBoostBinding;
import com.fast.cleaneral.interfaces.FragmentInterface;
import com.fast.cleaneral.ui.activities.MainActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;

public class FragmentBoost extends Fragment {
    FragmentInterface fragmentInterface;
    Context context;
    public FragmentBoost(FragmentInterface fragmentInterface, Context context) {
        this.fragmentInterface = fragmentInterface;
        this.context = context;
        // Required empty public constructor
    }
    FragmentBoostBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentBoostBinding.inflate(inflater, container,false);
       View view = binding.getRoot();
        SharedPreferences.Editor editor =requireContext().getSharedPreferences("whattoshow", MODE_PRIVATE).edit();

        binding.ivBack.setOnClickListener(v -> fragmentInterface.show(new FragmentMain(requireContext(), ((MainActivity)requireActivity())), this));
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager)requireActivity().getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        double availableMegs = mi.availMem / 0x100000L;
        binding.tvTemperature.setText(availableMegs+" MB");
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

                }
            }.start();
        }
        binding.adBanner.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {

                binding.clScan.setVisibility(View.GONE);
                binding.clCool.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {

                binding.clScan.setVisibility(View.GONE);
                binding.clCool.setVisibility(View.VISIBLE);
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

        binding.ivClean.setOnClickListener(v -> {

            editor.putBoolean("boost", false).apply();
            binding.clScan.setVisibility(View.GONE);
            binding.clCool.setVisibility(View.VISIBLE);
            binding.tvScan.setText(getResources().getString(R.string.ram_cleaned));
            new CountDownTimer(2000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
binding.clScan.setVisibility(View.VISIBLE);
binding.clCool.setVisibility(View.GONE);
                }

                @Override
                public void onFinish() {
                    if(isAdded()){
                        fragmentInterface.show(new FragmentCleanFinished(((MainActivity)requireActivity()), requireContext(),"",((MainActivity)requireActivity())), FragmentBoost.this);

                    }
                }
            }.start();
        });
        return view;
    }
}