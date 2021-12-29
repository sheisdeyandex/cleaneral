package com.clean.cleaneral.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.anjlab.android.iab.v3.SkuDetails;
import com.clean.cleaneral.R;
import com.clean.cleaneral.databinding.FragmentMainBinding;
import com.clean.cleaneral.databinding.FragmentPaywallBinding;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPaywall#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPaywall extends Fragment implements BillingProcessor.IBillingHandler {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentPaywall() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentPaywall.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentPaywall newInstance(String param1, String param2) {
        FragmentPaywall fragment = new FragmentPaywall();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
FragmentPaywallBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    public  String productId;
    public  String title;
    public  String description;
    public  boolean isSubscription;
    public String currency;
    public  Double priceValue;
    public  String priceText;
    BillingProcessor bp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPaywallBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        bp = new BillingProcessor(requireContext(), null, this);
        bp.initialize();

        bp.getSubscriptionPurchaseInfo("fast.clean.app.pro.365days");
        bp.getSubscriptionListingDetailsAsync("fast.clean.app.pro.365days", new BillingProcessor.ISkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(@Nullable List<SkuDetails> products) {
  binding.tvSubscribeMonthly.setText(products.get(0).priceText+" "+products.get(0).currency+" / month");
            }

            @Override
            public void onSkuDetailsError(String error) {
                binding.tvSubscribeMonthly.setText(error);
            }
        });
binding.clMonthly.setOnClickListener(view1 -> bp.subscribe(requireActivity(), "fast.clean.app.pro.365days"));

        return view;
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable PurchaseInfo details) {

    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }
}