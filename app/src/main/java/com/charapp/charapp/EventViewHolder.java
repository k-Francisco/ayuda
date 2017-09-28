package com.charapp.charapp;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.charapp.ayuda.R;
import com.charapp.charapp.models.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class EventViewHolder extends RecyclerView.ViewHolder {
    private final String TAG = EventViewHolder.class.getSimpleName();

    public TextView tvName, tvDate, tvDesc, tvViewMore;
    public ImageView mViewIcon, mDeleteIcon, mEditICon;
    private List<Event> eventObject;


    public EventViewHolder(final View itemView, final List<Event> eventObject) {
        super(itemView);
        this.eventObject = eventObject;

        tvName = (TextView) itemView.findViewById(R.id.eventName);
        tvDate = (TextView) itemView.findViewById(R.id.eventDate);
        tvDesc = (TextView) itemView.findViewById(R.id.eventDesc);

        mViewIcon = (ImageView) itemView.findViewById(R.id.ivView);
        mEditICon = (ImageView) itemView.findViewById(R.id.ivEdit);
        mDeleteIcon = (ImageView) itemView.findViewById(R.id.ivTrash);

        mViewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(), "view event", Toast.LENGTH_SHORT).show();
            }
        });

        mDeleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventTitle = eventObject.get(getAdapterPosition()).getActivityName();
                Log.d(TAG, "Event Title" + eventTitle);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query query = ref.orderByChild("activities").equalTo(eventTitle);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().removeValue();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });
            }
        });

        //check if foundation/volunteer side para ma determine if ang layout/textview ang e hide

//            LinearLayout ll = (LinearLayout)itemView.findViewById(R.id.lLayoutFoundation);
//            ll.setVisibility(View.GONE);

        tvViewMore = (TextView) itemView.findViewById(R.id.tvViewMore);
        tvViewMore.setVisibility(View.INVISIBLE);


    }
}
