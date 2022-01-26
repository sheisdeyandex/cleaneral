package com.fast.cleaneral;

import android.graphics.drawable.Drawable;

public class ModelDeleteApps {



        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public Drawable getIcon() {
            return icon;
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
        }

        public String getPackagename() {
            return packagename;
        }

        public void setPackagename(String packagename) {
            this.packagename = packagename;
        }

        String packagename;

    public String getLasttimeusage() {
        return lasttimeusage;
    }

    public void setLasttimeusage(String lasttimeusage) {
        this.lasttimeusage = lasttimeusage;
    }

    String lasttimeusage;
        Drawable icon;
        String Name;
        public boolean isCheckboxIsVisible() {
            return checkboxIsVisible;
        }

        public void setCheckboxIsVisible(boolean checkboxIsVisible) {
            this.checkboxIsVisible = checkboxIsVisible;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public   boolean checkboxIsVisible=false;//todo add setters and getters
        public boolean isChecked=false;//todo add setters and getters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

    public String getUsagetime() {
        return usagetime;
    }

    public void setUsagetime(String usagetime) {
        this.usagetime = usagetime;
    }

    String usagetime;
        int id;
        String size;

}
