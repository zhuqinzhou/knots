package com.example.qzhu1.myapplication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Felix on 4/26/16.
 */
@JsonIgnoreProperties({"selection"})
public class User implements Serializable {
    private static final long serialVersionUID = 1751526128880965987L;

    public User(){}

    public User(String email){
        this.email = email;
        this.friends = "";
        this.groups = "";
        this.owe = "";
        this.owed = "";
        this.avatar = "";
    }

    String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFriends() {
        return friends;
    }

    public void setFriends(String friends) {
        this.friends = friends;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public String getOwe() {
        return owe;
    }

    public void setOwe(String owe) {
        this.owe = owe;
    }

    public String getOwed() {
        return owed;
    }

    public void setOwed(String owed) {
        this.owed = owed;
    }

    String friends, groups;
    String owe, owed;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    String avatar;




}
