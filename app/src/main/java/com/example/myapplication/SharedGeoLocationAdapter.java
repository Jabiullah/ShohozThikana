package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SharedGeoLocationAdapter extends RecyclerView.Adapter<SharedGeoLocationAdapter.SharedGeoLocationDetailsViewHolder>{
    private Context mCtx;
    private List<SharedGeoLocationDetails> sharedGeoLocationList;

    public SharedGeoLocationAdapter(Context mCtx, List<SharedGeoLocationDetails> sharedGeoLocationList) {
        this.mCtx = mCtx;
        this.sharedGeoLocationList = sharedGeoLocationList;
    }
    @NonNull
    @Override
    public SharedGeoLocationAdapter.SharedGeoLocationDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.sharedgeolist, null);
        return new SharedGeoLocationAdapter.SharedGeoLocationDetailsViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull SharedGeoLocationAdapter.SharedGeoLocationDetailsViewHolder holder, int position) {
        SharedGeoLocationDetails n1 = sharedGeoLocationList.get(position);
        holder.gCode.setText(n1.getGeocode());
        holder.address.setText(n1.getLocationHouse() + " , " + n1.getLocationName() + " , " + n1.getLocationStreet());
        holder.originalUser.setText("এই জিও কোডটি তৈরি করেছে - "+n1.getGeoCodeOwner());
        holder.houseCat.setText(n1.getLocationCategory());
    }

    @Override
    public int getItemCount() {return sharedGeoLocationList.size();}
    public class SharedGeoLocationDetailsViewHolder extends RecyclerView.ViewHolder {
        TextView gCode, address, voiceFile, houseCat, originalUser;
        public SharedGeoLocationDetailsViewHolder(View itemView) {
            super(itemView);
            gCode       = itemView.findViewById(R.id.gCodeValue);
            address     = itemView.findViewById(R.id.address);
            originalUser= itemView.findViewById(R.id.originalUser);
            houseCat    = itemView.findViewById(R.id.houseCategory);
        }
    }
}
