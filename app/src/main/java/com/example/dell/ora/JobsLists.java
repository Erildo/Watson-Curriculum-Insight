package com.example.dell.ora;

import java.util.Date;
public class JobsLists {

    private String title, co_name,skills,location,description;
    private Date date;

    public JobsLists() {
    }

    public JobsLists(String title, String co_name, Date date,String skills,String location,String description) {
        this.title = title;
        this.co_name = co_name;
        this.date = date;
        this.skills = skills;
        this.location = location;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public String getCo_Name() {
        return co_name;
    }

    public void setCo_Name(String co_name) {
        this.co_name = co_name;
    }
    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
