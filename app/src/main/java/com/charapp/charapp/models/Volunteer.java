package com.charapp.charapp.models;

import java.util.List;

/**
 * Created by dobit on 9/14/2017.
 */

public class Volunteer {
    private String volunteerName;
    private String volunteerEmail;
    private List<Event> eventsJoined;


    public Volunteer(String volunteerName, String volunteerEmail, List<Event> eventsJoined) {
        this.volunteerName = volunteerName;
        this.volunteerEmail = volunteerEmail;
        this.eventsJoined = eventsJoined;
    }

    public Volunteer() {
    }

    public Volunteer(String volunteerName, String volunteerEmail) {
        this.volunteerName = volunteerName;
        this.volunteerEmail = volunteerEmail;
    }

    public List<Event> getEventsJoined() {
        return eventsJoined;
    }

    public void setEventsJoined(List<Event> eventsJoined) {
        this.eventsJoined = eventsJoined;
    }

    public String getVolunteerName() {
        return volunteerName;
    }

    public void setVolunteerName(String volunteerName) {
        this.volunteerName = volunteerName;
    }

    public String getVolunteerEmail() {
        return volunteerEmail;
    }

    public void setVolunteerEmail(String volunteerEmail) {
        this.volunteerEmail = volunteerEmail;
    }
}
