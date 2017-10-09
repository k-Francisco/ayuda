package com.charapp.charapp.Utilities;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.charapp.charapp.adapter.EventAdapter;
import com.charapp.charapp.models.Event;
import com.charapp.charapp.models.Foundation;
import com.charapp.charapp.views.ViewMyActivityActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by dobit on 9/26/2017.
 */

public class UtilitiesApplication extends Application {

    private UtilitiesApplication mInstance;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private NotificationCompat.Builder notification;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedpreferences.edit();
        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);
    }


    public NotificationCompat.Builder getNotification() {
        return notification;
    }

    public SharedPreferences getSharedpreferences() {
        return sharedpreferences;
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public UtilitiesApplication getmInstance() {
        return mInstance;
    }

    public void setmInstance(UtilitiesApplication mInstance) {
        this.mInstance = mInstance;
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public void setFirebaseAuth(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public void setFirebaseUser(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
    }


    public void addEvent(Event event, DatabaseReference databaseReference, String id){
        databaseReference.child(id).setValue(event);
    }
    public void addEvent(Foundation foundation, DatabaseReference databaseReference, String id){
        databaseReference.child(id).setValue(foundation);
    }

    public void deleteEvent(DataSnapshot dataSnapshot, DatabaseReference dbRef, Context context, EventAdapter adapter){
        //TODO deletion of event here including deletion of Firebase event entry
        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
            String key = singleSnapshot.getKey();
            dbRef.child(key).removeValue();

//            String eventTitle = singleSnapshot.getValue(String.class);
//            for (int i = 0; i < arrayListItem.size(); i++) {
//                if (arrayListItem.get(i).getActivityName().equals(eventTitle)) {
//                    arrayListItem.remove(i);
//                }
//            }
//            Log.d(TAG, "Event tile " + eventTitle);

        }
        adapter.notifyDataSetChanged();

    }

    public void getAllEvents(DataSnapshot dataSnapshot, ArrayList<Event> arrayListItem, EventAdapter adapter, Context context) {
        Event event = dataSnapshot.getValue(Event.class);
        arrayListItem.add(event);
        adapter.notifyDataSetChanged();
    }

    public void joinEvent(Event event, DatabaseReference databaseReference, String id){
        databaseReference.child(id).setValue(event);
    }

}
