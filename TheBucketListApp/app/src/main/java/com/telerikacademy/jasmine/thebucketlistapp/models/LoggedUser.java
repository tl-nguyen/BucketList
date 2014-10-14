package com.telerikacademy.jasmine.thebucketlistapp.models;

import android.graphics.Bitmap;

import com.telerik.everlive.sdk.core.model.system.User;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

public class LoggedUser {
    private static LoggedUser instance;

    private User loggedUser;

    private List<Goal> goals;

    private List<Idea> ideas;

    private Hashtable<UUID, Bitmap> pictures;

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public List<Idea> getIdeas() {
        return ideas;
    }

    public void addPicture(UUID id, Bitmap image) {
        this.pictures.put(id, image);
    }

    public Bitmap getPictureById(UUID id) {
        return this.pictures.get(id);
    }

    private LoggedUser() {
        this.goals = new ArrayList<Goal>();
        this.ideas = new ArrayList<Idea>();
        this.pictures = new Hashtable<UUID, Bitmap>();
    }

    public static LoggedUser getInstance() {
        if (instance == null) {
            instance = new LoggedUser();
        }
        return instance;
    }
}
