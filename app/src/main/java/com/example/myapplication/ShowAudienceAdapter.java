package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShowAudienceAdapter extends RecyclerView.Adapter<ShowAudienceAdapter.AudienceDetailsViewHolder>{
    private Context mCtx;
    private List<AudienceDetails> audienceDetailsList;

    public ShowAudienceAdapter(Context mCtx, List<AudienceDetails> audienceDetailsList) {
        this.mCtx = mCtx;
        this.audienceDetailsList = audienceDetailsList;
    }
    @NonNull
    @Override
    public ShowAudienceAdapter.AudienceDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.audiencelist, null);
        return new ShowAudienceAdapter.AudienceDetailsViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ShowAudienceAdapter.AudienceDetailsViewHolder holder, int position) {
        AudienceDetails n1 = audienceDetailsList.get(position);
        holder.Phone.setText(n1.getPhone());
        holder.Name.setText(n1.getAudienceName());
    }

    @Override
    public int getItemCount() {return audienceDetailsList.size();}
    public class AudienceDetailsViewHolder extends RecyclerView.ViewHolder {
        TextView Phone, Name;
        public AudienceDetailsViewHolder(View itemView) {
            super(itemView);
            Phone       = itemView.findViewById(R.id.UserPhone);
            Name        = itemView.findViewById(R.id.UserName);
        }
    }
}
