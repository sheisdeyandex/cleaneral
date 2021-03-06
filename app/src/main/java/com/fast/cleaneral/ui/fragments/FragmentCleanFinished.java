package com.fast.cleaneral.ui.fragments;
import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fast.cleaneral.R;
import com.fast.cleaneral.app;
import com.fast.cleaneral.databinding.FragmentCleanFinishedBinding;
import com.fast.cleaneral.interfaces.FragmentInterface;
import com.fast.cleaneral.interfaces.LoadAdmob;
import com.fast.cleaneral.ui.activities.MainActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;

import java.util.List;

public class FragmentCleanFinished extends Fragment {

FragmentInterface fragmentInterface;
     Context context;
     String cleaned;
LoadAdmob loadAdmob;
    public FragmentCleanFinished(FragmentInterface fragmentInterface, Context context, String cleaned, LoadAdmob loadAdmob) {
        this.fragmentInterface = fragmentInterface;
        this.cleaned = cleaned;
        this.context = context;
this.loadAdmob  = loadAdmob;
        // Required empty public constructor
    }
   Integer countapps= 0;
    public  String getActiveApps(Context context) {

        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        String value = ""; // basic date stamp
        value += "---------------------------------\n";
        value += "Active Apps\n";
        value += "=================================\n";

        for (ApplicationInfo packageInfo : packages) {

            //system apps! get out
            if (!isSTOPPED(packageInfo) && !isSYSTEM(packageInfo)) {
countapps++;
                value += getApplicationLabel(context, packageInfo.packageName) + "\n" + packageInfo.packageName  + "\n-----------------------\n";

            }
        }

        return value;}
    public static String getApplicationLabel(Context context, String packageName) {

        PackageManager        packageManager = context.getPackageManager();
        List<ApplicationInfo> packages       = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        String                label          = null;

        for (int i = 0; i < packages.size(); i++) {

            ApplicationInfo temp = packages.get(i);

            if (temp.packageName.equals(packageName))
                label = packageManager.getApplicationLabel(temp).toString();
        }

        return label;
    }


    private static boolean isSTOPPED(ApplicationInfo pkgInfo) {

        return ((pkgInfo.flags & ApplicationInfo.FLAG_STOPPED) != 0);
    }


    private static boolean isSYSTEM(ApplicationInfo pkgInfo) {

        return ((pkgInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    FragmentCleanFinishedBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCleanFinishedBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        SharedPreferences prefs =requireContext().getSharedPreferences("whattoshow", MODE_PRIVATE);
        boolean clean = prefs.getBoolean("clean", true);

        boolean boost = prefs.getBoolean("boost", true);
        boolean cool = prefs.getBoolean("cool", true);
        boolean batterysaver = prefs.getBoolean("batterysaver", true);
        AdRequest adRequest = new AdRequest.Builder().build();
        if(!((app) requireActivity().getApplication()).getsubscribe()){

            binding.adBanner.loadAd(adRequest);
        }
        else {

                    binding.clFinished.setVisibility(View.VISIBLE);

        }
        binding.adBanner.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
binding.clFinished.setVisibility(View.VISIBLE);
                loadAdmob.loadInter();
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                binding.clFinished.setVisibility(View.VISIBLE);
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
        if(clean){
       binding.tvCool.setOnClickListener(v ->fragmentInterface.show(new FragmentLoading(getContext(), ((MainActivity)requireContext())),this) );
       binding.tvCool.setText(getResources().getString(R.string.clean_text));
        }
        else {
            if(boost){
                binding.tvCool.setOnClickListener(v ->fragmentInterface.show(new FragmentBoost(((MainActivity)requireContext()), requireContext()),this) );
                binding.tvCool.setText(getResources().getString(R.string.boost));
            }
            else {
                if(cool){
                    binding.tvCool.setOnClickListener(v ->fragmentInterface.show(new FragmentCool(requireContext(), ((MainActivity)requireContext())),this) );
                    binding.tvCool.setText(getResources().getString(R.string.cool_text));
                }
                else {
                    fragmentInterface.show(new FragmentCoolFinished( ((MainActivity)requireContext()),requireContext()),this);

                }
            }
        }
        binding.ivBack.setOnClickListener(v -> fragmentInterface.show(new FragmentMain(requireContext(),((MainActivity)requireContext())), this));
        return view;
    }
}