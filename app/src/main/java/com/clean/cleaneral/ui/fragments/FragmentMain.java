package com.clean.cleaneral.ui.fragments;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.clean.cleaneral.FragmentAppManager;
import com.clean.cleaneral.FragmentNotifications;
import com.clean.cleaneral.databinding.FragmentMainBinding;
import com.clean.cleaneral.interfaces.FragmentInterface;
import com.clean.cleaneral.ui.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class FragmentMain extends Fragment implements FragmentInterface{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
Context context;
FragmentInterface fragmentInterface;
    public FragmentMain(Context context, FragmentInterface fragmentInterface) {
        this.context = context;
        this.fragmentInterface = fragmentInterface;
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    public long getTotalStorageInfo(String path) {
        StatFs statFs = new StatFs(path);
        long t;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            t = statFs.getTotalBytes();
        } else {
            t = statFs.getBlockCountLong() * statFs.getBlockSizeLong();
        }
        return t;    // remember to convert in GB,MB or KB.
    }

    public long getUsedStorageInfo(String path) {
        StatFs statFs = new StatFs(path);
        long u;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            u = statFs.getTotalBytes() - statFs.getAvailableBytes();
        } else {
            u = statFs.getBlockCountLong() * statFs.getBlockSizeLong() - statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong();
        }
        return u;  // remember to convert in GB,MB or KB.
    }

    private boolean checkSystemWritePermission() {
        boolean retVal ;
        retVal = Settings.System.canWrite(getContext());
        if(retVal){

            fragmentInterface.show(new FragmentBatterySaver(requireContext(), ((MainActivity)requireContext())), this);
        }
        else {

           new DialogPermissionWriteSettingsFragment(this, requireContext()).show(getChildFragmentManager(),"");
        }
        return retVal;
    }
    private boolean checkSystemappManagerPermission() {
        boolean granted ;
        UsageStatsManager  usageStatsManager = (UsageStatsManager)requireContext().getSystemService(Context.USAGE_STATS_SERVICE);// Context.USAGE_STATS_SERVICE);

        List<UsageStats> stats = usageStatsManager
                .queryUsageStats(UsageStatsManager.INTERVAL_BEST, 0, System.currentTimeMillis());
         granted = stats.isEmpty();
        if (granted) {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
        else {
            fragmentInterface.show(new FragmentAppManager(requireContext(), ((MainActivity)requireContext())), this);
        }
        return granted;
    }
    public void openAndroidPermissionsMenu() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
        startActivity(intent);
    }
FragmentMainBinding binding;

    UsageStatsManager usageStatsManager;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        long internaltotal=  getTotalStorageInfo(Environment.getDataDirectory().getPath());
        long internalfree=  getUsedStorageInfo(Environment.getDataDirectory().getPath());
        binding.tvMemoryUsage.setText(String.valueOf((int)(100*internalfree/internaltotal))+"%");
        binding.lpiProgress.setProgress((int) (100*internalfree/internaltotal));
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) requireActivity().getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        double percentAvail = mi.availMem / (double)mi.totalMem * 100.0;
        binding.tvRamUsage.setText(String.valueOf( (int) percentAvail)+"%");
        binding.lpiRamProgress.setProgress((int) percentAvail);
        binding.tvCirclePercent.setText(String.valueOf((int)(100*internalfree/internaltotal))+"%");
binding.ivBatterySaver.setOnClickListener(v -> {
    checkSystemWritePermission();
});
binding.ivBoost.setOnClickListener(v ->
{

    fragmentInterface.show(new FragmentBoost(((MainActivity)requireContext()), requireContext()),this);

});
binding.ivBoostMain.setOnClickListener(v -> {  fragmentInterface.show(new FragmentBoost(((MainActivity)requireContext()), requireContext()),this);
});
binding.ivScanView.setOnClickListener(v ->binding.ivClean.performClick() );
binding.animationView1.setRotation(90);
        usageStatsManager = (UsageStatsManager)requireContext().getSystemService(Context.USAGE_STATS_SERVICE);// Context.USAGE_STATS_SERVICE);

        binding.ivCleanNotifications.setOnClickListener(v ->{
    List<UsageStats> stats = usageStatsManager
            .queryUsageStats(UsageStatsManager.INTERVAL_DAILY, 0, System.currentTimeMillis());
    boolean isEmpty = stats.isEmpty();
    if (isEmpty) {
        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
    }
    else {
        fragmentInterface.show(new FragmentAppManager(requireContext(), ((MainActivity)requireContext())),this);
    }

});
binding.ivClean.setOnClickListener(v ->

        {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {

               fragmentInterface.show(new FragmentLoading(getContext(),((MainActivity)getActivity())),this);
                }
                else {
                    if (Environment.isExternalStorageLegacy()) {
                        new DialogPermissionFragment().show(getChildFragmentManager(),"");
                    }

                }
                }
         else     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (checkPermission()) {
                    fragmentInterface.show(new FragmentLoading(getContext(), ((MainActivity)getActivity())),this);

                } else {
                    new DialogPermissionLowApiFragment().show(getChildFragmentManager(),"");
                }
            }
            else     if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                if (checkPermission()) {
                    fragmentInterface.show(new FragmentLoading(getContext(), ((MainActivity)getActivity())),this);

                } else {
                    new DialogPermissionLowApiFragment().show(getChildFragmentManager(),"");
                }
            }

        });
        binding.ivCool.setOnClickListener(v ->{

            fragmentInterface.show(new FragmentCool(getContext(), ((MainActivity)requireContext())),this);

        });

        return view;
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void permission(boolean given) {
        openAndroidPermissionsMenu();
    }
    @Override
    public void show(Fragment fragment, Fragment fragment1) {
    }
    @Override
    public void delete(ArrayList<Integer> integers, ArrayList<Uri> uris, ArrayList<Integer> ids) {
    }
}