package com.fast.cleaneral.ui.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fast.cleaneral.databinding.FragmentMainBinding;
import com.fast.cleaneral.databinding.FragmentPaywallBinding;
import com.fast.cleaneral.interfaces.purchaseinterface;

public class FragmentPaywall extends Fragment {
Context context;
purchaseinterface purchaseinterface;
    public FragmentPaywall(Context context, purchaseinterface purchaseinterface) {
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPaywallBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        SharedPreferences prefs =requireContext().getSharedPreferences("whattoshow", MODE_PRIVATE);
String priceText = prefs.getString("pricetext", "");
        binding.tvMonthly.setText(priceText);
binding.tvMonthly.setOnClickListener(view1 ->{
    purchaseinterface.subscribe();
} );

        return view;
    }
}