package com.charapp.charapp.models;

import java.util.ArrayList;

/**
 * Created by dobit on 9/27/2017.
 */

public class Foundation {
    private String foundationName;
    private String foundationEmail;
    private ArrayList<Event> events;

    public Foundation() {
    }

    public Foundation(String foundationName, String foundationEmail) {
        this.foundationName = foundationName;
        this.foundationEmail = foundationEmail;
    }

    public Foundation(String foundationName, String foundationEmail, ArrayList<Event> events) {
        this.foundationName = foundationName;
        this.foundationEmail = foundationEmail;
        this.events = events;
    }

    public String getFoundationName() {
        return foundationName;
    }

    public void setFoundationName(String foundationName) {
        this.foundationName = foundationName;
    }

    public String getFoundationEmail() {
        return foundationEmail;
    }

    public void setFoundationEmail(String foundationEmail) {
        this.foundationEmail = foundationEmail;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
