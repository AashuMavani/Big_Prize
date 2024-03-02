package com.app.bigprize.Async.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Big_Home_Slider_Item implements Serializable {

    @SerializedName("image")
    private String image;

    @SerializedName("offerId")
    private String offerId;

    @SerializedName("screenNo")
    private String screenNo;

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;

    @SerializedName("matchId")
    private String matchId;

    public String getImage(){
        return image;
    }

    public String getOfferId(){
        return offerId;
    }

    public String getScreenNo(){
        return screenNo;
    }

    public String getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getUrl(){
        return url;
    }

    public String getMatchId(){
        return matchId;
    }

    public void setImage(String image) {
        this.image = image;
    }
}