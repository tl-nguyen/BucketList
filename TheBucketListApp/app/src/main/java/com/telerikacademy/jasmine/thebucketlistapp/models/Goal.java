package com.telerikacademy.jasmine.thebucketlistapp.models;


import com.google.gson.annotations.SerializedName;
import com.telerik.everlive.sdk.core.model.base.DataItem;
import com.telerik.everlive.sdk.core.serialization.ServerIgnore;
import com.telerik.everlive.sdk.core.serialization.ServerProperty;
import com.telerik.everlive.sdk.core.serialization.ServerType;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@ServerType("Goals")
public class Goal extends DataItem {

    public Goal(String title, String description, UUID ideaId) {
        this.title = title;
        this.description = description;
        this.ideaId = ideaId;
        this.selected = false;
    }

    @ServerProperty("Title")
    @SerializedName("Title")
    private String title;

    @ServerProperty("Description")
    @SerializedName("Description")
    private String description;

    @ServerProperty("Done")
    @SerializedName("Done")
    private boolean isDone;

    @ServerProperty("Idea")
    @SerializedName("Idea")
    private UUID ideaId;

    @ServerProperty("Pictures")
    @SerializedName("Pictures")
    private ArrayList<UUID> pictures;

    @ServerIgnore
    private boolean selected;

    @ServerIgnore
    private Date modifiedAt;

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

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    public UUID getIdeaId() {
        return ideaId;
    }

    public void setIdeaId(UUID ideaId) {
        this.ideaId = ideaId;
    }

    public ArrayList<UUID> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<UUID> pictures) {
        this.pictures = pictures;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public Date getModifiedAt() {
        return modifiedAt;
    }

    @Override
    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}
