package com.clean.cleaneral.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.view.Window;
import android.view.WindowManager;

import com.clean.cleaneral.FragmentSplash;
import com.clean.cleaneral.ui.fragments.FragmentMain;
import com.clean.cleaneral.R;
import com.clean.cleaneral.interfaces.FragmentInterface;

import static android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FragmentInterface {

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
                    intent.setData(Uri.parse("package:com.clean.cleaneral"));
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
                    intent.setData(Uri.parse("package:com.clean.cleaneral"));
                    startActivityForResult(intent, REQUEST_PERMISSION); //result code is just an int
                    return false;
                } catch (Exception e) {
                    return true; //if anything needs adjusting it would be this
                }
            }
        }
        return true; // assumed storage permissions granted
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences.Editor editor =getSharedPreferences("whattoshow", MODE_PRIVATE).edit();

        editor.putBoolean("boost", true).apply();
        editor.putBoolean("clean", true).apply();
        editor.putBoolean("cool", true).apply();
        editor.putBoolean("batterysaver", true).apply();
        Window window =getWindow();

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.WHITE);
        getSupportFragmentManager().beginTransaction().add(R.id.container, new FragmentSplash(getApplicationContext(), this)).commit();

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
        getSupportFragmentManager().beginTransaction().remove(fragment1).add(R.id.container, fragment).commit();

    }

    @Override
    public void delete(ArrayList<Integer> integers, ArrayList<Uri> uris, ArrayList<Integer> ids) {

    }
}