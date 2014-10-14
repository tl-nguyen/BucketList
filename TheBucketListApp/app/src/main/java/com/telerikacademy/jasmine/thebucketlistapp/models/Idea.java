package com.telerikacademy.jasmine.thebucketlistapp.models;

import com.google.gson.annotations.SerializedName;
import com.telerik.everlive.sdk.core.model.base.DataItem;
import com.telerik.everlive.sdk.core.serialization.ServerIgnore;
import com.telerik.everlive.sdk.core.serialization.ServerProperty;
import com.telerik.everlive.sdk.core.serialization.ServerType;

import java.util.UUID;

@ServerType("Ideas")
public class Idea extends DataItem {

    public Idea() {}

    public Idea(String title, String description) {
        this();
        this.title = title;
        this.description = description;
    }

    @ServerProperty("Title")
    @SerializedName("Title")
    private String title;

    @ServerProperty("Description")
    @SerializedName("Description")
    private String description;

    @ServerIgnore
    private String authorName;

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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

}
