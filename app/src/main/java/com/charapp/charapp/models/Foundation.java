package com.charapp.charapp.models;



/**
 * Created by dobit on 9/27/2017.
 */

public class Foundation {
    private String foundationName;
    private String foundationEmail;

    public Foundation() {
    }

    public Foundation(String foundationName, String foundationEmail) {
        this.foundationName = foundationName;
        this.foundationEmail = foundationEmail;
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

}
