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
import android.os.Environment;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fast.cleaneral.FileScanner;
import com.fast.cleaneral.R;
import com.fast.cleaneral.app;
import com.fast.cleaneral.databinding.FragmentLoadingBinding;
import com.fast.cleaneral.interfaces.FragmentInterface;
import com.fast.cleaneral.ui.activities.MainActivity;
import com.fast.cleaneral.ui.activities.WhitelistActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;


public class FragmentLoading extends Fragment {
    public static SharedPreferences prefs;
    long kilobytesTotal;
    private String convertSize(long length) {
        final DecimalFormat format = new DecimalFormat("#.##");
        final  long GiB = 1024*1024*1024;
        final long MiB = 1024 * 1024;
        final long KiB = 1024;
        if (length > GiB) {
            return format.format(length / GiB) + " GB";
        }
        if (length > MiB) {
            return format.format(length / MiB) + " MB";
        }
        if (length > KiB) {
            return format.format(length / KiB) + " KB";
        }
        return format.format(length) + " B";
    }
    private void scan(boolean delete) {
        Looper.prepare();

        File path = Environment.getExternalStorageDirectory();
        if(isAdded()){
            FileScanner fs = new FileScanner(path, requireContext())
                    .setEmptyDir(prefs.getBoolean("empty", false))
                    .setAutoWhite(prefs.getBoolean("auto_white", true))
                    .setDelete(delete)
                    .setCorpse(prefs.getBoolean("corpse", true))
                    .setContext(requireContext())
                    .setUpFilters(true, false, true);

            // kilobytes found/freed text
            kilobytesTotal = fs.startScan();
        }
        // scanner setup




        if(isAdded()){

            requireActivity().runOnUiThread(() -> {
                if (delete) {
                    //     binding.statusTextView.setText(getString(R.string.freed) + " " + convertSize(kilobytesTotal));
                } else {
                    //   binding.statusTextView.setText(getString(R.string.found) + " " + convertSize(kilobytesTotal));

                    binding.tvFoundFiles.setText(convertSize(kilobytesTotal));

                    binding.tvScanPercent.setText("0");
                    if(isAdded()){
                        getActiveApps(requireContext());}
                    if(!((app) requireActivity().getApplication()).getsubscribe()){

                        binding.adBanner.setAdListener(new AdListener() {
                            @Override
                            public void onAdLoaded() {

                                binding.clScan.setVisibility(View.GONE);
                                binding.tvActiveApps.setText(getResources().getString(R.string.running_apps)+ " " +countapps);
                                binding.clScanResult.setVisibility(View.VISIBLE);

                            }

                            @Override
                            public void onAdFailedToLoad(LoadAdError adError) {
                                binding.clScan.setVisibility(View.GONE);
                                binding.tvActiveApps.setText(getResources().getString(R.string.running_apps)+ " " +countapps);
                                binding.clScanResult.setVisibility(View.VISIBLE);

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

                    }
                    else {
                        binding.clScan.setVisibility(View.GONE);
                        binding.tvActiveApps.setText(getResources().getString(R.string.running_apps)+ " " +countapps);
                        binding.clScanResult.setVisibility(View.VISIBLE);

                    }

                }



                binding.tvScanPercent.setText("100 %");


            });
        }

        Looper.loop();
    }
    public Context context;
    public  FragmentInterface fragmentInterface;
    public FragmentLoading(Context context, FragmentInterface fragmentInterface) {
        this.context = context ;
        this.fragmentInterface = fragmentInterface;
    }
    Integer countapps= 0;
    public String getActiveApps(Context context) {

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
    FragmentLoadingBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoadingBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.ivBack.setOnClickListener(v -> {fragmentInterface.show(new FragmentMain(requireContext(), ((MainActivity)requireContext())),this);});
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        WhitelistActivity.getWhiteList();
        SharedPreferences.Editor editor =requireContext().getSharedPreferences("whattoshow", MODE_PRIVATE).edit();
        AdRequest adRequest = new AdRequest.Builder().build();
        if(!((app) requireActivity().getApplication()).getsubscribe()){

            binding.adBanner.loadAd(adRequest);
        }

        new Thread(()-> scan(false)).start();
        binding.ivClean.setOnClickListener(v -> {new Thread(()-> scan(true)).start();
            binding.clScanResult.setVisibility(View.GONE);
            binding.clScan.setVisibility(View.VISIBLE);
            binding.ivBigCircle.setVisibility(View.GONE);
            Picasso.get().load(R.drawable.ic_cleaning).error(R.drawable.ic_cleaning).placeholder(R.drawable.ic_cleaning).into(binding.ivSmallCircle);
            binding.ivBack.setVisibility(View.GONE);
            binding.tvScan.setText(getResources().getString(R.string.clean_app_files));
            binding.tvScanPercent.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(binding.tvScanPercent.getText().toString().contains("100")){

                        editor.putBoolean("clean", false).apply();
                        fragmentInterface.show(new FragmentCleanFinished(((MainActivity)requireActivity()), getContext(), convertSize(kilobytesTotal), ((MainActivity)requireActivity())),FragmentLoading.this);
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        });


        return view;
    }
}