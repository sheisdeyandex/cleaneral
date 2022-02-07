package com.fast.cleaneral.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.anjlab.android.iab.v3.SkuDetails;
import com.fast.cleaneral.AppOpenManager;
import com.fast.cleaneral.FragmentAppManager;
import com.fast.cleaneral.FragmentSplash;
import com.fast.cleaneral.app;
import com.fast.cleaneral.interfaces.LoadAdmob;
import com.fast.cleaneral.interfaces.purchaseinterface;
import com.fast.cleaneral.ui.fragments.FragmentMain;
import com.fast.cleaneral.R;
import com.fast.cleaneral.interfaces.FragmentInterface;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import static android.content.ContentValues.TAG;
import static android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FragmentInterface, purchaseinterface, LoadAdmob {
    Boolean AdisReady= false;

    @Override
    protected void onResume() {
        super.onResume();

    }

    int REQUEST_PERMISSION = 1000;
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                recreate();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragmentMain(getApplicationContext(), this)).commit();

            } else {
                // User refused to grant permission.
            }
        }
    }
    public boolean hasManageExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                return true;
            } else {
                if (Environment.isExternalStorageLegacy()) {
                    return true;
                }
                try {
                    Intent intent = new Intent();
                    intent.setAction(ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.setData(Uri.parse("package:fast.clean.app.pro"));
                    startActivityForResult(intent, REQUEST_PERMISSION); //result code is just an int
                    return false;
                } catch (Exception e) {
                    return false;
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (Environment.isExternalStorageLegacy()) {
                return true;
            } else {
                try {
                    Intent intent = new Intent();
                    intent.setAction(ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.setData(Uri.parse("package:fast.clean.app.pro"));
                    startActivityForResult(intent, REQUEST_PERMISSION); //result code is just an int
                    return false;
                } catch (Exception e) {
                    return true; //if anything needs adjusting it would be this
                }
            }
        }
        return true; // assumed storage permissions granted
    }
    BillingProcessor bp;
    String price;
    AdRequest adRequest;
    ProgressDialog dialog;
    AppOpenManager appOpenManager;
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentSplash fragmentSplash= new FragmentSplash(this, this, this);

        SharedPreferences.Editor editor =getSharedPreferences("whattoshow", MODE_PRIVATE).edit();
        bp =  BillingProcessor.newBillingProcessor(this, null, new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(@NonNull String productId, @Nullable PurchaseInfo details) {
recreate();
            }

            @Override
            public void onPurchaseHistoryRestored() {

            }

            @Override
            public void onBillingError(int errorCode, @Nullable Throwable error) {
                InterstitialAd.load(getApplicationContext(),"ca-app-pub-2272866171011454/5327827261", adRequest,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                // The mInterstitialAd reference will be null until
                                // an ad is loaded.
                                mInterstitialAd = interstitialAd;
                                if(!((app) getApplication()).getsubscribe()){
                                    interstitialAd.show(MainActivity.this);

                                }

                                show(new FragmentMain(getApplicationContext(),MainActivity.this), fragmentSplash);

                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                                Log.i(TAG, loadAdError.getMessage());
                                mInterstitialAd = null;
                                show(new FragmentMain(getApplicationContext(),MainActivity.this), fragmentSplash);

                            }
                        });
             }

            @Override
            public void onBillingInitialized() {
                InterstitialAd.load(getApplicationContext(),"ca-app-pub-2272866171011454/5327827261", adRequest,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                // The mInterstitialAd reference will be null until
                                // an ad is loaded.
                                mInterstitialAd = interstitialAd;
                                if(!((app) getApplication()).getsubscribe()){
                                    interstitialAd.show(MainActivity.this);

                                }

                                show(new FragmentMain(getApplicationContext(),MainActivity.this), fragmentSplash);

                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                                Log.i(TAG, loadAdError.getMessage());
                                mInterstitialAd = null;
                                show(new FragmentMain(getApplicationContext(),MainActivity.this), fragmentSplash);

                            }
                        });
                if(bp.isSubscribed("365days")){
                    ((app) getApplication()).setgetsubscribe(true);


                }
                else if(bp.isSubscribed("30days")){
                    ((app) getApplication()).setgetsubscribe(true);
                }

                bp.getSubscriptionListingDetailsAsync("365days", new BillingProcessor.ISkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(@Nullable List<SkuDetails> products) {
                        try {
                            JSONArray skuArray = new JSONArray(products);
                            for (int i = 0; i < skuArray.length(); i++) {
                                String priceText = products.get(i).priceText;
                                Double priceTextfloat = products.get(i).priceValue;
                                ((app) getApplication()).setprice(priceTextfloat);

                                editor.putString("pricetext",priceText).apply();
                            }
                        } catch (Exception e) {
                            editor.putString("pricetext",e.getLocalizedMessage()).apply();
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onSkuDetailsError(String error) {
                        editor.putString("pricetext",error).apply();
                    }
                });
                bp.getSubscriptionListingDetailsAsync("30days", new BillingProcessor.ISkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(@Nullable List<SkuDetails> products) {
                        try {
                            JSONArray skuArray = new JSONArray(products);
                            for (int i = 0; i < skuArray.length(); i++) {
                                String priceText = products.get(i).priceText;
                                Double priceTextfloat = products.get(i).priceValue;
                                ((app) getApplication()).setprice30(priceTextfloat);
                                editor.putString("pricetext30",priceText).apply();
                            }
                        } catch (Exception e) {
                            editor.putString("pricetext30",e.getLocalizedMessage()).apply();
                            editor.putString("pricetext30",e.getLocalizedMessage()).apply();

                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onSkuDetailsError(String error) {
                        editor.putString("pricetext30",error).apply();
                        editor.putString("pricetext30",error).apply();
                    }
                });
            }
        });

        bp.initialize();
        boolean isConnected = bp.isConnected();

        MobileAds.initialize(this, initializationStatus -> {

        });
