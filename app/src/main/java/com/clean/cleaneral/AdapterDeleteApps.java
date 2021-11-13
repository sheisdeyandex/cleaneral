package com.clean.cleaneral;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.clean.cleaneral.interfaces.FragmentInterface;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class AdapterDeleteApps extends RecyclerView.Adapter<AdapterDeleteApps.ViewHolder>{
    List<ModelDeleteApps> deleteAppsModels;
    Context context;
    ArrayList<Integer> positions = new ArrayList<>();
    ArrayList<Integer> ids = new ArrayList<>();
    ArrayList<Uri> uris = new ArrayList<>();
    FragmentAppManager fragmentDeleteApps;
FragmentInterface fragmentInterface;
    public int checked= 0;
    public AdapterDeleteApps(Context context, List<ModelDeleteApps> deleteAppsModels,  FragmentAppManager fragmentDeleteApps, FragmentInterface fragmentInterface) {
        this.fragmentDeleteApps = fragmentDeleteApps;
        this.context = context;
        this.deleteAppsModels = deleteAppsModels;
        this.fragmentInterface = fragmentInterface;

    }
    @Override
    public AdapterDeleteApps.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_apps, parent, false);
        return new ViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull AdapterDeleteApps.ViewHolder holder, int position) {
        final String name = deleteAppsModels.get(position).getName();
        final String packagename = deleteAppsModels.get(position).getPackagename();
        String size = deleteAppsModels.get(position).getSize();
        holder.checktodelete.setOnCheckedChangeListener(null);
        holder.checktodelete.setChecked(deleteAppsModels.get(position).checkboxIsVisible);
        int id = deleteAppsModels.get(position).getId();
        holder.checktodelete.setChecked(deleteAppsModels.get(position).isCheckboxIsVisible());
        if(deleteAppsModels.get(position).getUsagetime()==null){

            holder.usagetime.setText("0"+context.getResources().getString(R.string.hours));
        }
        else {
holder.usagetime.setText(deleteAppsModels.get(position).getUsagetime()+context.getResources().getString(R.string.hours));}
        if(deleteAppsModels.get(position).getLasttimeusage()==null) {

            holder.lasttimeusage.setText(context.getResources().getString(R.string.never));
        }
        else {
            holder.lasttimeusage.setText(deleteAppsModels.get(position).getLasttimeusage());
        }
        Drawable icon = deleteAppsModels.get(position).getIcon();
        Picasso.get().load("nothing").error(icon).placeholder(icon).into(holder.icon);
        holder.name.setText(name);
        holder.size.setText(size);
        holder.checktodelete.setOnCheckedChangeListener((buttonView, isChecked) -> {
            deleteAppsModels.get(position).setCheckboxIsVisible(isChecked);

            Uri packageURI = Uri.parse("package:"+packagename);
            if(isChecked){
                ids.add(id);
                checked+=1;
                positions.add(holder.getAdapterPosition());
                uris.add(packageURI);
                fragmentDeleteApps.binding.mbtnDelete.setOnClickListener(v -> {
                    fragmentInterface.delete(positions, uris, ids);

                });
            }
            else{
                if(checked!=0){
                      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                          positions.removeIf(new Predicate<Integer>() {
                              @Override
                              public boolean test(Integer integer) {
                                  if(integer==holder.getAdapterPosition()){
                                      return true;
                                  }
                                  return false;
                              }
                          });
                      }
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        uris.removeIf(new Predicate<Uri>() {
                            @Override
                            public boolean test(Uri uri) {
                                if(uri.equals(packageURI)){
                                    return true;
                                }
                                return false;
                            }
                        });
                    }

                    checked-=1;}

            }
        });


    }
    @Override
    public int getItemCount() {
        return deleteAppsModels.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, size,usagetime,lasttimeusage;
        ConstraintLayout check;
        ImageView icon;
        MaterialCheckBox checktodelete;
        ConstraintLayout delete;
        public ViewHolder(View view) {
            super(view);
            usagetime = view.findViewById(R.id.tv_usage_time);
            lasttimeusage = view.findViewById(R.id.tv_used_time);
            name= view.findViewById(R.id.tv_app_name);
            icon= view.findViewById(R.id.iv_app_icon);
            checktodelete = view.findViewById(R.id.checktodelete);
            size = view.findViewById(R.id.tv_app_size);
        }
    }}



