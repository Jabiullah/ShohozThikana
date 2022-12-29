package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GeoLocationAdapter extends RecyclerView.Adapter<GeoLocationAdapter.GeoLocationDetailsViewHolder>{
    private Context mCtx;
    private List<GeoLocationDetails> geoLocationList;
    public GeoLocationAdapter(Context mCtx, List<GeoLocationDetails> geoLocationList) {
        this.mCtx = mCtx;
        this.geoLocationList = geoLocationList;
    }
    @NonNull
    @Override
    public GeoLocationAdapter.GeoLocationDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.geolist, null);
        return new GeoLocationAdapter.GeoLocationDetailsViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull GeoLocationAdapter.GeoLocationDetailsViewHolder holder, int position) {
        GeoLocationDetails n1 = geoLocationList.get(position);
        holder.GeoCode.setText(n1.getGeocode());
        holder.address.setText(n1.getLocationHouse()+" , "+n1.getLocationName()+" , "+n1.getLocationStreet());
    }
    @Override
    public int getItemCount() {return geoLocationList.size();}
    public class GeoLocationDetailsViewHolder extends RecyclerView.ViewHolder {
        TextView GeoCode,address, voiceFile;
        public GeoLocationDetailsViewHolder(View itemView) {
            super(itemView);

            GeoCode     = itemView.findViewById(R.id.geoCodeValue);
            address     = itemView.findViewById(R.id.address);

        }
    }
}
