package com.charapp.charapp.models;

/**
 * Created by dobit on 9/14/2017.
 */

public class Volunteer {
    private String volunteerName;
    private String[] activitiesJoined;
    private String[] goodsToDonate;
    private String typeOfGoods;
    private int numberOfGoodsToDonate;
    private int[] volunteerPics;

    public String getVolunteerName() {
        return volunteerName;
    }

    public void setVolunteerName(String volunteerName) {
        this.volunteerName = volunteerName;
    }

    public String[] getActivitiesJoined() {
        return activitiesJoined;
    }

    public void setActivitiesJoined(String[] activitiesJoined) {
        this.activitiesJoined = activitiesJoined;
    }

    public String[] getGoodsToDonate() {
        return goodsToDonate;
    }

    public void setGoodsToDonate(String[] goodsToDonate) {
        this.goodsToDonate = goodsToDonate;
    }

    public String getTypeOfGoods() {
        return typeOfGoods;
    }

    public void setTypeOfGoods(String typeOfGoods) {
        this.typeOfGoods = typeOfGoods;
    }

    public int getNumberOfGoodsToDonate() {
        return numberOfGoodsToDonate;
    }

    public void setNumberOfGoodsToDonate(int numberOfGoodsToDonate) {
        this.numberOfGoodsToDonate = numberOfGoodsToDonate;
    }

    public int[] getVolunteerPics() {
        return volunteerPics;
    }

    public void setVolunteerPics(int[] volunteerPics) {
        this.volunteerPics = volunteerPics;
    }
}
