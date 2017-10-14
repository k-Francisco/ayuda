package com.charapp.charapp.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import com.charapp.ayuda.R;
import com.charapp.charapp.Utilities.UtilitiesApplication;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by dobit on 10/10/2017.
 */

public class NotificationService extends Service {
    private NotificationCompat.Builder notification;
    private static final int notifyId = 1;
    private DatabaseReference mRef;
    private ChildEventListener childEventListener;
    private String userIdentity;
    private String fName;
    private boolean fromIntent;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mRef = FirebaseDatabase.getInstance().getReference("activities/");

        userIdentity = ((UtilitiesApplication)getApplication()).getSharedpreferences().getString("identity", "");
        fName = ((UtilitiesApplication)getApplication()).getSharedpreferences().getString("name", "");

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                notifyUser();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        if (userIdentity.equals("foundation")) {
            mRef = mRef.child(fName);
            mRef.addChildEventListener(childEventListener);
            notifyUser();

        } else {
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        mRef.child(ds.getKey()).addChildEventListener(childEventListener);
                        notifyUser();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

        return super.onStartCommand(intent, flags, startId);
    }

    public void notifyUser(){

            notification = ((UtilitiesApplication) getApplication()).getNotification();
            notification.setSmallIcon(R.drawable.logo_gradient);
            notification.setTicker("");
            notification.setWhen(System.currentTimeMillis());
            notification.setContentTitle("Event");
            notification.setContentText("A new event has been added");

            Intent intent = new Intent("notify_user");
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(notifyId, notification.build());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
