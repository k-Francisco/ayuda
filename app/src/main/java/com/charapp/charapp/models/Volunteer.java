package com.charapp.charapp.models;

/**
 * Created by dobit on 9/14/2017.
 */

public class Volunteer {
    private String volunteerName;
    private String volunteerEmail;

    public Volunteer() {
    }

    public Volunteer(String volunteerName, String volunteerEmail) {
        this.volunteerName = volunteerName;
        this.volunteerEmail = volunteerEmail;
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
