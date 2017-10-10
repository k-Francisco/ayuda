package com.charapp.charapp.ViewHolder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.charapp.ayuda.R;
import com.charapp.charapp.Utilities.UtilitiesApplication;
import com.charapp.charapp.models.Event;
import com.charapp.charapp.views.ViewMyActivityActivity;

import java.util.List;

public class EventViewHolder extends RecyclerView.ViewHolder {
    private final String TAG = EventViewHolder.class.getSimpleName();

    public View mCardView;
    public TextView tvName, tvDate, tvDesc, tvViewMore;
    public ImageView mViewIcon, mDeleteIcon, mEditICon;
    public EditText mName, mDate, mStart, mEnd, mAddress, mDesc;
    private List<Event> eventObject;
    private Context context;
    private int position;
    private String identity;
    UtilitiesApplication utilitiesApplication = new UtilitiesApplication();


    public EventViewHolder(final Context context, final View itemView, final List<Event> eventObject, String identity) {
        super(itemView);
        this.context = context;
        this.eventObject = eventObject;
        this.identity = identity;

        mCardView = (CardView) itemView.findViewById(R.id.card_view);
        mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = (int) view.getTag();
            }
        });


        tvName = (TextView) itemView.findViewById(R.id.eventName);
        tvDate = (TextView) itemView.findViewById(R.id.eventDate);
        tvDesc = (TextView) itemView.findViewById(R.id.eventDesc);

        tvViewMore = (TextView) itemView.findViewById(R.id.tvViewMore);
        LinearLayout ll = (LinearLayout)itemView.findViewById(R.id.lLayoutFoundation);

        if(identity.equals("foundation")){
            tvViewMore.setVisibility(View.INVISIBLE);
        }else if(identity.equals("volunteer")){
            ll.setVisibility(View.GONE);
        }

        tvViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "insert 'view more' actions here", Toast.LENGTH_LONG).show();
            }
        });

        mViewIcon = (ImageView) itemView.findViewById(R.id.ivView);
        mEditICon = (ImageView) itemView.findViewById(R.id.ivEdit);
        mDeleteIcon = (ImageView) itemView.findViewById(R.id.ivTrash);

        mViewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("NAME", eventObject.get(position).getActivityName());
                bundle.putString("DATE", eventObject.get(position).getDate());
                bundle.putString("START", eventObject.get(position).getTimeStart());
                bundle.putString("END", eventObject.get(position).getTimeEnd());
                bundle.putString("ADDRESS", eventObject.get(position).getAddress());
                bundle.putString("DESC", eventObject.get(position).getDescription());

                ViewMyActivityActivity.getInstance().viewEvent(bundle, position);

            }
        });

        mDeleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowConfirmDialog(context, position);

            }
        });

        mEditICon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View mView = mInflater.inflate(R.layout.update_event, null);
                alertDialog.setTitle("Edit event...");

                mName = (EditText) mView.findViewById(R.id.edtActivityName);
                mDate = (EditText) mView.findViewById(R.id.edtDate);
                mStart = (EditText) mView.findViewById(R.id.edtTimeStart);
                mEnd = (EditText) mView.findViewById(R.id.edtTimeEnd);
                mAddress = (EditText) mView.findViewById(R.id.edtAddress);
                mDesc = (EditText) mView.findViewById(R.id.edtDescription);


                mName.setText(eventObject.get(position).getActivityName());
                mDate.setText(eventObject.get(position).getDate());
                mStart.setText(eventObject.get(position).getTimeStart());
                mEnd.setText(eventObject.get(position).getTimeEnd());
                mAddress.setText(eventObject.get(position).getAddress());
                mDesc.setText(eventObject.get(position).getDescription());

                alertDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (!validateForm()) {
                            return;
                        } else {
                            Event event = new Event();
                            event.setActivityName(mName.getText().toString());
                            event.setDate(mDate.getText().toString());
                            event.setTimeStart(mStart.getText().toString());
                            event.setTimeEnd(mEnd.getText().toString());
                            event.setAddress(mAddress.getText().toString());
                            event.setDescription(mDesc.getText().toString());

                            ViewMyActivityActivity.getInstance().updateEvent(event, position);

                        }
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog builder = alertDialog.create();
                builder.setView(mView);
                builder.show();
            }
        });







    }


    private boolean validateForm() {
        boolean valid = true;
        String name = mName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            mName.setError("Required.");
            valid = false;
        } else {
            mName.setError(null);
        }

        String date = mDate.getText().toString();
        if (TextUtils.isEmpty(date)) {
            mDate.setError("Required.");
            valid = false;
        } else {
            mDate.setError(null);
        }

        String time = mStart.getText().toString();
        if (TextUtils.isEmpty(time)) {
            mStart.setError("Required.");
            valid = false;
        } else {
            mStart.setError(null);
        }

        String time2 = mEnd.getText().toString();
        if (TextUtils.isEmpty(time2)) {
            mEnd.setError("Required.");
            valid = false;
        } else {
            mEnd.setError(null);
        }

        String address = mAddress.getText().toString();
        if (TextUtils.isEmpty(address)) {
            mAddress.setError("Required.");
            valid = false;
        } else {
            mAddress.setError(null);
        }

        String desc = mDesc.getText().toString();
        if (TextUtils.isEmpty(desc)) {
            mDesc.setError("Required.");
            valid = false;
        } else {
            mDesc.setError(null);
        }
        return valid;
    }

    public static void ShowConfirmDialog(Context context, final int position) {
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
        alertDialogBuilder
                .setMessage("Are you sure you want to delete this event?")
                .setCancelable(true)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ViewMyActivityActivity.getInstance().deleteEvent(position);
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }




}
