package com.clean.cleaneral;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clean.cleaneral.databinding.FragmentBoostFinishedBinding;
import com.clean.cleaneral.interfaces.FragmentInterface;
import com.clean.cleaneral.ui.activities.MainActivity;
import com.clean.cleaneral.ui.fragments.FragmentCool;
import com.clean.cleaneral.ui.fragments.FragmentLoading;
import com.clean.cleaneral.ui.fragments.FragmentMain;

import java.util.List;

public class FragmentBoostFinished extends Fragment {
    public FragmentBoostFinished(FragmentInterface fragmentInterface, Context context) {
        this.fragmentInterface = fragmentInterface;
        this.context = context;
    }

    FragmentInterface fragmentInterface;
    Context context;
    Integer countapps= 0;
    public  String getActiveApps(Context context) {

        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        String value = "";
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

    FragmentBoostFinishedBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
 binding = FragmentBoostFinishedBinding.inflate(inflater,container,false);
 View view = binding.getRoot();
        getActiveApps(requireContext());
        binding.ivBack.setOnClickListener(v -> fragmentInterface.show(new FragmentMain(requireContext(),((MainActivity)requireContext())), this));
        binding.tvClosedApps.setText(getResources().getString(R.string.closed)+" "+countapps+" "+getResources().getString(R.string.apps));
        binding.clCool.setOnClickListener(v ->fragmentInterface.show(new FragmentLoading(getContext(), ((MainActivity)requireContext())), this) );

        return view;
    }
}