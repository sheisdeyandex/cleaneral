package com.fast.cleaneral.ui.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fast.cleaneral.R;
import com.fast.cleaneral.app;
import com.fast.cleaneral.databinding.FragmentMainBinding;
import com.fast.cleaneral.databinding.FragmentPaywallBinding;
import com.fast.cleaneral.interfaces.FragmentInterface;
import com.fast.cleaneral.interfaces.purchaseinterface;
import com.fast.cleaneral.ui.activities.MainActivity;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FragmentPaywall extends Fragment {
Context context;
purchaseinterface purchaseinterface;
FragmentInterface fragmentInterface;
    public FragmentPaywall(Context context, purchaseinterface purchaseinterface, FragmentInterface fragmentInterface) {
        this.fragmentInterface = fragmentInterface;
      this.context = context;
      this.purchaseinterface = purchaseinterface;
    }

FragmentPaywallBinding binding;
    public  String productId;
    public  String title;
    public  String description;
    public  boolean isSubscription;
    public String currency;
    public  Double priceValue;
    public  String priceText;
    SharedPreferences prefs;
SharedPreferences.Editor editor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPaywallBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        editor  =requireActivity().getSharedPreferences("whattoshow", MODE_PRIVATE).edit();
binding.ivBack.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        fragmentInterface.show(new FragmentMain(requireContext(),((MainActivity)requireActivity())), FragmentPaywall.this);
    }
});
         prefs =requireContext().getSharedPreferences("whattoshow", MODE_PRIVATE);
initTexts();
    countTimer();
    binding.clYearlyCl.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            purchaseinterface.subscribe();
        }
    });
binding.clMonthly.setOnClickListener(view1 ->{
    purchaseinterface.subscribe();
} );

        return view;
    }
    void initTexts(){
        String priceText = prefs.getString("pricetext", "");
        String priceText30 = prefs.getString("pricetext30", "");
        Double s = ((app) requireActivity().getApplication()).getprice30();
        Double yearlys = ((app) requireActivity().getApplication()).getprice();
        binding.tvSubscribeMonthly.setText(priceText30+" / "+getString(R.string.month));
        binding.tvYearly.setText(priceText);
        DecimalFormat formater = new DecimalFormat("#.##");
        if(priceText30.length()>0){
            binding.tvYearlyBottom.setText(formater.format( yearlys/12.00)+" "+priceText30.substring(priceText30.length()-1) +" / "+ getString(R.string.month));
            binding.tvYearlyCrossline.setText(s*12.00+" "+ priceText30.substring(priceText30.length()-1));

        }
        binding.tvYearlyCrossline.setPaintFlags(binding.tvYearlyCrossline.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

    }
    void countTimer(){
        long savedTime = prefs.getLong("savedtime", 0);

if(savedTime<=0){

    long Time1 =System.currentTimeMillis();
    long Time2 = System.currentTimeMillis()+9330000;
    editor.putLong("savedtime", Time2).apply();
    SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
    String dateString1 = format.format(new Date(Time1));
    String dateString2 = format.format(new Date(Time2));
    Date d1 = null;
    Date d2 = null;
    try {
        d1= format.parse(dateString1);
        d2 = format.parse(dateString2);
    } catch (ParseException e) {
        e.printStackTrace();
    }

    final String FORMAT = "%02d:%02d:%02d";

    long diff = d2.getTime() - d1.getTime();
    long diffSeconds2 = diff ;
    new CountDownTimer(diffSeconds2,1000){

        @Override
        public void onTick(long millisUntilFinished) {

            binding.tvTimer.setText(""+String.format(FORMAT,
                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));


        }

        @Override
        public void onFinish() {

        }
    }.start();

}
else {

    long Time1 =System.currentTimeMillis();
    SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
    String dateString1 = format.format(new Date(Time1));
    String dateString2 = format.format(new Date(savedTime));
    Date d1 = null;
    Date d2 = null;
    try {
        d1= format.parse(dateString1);
        d2 = format.parse(dateString2);
    } catch (ParseException e) {
        e.printStackTrace();
    }

    final String FORMAT = "%02d:%02d:%02d";

    long diff = d2.getTime() - d1.getTime();
    long diffSeconds2 = diff ;
    new CountDownTimer(diffSeconds2,1000){

        @Override
        public void onTick(long millisUntilFinished) {
            binding.tvTimer.setText(""+String.format(FORMAT,
                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));


        }

        @Override
        public void onFinish() {

        }
    }.start();

}

    }
}