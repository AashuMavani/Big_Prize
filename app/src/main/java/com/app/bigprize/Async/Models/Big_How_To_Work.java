package com.app.bigprize.Async.Models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

@SuppressWarnings("unused")
public class Big_How_To_Work implements Serializable {

    @Expose
    private String description;
    @Expose
    private String icon;
    @Expose
    private String id;
    @Expose
    private String title;
    @Expose
    private String points;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoints() {
        return points;
    }
}

