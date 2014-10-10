package com.telerikacademy.jasmine.thebucketlistapp.models;

import com.telerik.everlive.sdk.core.model.base.DataItem;
import com.telerik.everlive.sdk.core.serialization.ServerProperty;
import com.telerik.everlive.sdk.core.serialization.ServerType;

import java.util.UUID;

@ServerType("Ideas")
public class Idea extends DataItem {

    public Idea (String title, String description) {
        this.title = title;
        this.description = description;
    }

    @ServerProperty("Title")
    private String title;

    @ServerProperty("Description")
    private String description;

    @ServerProperty("Owner")
    private UUID userId;

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

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

}
