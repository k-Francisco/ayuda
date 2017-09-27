package com.charapp.charapp.models;

/**
 * Created by honey on 9/28/2017.
 */

public class Event {

    public Event() {
    }

    public Event(String activityName, String date, String timeStart, String timeEnd, String address, String description) {
        this.activityName = activityName;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.address = address;
        this.description = description;
    }


    private String activityName;
    private String date;
    private String timeStart;
    private String timeEnd;
    private String address;
    private String description;

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
