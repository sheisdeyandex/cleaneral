package com.fast.cleaneral.interfaces;

import android.net.Uri;

import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public interface FragmentInterface {
    void permission(boolean given);
    void show(Fragment fragment, Fragment fragment1);
    void delete(ArrayList<Integer> integers, ArrayList<Uri> uris, ArrayList<Integer> ids);
}
