package com.clean.cleaneral.ui.fragments;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clean.cleaneral.R;
import com.clean.cleaneral.databinding.FragmentBatterySaveFinishedBinding;
import com.clean.cleaneral.interfaces.FragmentInterface;
import com.clean.cleaneral.ui.activities.MainActivity;
public class FragmentBatterySaveFinished extends Fragment {
FragmentInterface fragmentInterface;
Context context;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentBatterySaveFinished(Context context, FragmentInterface fragmentInterface) {

        this.context = context;
        this.fragmentInterface = fragmentInterface;
        // Required empty public constructor
    }
FragmentBatterySaveFinishedBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBatterySaveFinishedBinding.inflate(inflater, container, false);
       View view = binding.getRoot();
       binding.clCool.setOnClickListener(v -> fragmentInterface.show(new FragmentLoading(requireContext(), ((MainActivity)requireContext())), this));
       binding.tvCool.setOnClickListener(v -> fragmentInterface.show(new FragmentLoading(requireContext(), ((MainActivity)requireContext())),this));
       binding.ivCool.setOnClickListener(v -> fragmentInterface.show(new FragmentLoading(requireContext(), ((MainActivity)requireContext())),this));
       binding.ivBack.setOnClickListener(v -> fragmentInterface.show(new FragmentMain(requireContext(), ((MainActivity)requireContext())), this));
       return view;
    }
}