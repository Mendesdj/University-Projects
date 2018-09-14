package com.smartDoor.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smartDoor.R;
import com.smartDoor.constants.constants;
import com.smartDoor.shared.Entry;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;


public class AdapterEntries extends RecyclerView.Adapter<AdapterEntries.ViewHolder> {

    private List<Entry> entriesList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView entranceText, dateText, usernameText;
        protected ImageView userImg;
        public ViewHolder (View itemView) {

            super(itemView);
            entranceText = itemView.findViewById(R.id.info_entrance);
            dateText = itemView.findViewById(R.id.info_time);
            usernameText = itemView.findViewById(R.id.info_username);
            userImg = itemView.findViewById(R.id.avatarImageView);
        }
    }

    public AdapterEntries(List<Entry> list) {
        entriesList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Entry entry = entriesList.get(position);
        if (entry.getenter()) {
            holder.entranceText.setText("Entrada");
        }
        else {
            holder.entranceText.setText("Saída");
        }

        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String data = isoFormat.format(entry.getdate());

        holder.dateText.setText(data);
        holder.usernameText.setText(entry.getusername());

        if (entry.getisAdmin()) {
            Glide.with(holder.userImg.getContext()).load(constants.adminImgString).into(holder.userImg);
        }
        else {
            Glide.with(holder.userImg.getContext()).load(constants.userImgString).into(holder.userImg);
        }

    }

    @Override
    public int getItemCount() {
        return entriesList.size();
    }
}