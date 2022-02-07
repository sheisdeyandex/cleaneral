package com.fast.cleaneral;

import android.app.Application;

import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

public class app extends Application {
public double price30=0;

    public Double getprice30() {
        return price30;
    }

    public void setprice30(Double price30) {
        this.price30 = price30;
    }
    public double price=0;

    public Double getprice() {
        return price;
    }

    public void setprice(Double price) {
        this.price = price;
    }
    public boolean activesubscribe=false;
    public boolean getsubscribe() {
        return activesubscribe;
    }

    public void setgetsubscribe(boolean activesubscribe) {
        this.activesubscribe = activesubscribe;

    }
    private static AppOpenManager appOpenManager;
    @Override
    public void onCreate() {
        super.onCreate();
        // Creating an extended library configuration.
        YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder("675b437a-ce0f-4846-9ddc-4bbdc19c88b9").build();
        // Initializing the AppMetrica SDK.
        YandexMetrica.activate(getApplicationContext(), config);
        // Automatic tracking of user activity.
        YandexMetrica.enableActivityAutoTracking(this);
        appOpenManager = new AppOpenManager(this);
    }
}
