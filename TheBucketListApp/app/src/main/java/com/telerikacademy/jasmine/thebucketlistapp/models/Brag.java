package com.telerikacademy.jasmine.thebucketlistapp.models;

import com.google.gson.annotations.SerializedName;
import com.telerik.everlive.sdk.core.model.base.DataItem;
import com.telerik.everlive.sdk.core.serialization.ServerIgnore;
import com.telerik.everlive.sdk.core.serialization.ServerProperty;
import com.telerik.everlive.sdk.core.serialization.ServerType;

import java.util.UUID;

@ServerType("Brags")
public class Brag extends DataItem{

    public Brag() {}

    public Brag(String title, String description, UUID goalId) {
        this();
        this.title = title;
        this.description = description;
        this.goalId = goalId;
    }


    @ServerProperty("Title")
    @SerializedName("Title")
    private String title;

    @ServerProperty("Description")
    @SerializedName("Description")
    private String description;

    @ServerProperty("LikeCount")
    @SerializedName("LikeCount")
    private int likeCount;

    @ServerProperty("Goal")
    @SerializedName("Goal")
    private UUID goalId;

    @ServerProperty("Cover")
    @SerializedName("Cover")
    private UUID cover;

    @ServerIgnore
    private String authorName;

    public UUID getCover() {
        return cover;
    }

    public void setCover(UUID cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public UUID getGoalId() {
        return goalId;
    }

    public void setGoalId(UUID goalId) {
        this.goalId = goalId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