adRequest =
        new AdRequest.Builder()
                .build();


        editor.putBoolean("boost", true).apply();
        editor.putBoolean("clean", true).apply();
        editor.putBoolean("cool", true).apply();
        editor.putBoolean("batterysaver", true).apply();
        Window window =getWindow();

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.WHITE);

        getSupportFragmentManager().beginTransaction().add(R.id.container, fragmentSplash).commit();

            new WindowInsetsControllerCompat(window,getCurrentFocus()).setAppearanceLightStatusBars(true);

    }

    @Override
    public void permission(boolean given) {
        if(!given){
            hasManageExternalStoragePermission();
        }
    }

    @Override
    public void show(Fragment fragment, Fragment fragment1) {
        if(fragment.toString().contains("FragmentCleanFinished")){

            InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            mInterstitialAd = interstitialAd;
                            if(!((app) getApplication()).getsubscribe()){
                                interstitialAd.show(MainActivity.this);

                            }


                            getSupportFragmentManager().beginTransaction().remove(fragment1).add(R.id.container, fragment).commit();
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            Log.i(TAG, loadAdError.getMessage());
                            mInterstitialAd = null;
                            getSupportFragmentManager().beginTransaction().remove(fragment1).add(R.id.container, fragment).commit();

                        }
                    });
        }
        else {

            getSupportFragmentManager().beginTransaction().remove(fragment1).add(R.id.container, fragment).commit();
        }

    }

    @Override
    public void delete(ArrayList<Integer> integers, ArrayList<Uri> uris, ArrayList<Integer> ids) {

    }

    @Override
    public void subscribe() {
        bp.subscribe(this, "30days");
    }

    @Override
    public void subscribeYearly() {
        bp.subscribe(this, "30days");
    }

    @Override
    public void loadAd(AdView adView) {

        dialog= ProgressDialog.show(this, "",
                "Loading. Please wait...", true);
        dialog.show();
        if(!((app) getApplication()).getsubscribe()){
            adView.loadAd(adRequest);
        }
        else {
            dialog.dismiss();
        }
       adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
         dialog.dismiss();
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }

    @Override
    public void loadInter() {


    }
}