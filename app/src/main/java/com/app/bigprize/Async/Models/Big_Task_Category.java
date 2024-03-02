package com.app.bigprize.Async.Models;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class Big_Task_Category {
    @Expose
    private String name;

    @Expose
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String title) {
        this.name = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
