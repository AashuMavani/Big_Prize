package com.app.bigprize.Async.Models;

import com.google.gson.annotations.Expose;

public class BottomGrid {
    @Expose
    private String image;
    @Expose
    private String screenNo;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getScreenNo() {
        return screenNo;
    }

    public void setScreenNo(String screenNo) {
        this.screenNo = screenNo;
    }
}
