package com.charapp.charapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.charapp.ayuda.R;
import com.charapp.charapp.Utilities.UtilitiesApplication;
import com.charapp.charapp.ViewHolder.EventViewHolder;
import com.charapp.charapp.models.Event;


import java.util.List;
import java.util.Random;

public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {

    private List<Event> eventsList;
    private Context context;
    private String identity;
    UtilitiesApplication utilitiesApplication = new UtilitiesApplication();


    public EventAdapter(Context context, List<Event> eventsList, String identity) {
        this.eventsList = eventsList;
        this.context = context;
        this.identity = identity;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EventViewHolder viewHolder;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activities_card_layout_2, parent, false);
        viewHolder = new EventViewHolder(context, layoutView, eventsList, identity);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final EventViewHolder holder, int position) {
        holder.mCardView.setTag(position);
        holder.tvViewMore.setTag(position);
        holder.tvName.setText(eventsList.get(position).getActivityName());
        holder.tvDate.setText(eventsList.get(position).getDate());
        holder.tvDesc.setText(eventsList.get(position).getDescription());

        Random rand = new Random();
        int  n = rand.nextInt(4) + 1;
        switch (n){
            case 1:
             holder.mImgCoverPhoto.setImageResource(R.drawable.img_helping_hand_1);
                break;
            case 2:
                holder.mImgCoverPhoto.setImageResource(R.drawable.img_helping_hand_2);
                break;
            case 3:
                holder.mImgCoverPhoto.setImageResource(R.drawable.img_helping_hand_3);
                break;
            case 4:
                holder.mImgCoverPhoto.setImageResource(R.drawable.img_helping_hand_4);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return this.eventsList.size();
    }


}
