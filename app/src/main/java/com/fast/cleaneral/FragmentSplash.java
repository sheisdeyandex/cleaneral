package com.fast.cleaneral;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fast.cleaneral.databinding.FragmentSplashBinding;
import com.fast.cleaneral.interfaces.FragmentInterface;
import com.fast.cleaneral.interfaces.LoadAdmob;
import com.fast.cleaneral.ui.activities.MainActivity;
import com.fast.cleaneral.ui.fragments.FragmentMain;
public class FragmentSplash extends Fragment {
Context context;
FragmentInterface fragmentInterface;
LoadAdmob loadAdmob;
    public FragmentSplash(Context context, FragmentInterface fragmentInterface, LoadAdmob loadAdmob) {
        this.context = context;
        this.loadAdmob = loadAdmob;
        this.fragmentInterface = fragmentInterface;
        // Required empty public constructor
    }
    int i=0;
FragmentSplashBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     binding = FragmentSplashBinding.inflate(inflater,container,false);
     View v = binding.getRoot();
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                i++;
                binding.lpiProgress.setProgress(    i*100/(3000/1000));
            }
            @Override
            public void onFinish() {
                binding.lpiProgress.setProgress( 100);

            }
        }.start();
        return v;
    }
}