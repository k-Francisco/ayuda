package com.charapp.charapp.models;

/**
 * Created by dobit on 9/28/2017.
 */

public class VolunteerProfile {
    private String volunteerName;
    private String volunteerAddress;
    private String contactNumber;
    private String volunteerAbout;
    private int[] volunteerPics;

    public String getVolunteerName() {
        return volunteerName;
    }

    public void setVolunteerName(String volunteerName) {
        this.volunteerName = volunteerName;
    }

    public String getVolunteerAddress() {
        return volunteerAddress;
    }

    public void setVolunteerAddress(String volunteerAddress) {
        this.volunteerAddress = volunteerAddress;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getVolunteerAbout() {
        return volunteerAbout;
    }

    public void setVolunteerAbout(String volunteerAbout) {
        this.volunteerAbout = volunteerAbout;
    }

    public int[] getVolunteerPics() {
        return volunteerPics;
    }

    public void setVolunteerPics(int[] volunteerPics) {
        this.volunteerPics = volunteerPics;
    }
}
