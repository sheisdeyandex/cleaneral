package com.clean.cleaneral.ui.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.clean.cleaneral.R;
import com.clean.cleaneral.databinding.FragmentBatterySaverBinding;
import com.clean.cleaneral.interfaces.FragmentInterface;
import com.clean.cleaneral.ui.activities.MainActivity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
public class FragmentBatterySaver extends Fragment {
FragmentInterface fragmentInterface;
Context context;
    public FragmentBatterySaver(Context context, FragmentInterface fragmentInterface) {
        this.context = context;
        this.fragmentInterface = fragmentInterface;
        // Required empty public constructor
    }
    ArrayList<String> names = new ArrayList<>();
    public void setBrightness(int brightness){

        //constrain the value of brightness
        if(brightness < 0)
            brightness = 0;
        else if(brightness > 255)
            brightness = 255;


        ContentResolver cResolver = requireActivity().getApplicationContext().getContentResolver();
        Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);

    }
    private TimerTask NoticeTimerTask;
    private final Handler handler = new Handler();
    Timer timer;
    int appscount;

    ArrayList<Integer> appstotal= new ArrayList<>();
FragmentBatterySaverBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentBatterySaverBinding.inflate(inflater,container, false);
       View view = binding.getRoot();
        SharedPreferences.Editor editor =requireContext().getSharedPreferences("whattoshow", MODE_PRIVATE).edit();

        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
           binding.clScan.setVisibility(View.GONE);
           binding.clScanResult.setVisibility(View.VISIBLE);
            }
        }.start();
        WifiManager wifiManager = (WifiManager) requireContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        boolean wifiEnabled = wifiManager.isWifiEnabled();
binding.mcbNormalMode.setOnCheckedChangeListener((buttonView, isChecked) -> {

    if(isChecked){
        binding.mcbHardSaving.setChecked(false);
        binding.mcbLowSaving.setChecked(false);
    }
});
        binding.mcbHardSaving.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                binding.mcbNormalMode.setChecked(false);
                binding.mcbLowSaving.setChecked(false);
            }
        });
        binding.mcbLowSaving.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                binding.mcbHardSaving.setChecked(false);
                binding.mcbNormalMode.setChecked(false);
            }
        });
        binding.ivBack.setOnClickListener(v -> fragmentInterface.show(new FragmentMain(requireContext(),((MainActivity)requireActivity())),this));
        List<PackageInfo> packList = requireActivity().getPackageManager().getInstalledPackages(0);
        for (int i=0; i < packList.size(); i++)
        {
            PackageInfo packInfo = packList.get(i);
            if (  (packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0&&(packInfo.applicationInfo.flags & ApplicationInfo.FLAG_STOPPED) == 0)
            {
                String packageName =  packInfo.applicationInfo.packageName;
                if(!packageName.equals("com.clean.cleaneral")) {
                    ActivityManager am = (ActivityManager)requireActivity().getSystemService(Context.ACTIVITY_SERVICE);
                    am.killBackgroundProcesses(packageName);
                    appscount++;
                    String appName = packInfo.applicationInfo.loadLabel(requireActivity().getPackageManager()).toString();
                    names.add(appName);
                }}
        }
        timer  = new Timer();
        final int max = names.size();
        final int[] current = {0};
       binding.ivClean.setOnClickListener(v -> {
           editor.putBoolean("batterysaver", false).apply();

           binding.ivBack.setVisibility(View.GONE);
           binding.clScan.setVisibility(View.VISIBLE);
           binding.clScanResult.setVisibility(View.GONE);
           NoticeTimerTask = new TimerTask() {
               public void run() {
                   handler.post(() -> {
                       if(current[0] <max) {
                           binding.tvScan.setText( (getResources().getString(R.string.closing_heated_apps)+" "+(current[0]+1))+"/ "+ appscount);
                           current[0]++;
                       }
                       else {
                           timer.cancel();

                         fragmentInterface.show(new FragmentCleanFinished( ((MainActivity)requireActivity()),requireContext(),""), FragmentBatterySaver.this);
                       }
                   });
               }
           };
           if (timer!=null) {

               timer.schedule(NoticeTimerTask, 0, 50);
           }

           if(binding.mcbNormalMode.isChecked()){

               setBrightness(200);

           }
           else  if(binding.mcbLowSaving.isChecked()){
               if(wifiEnabled){
                   wifiManager.setWifiEnabled(false);
               }

               setBrightness(150);
           }
           else if(binding.mcbHardSaving.isChecked()){
               if(wifiEnabled){
                   wifiManager.setWifiEnabled(false);
               }

               setBrightness(80);
           }
 });
        return view;
    }
}