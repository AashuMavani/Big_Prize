
package com.app.bigprize.Async.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class Big_AdjoeLeaderboardHistoryItem {
    @Expose
    private String id;
    @Expose
    private String declarationDate;

    @SerializedName("data")
    private List<Big_AdjoeLeaderboardItem> data;

    public String getId() {
        return id;
    }

    public String getDeclarationDate() {
        return declarationDate;
    }

    public List<Big_AdjoeLeaderboardItem> getData() {
        return data;
    }
}
