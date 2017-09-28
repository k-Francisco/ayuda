package com.charapp.charapp.models;

/**
 * Created by dobit on 9/28/2017.
 */

public class VolunteerDonation {
    private String volunteerName;
    private String[] goodsToDonate;
    private int numberOfGoodsToDonate;

    public String getVolunteerName() {
        return volunteerName;
    }

    public void setVolunteerName(String volunteerName) {
        this.volunteerName = volunteerName;
    }

    public String[] getGoodsToDonate() {
        return goodsToDonate;
    }

    public void setGoodsToDonate(String[] goodsToDonate) {
        this.goodsToDonate = goodsToDonate;
    }

    public int getNumberOfGoodsToDonate() {
        return numberOfGoodsToDonate;
    }

    public void setNumberOfGoodsToDonate(int numberOfGoodsToDonate) {
        this.numberOfGoodsToDonate = numberOfGoodsToDonate;
    }


}
