package com.clean.cleaneral.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.clean.cleaneral.databinding.FragmentCoolFinishedBinding;
import com.clean.cleaneral.interfaces.FragmentInterface;
import com.clean.cleaneral.ui.activities.MainActivity;

public class FragmentCoolFinished extends Fragment {

FragmentInterface fragmentInterface;
     Context context;
    public FragmentCoolFinished(FragmentInterface fragmentInterface, Context context) {
        this.fragmentInterface = fragmentInterface;
        this.context = context;
    }
FragmentCoolFinishedBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCoolFinishedBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.mbtnOk.setOnClickListener(v ->fragmentInterface.show(new FragmentMain(requireContext(), ((MainActivity)requireContext())), this) );
        binding.ivBack.setOnClickListener(v -> fragmentInterface.show(new FragmentMain(requireContext(), ((MainActivity)requireContext())),this));
        return view;
    }
}