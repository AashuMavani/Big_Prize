package com.app.bigprize.Async.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Big_Api_Response implements Serializable {
    @SerializedName("encrypt")
    private String encrypt;

    public String getEncrypt() {
        return encrypt;
    }
    public void setEncrypt(String encrypt) {
        this.encrypt = encrypt;
    }


}
