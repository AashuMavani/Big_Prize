package com.app.bigprize.Async.Models;

import com.google.gson.annotations.Expose;

public class Big_TaskOfferFootSteps {
    @Expose
    private String bgcolor;
    @Expose
    private String fontcolor;
    @Expose
    private String steps;

    public String getBgcolor() {
        return bgcolor;
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public String getFontcolor() {
        return fontcolor;
    }

    public void setFontcolor(String fontcolor) {
        this.fontcolor = fontcolor;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }
}
