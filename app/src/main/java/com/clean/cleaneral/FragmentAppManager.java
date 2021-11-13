package com.clean.cleaneral;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_FIRST_USER;
import static android.app.Activity.RESULT_OK;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.clean.cleaneral.databinding.FragmentAppManagerBinding;
import com.clean.cleaneral.interfaces.FragmentInterface;
import com.clean.cleaneral.ui.activities.MainActivity;
import com.clean.cleaneral.ui.fragments.FragmentMain;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
public class FragmentAppManager extends Fragment implements FragmentInterface {
Context context;
    FragmentInterface fragmentAppManager;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    int uniqueId = 0;

    int getUniqueId()
    {
        return uniqueId++;
    }
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentAppManager(Context context, FragmentInterface fragmentAppManager) {
        this.context = context;
        this.fragmentAppManager = fragmentAppManager;
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
    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";

        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
    AdapterDeleteApps appsAdapter;
    List<ModelDeleteApps> appsModelList;
    List<String> appname;
    List<String> appsize;
    List<Drawable> appicon;
    ModelDeleteApps appsModel;
    ArrayList<Integer>  ids;
    ArrayList<Integer> integers1;
FragmentAppManagerBinding binding;
    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }
    ActivityResultLauncher<Intent> activityResultLaunch;
     UsageStatsManager usageStatsManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activityResultLaunch = registerForActivityResult(
                new  ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            for(int i :integers1) {

                                for(int i1=0;i1<ids.size();i1++){
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                                        int finalI1 = i1;
                                        appsModelList.removeIf(imagesModel -> {
                                            if(imagesModel.getId() == ids.get(finalI1)){
                                                appsAdapter.checked-=1;

                                                return true;}
                                            else {
                                                return false;
                                            }
                                        });
                                    }
                                }
                                appsAdapter.notifyItemRemoved(i);
                                appsAdapter.notifyItemRangeChanged(i, appsModelList.size());
                            }
                 /*          for(int i1:integers1){
                               appsModelList.remove(i1);
                               appsAdapter.notifyItemRemoved(i1);
                           }
                           appsAdapter.notifyItemRangeChanged(findMinIndex(integers1), appsModelList.size());*/
                        }
                        else if(result.getResultCode() == RESULT_CANCELED){
                            Log.d("tag", "cancelled");
                        }
                        else if(result.getResultCode() == RESULT_FIRST_USER){

                            Log.d("tag", "uninstallfail");
                        }
                    }
                });

  binding = FragmentAppManagerBinding.inflate(inflater, container,false);
  View view = binding.getRoot();

        binding.ivBack.setOnClickListener(v -> fragmentAppManager.show(new FragmentMain(requireContext(), ((MainActivity)requireContext())), this));
        appname =new ArrayList<>();
        appicon= new ArrayList<>();
        appsize = new ArrayList<>();
       usageStatsManager = (UsageStatsManager)requireContext().getSystemService(Context.USAGE_STATS_SERVICE);// Context.USAGE_STATS_SERVICE);
        Calendar beginCal = Calendar.getInstance();
        beginCal.set(Calendar.DAY_OF_MONTH, 11);
        beginCal.set(Calendar.MONTH, 10);
        beginCal.set(Calendar.YEAR, 2015);
        List<String> options = new ArrayList<>();
        options.add("1");
        options.add("2");
        options.add("3");
        ArrayAdapter<String> myAdapter = new AdapterCustomSpinner(requireContext(), R.layout.custom_spinner, options);
        binding.spMain.setAdapter(myAdapter);
        final List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, beginCal.getTimeInMillis(), System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        appsModelList =new ArrayList<>();
        List<PackageInfo> packList = requireContext().getPackageManager().getInstalledPackages(0);
        for (int i=0; i < packList.size(); i++)
        {
            PackageInfo packInfo = packList.get(i);

            if (  (packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
            {

                String appName = packInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();

                String packageName =  packInfo.applicationInfo.packageName;

                if(!packageName.equals("com.clean.cleaneral")){
                    appname.add(appName);
                    try {

                        long size =new File(getContext().getPackageManager().getApplicationInfo(packageName,0).publicSourceDir).length();
                        appsize.add(getFileSize(size));
                        try {
                            Drawable icon = getContext().getPackageManager().getApplicationIcon(packageName);

                            appicon.add(icon);

                            appsModel = new ModelDeleteApps();
                            for (UsageStats app : queryUsageStats) {
                                if(app.getPackageName().equals(packageName)&&!app.getPackageName().equals("com.clean.cleaneral")){
appsModel.setUsagetime(String.valueOf( app.getTotalTimeInForeground() / (1000*60*60) % 24));
                                    String dateString = formatter.format(app.getLastTimeUsed());
appsModel.setLasttimeusage(dateString);
                                }
                            }
                            appsModel.setName(appName);
                            appsModel.setIcon(icon);
                            appsModel.setSize(getFileSize(size));
                            appsModel.setId(getUniqueId());
                            appsModel.setPackagename(packageName);
                            appsModelList.add(appsModel);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }}
            }
            else if(isSystemPackage(packList.get(i))){

                String packageName =  packInfo.applicationInfo.packageName;
                long size = 0;
                try {
                    size = new File(getContext().getPackageManager().getApplicationInfo(packageName,0).publicSourceDir).length();

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }


        binding.recycler1.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        appsAdapter = new AdapterDeleteApps(getContext(), appsModelList, this, this);
        binding.recycler1.setAdapter(appsAdapter);
        return view;
    }

    @Override
    public void permission(boolean given) {

    }

    @Override
    public void show(Fragment fragment, Fragment fragment1) {

    }

    @Override
    public void delete(ArrayList<Integer> integers, ArrayList<Uri> uris, ArrayList<Integer> ids) {
        integers1= new ArrayList<>();
        integers1.addAll(integers);
        this.ids = new ArrayList<>();
        this.ids.addAll(ids);
        for(Uri i:uris){

            Intent uninstallIntent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, i);
            uninstallIntent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
            activityResultLaunch.launch(uninstallIntent);}
    }


}