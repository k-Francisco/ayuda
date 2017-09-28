package com.charapp.charapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.charapp.ayuda.R;
import com.charapp.charapp.ViewHolder.EventViewHolder;
import com.charapp.charapp.models.Event;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {

    private List<Event> eventsList;
    private Context context;

    public EventAdapter(Context context, List<Event> eventsList) {
        this.eventsList = eventsList;
        this.context = context;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EventViewHolder viewHolder;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activities_card_layout, parent, false);
        viewHolder = new EventViewHolder(layoutView, eventsList);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final EventViewHolder holder, int position) {

        holder.tvName.setText(eventsList.get(position).getActivityName());
        holder.tvDate.setText(eventsList.get(position).getDate());
        holder.tvDesc.setText(eventsList.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return this.eventsList.size();
    }




}
