package com.telerikacademy.jasmine.thebucketlistapp.models;

import com.telerik.everlive.sdk.core.model.system.User;

import java.util.ArrayList;
import java.util.List;

public class LoggedUser {
    private static LoggedUser instance;

    private User loggedUser;

    private List<Goal> goals;

    private List<Idea> ideas;

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

    private LoggedUser() {
        this.goals = new ArrayList<Goal>();
        this.ideas = new ArrayList<Idea>();
    }

    public static LoggedUser getInstance() {
        if (instance == null) {
            instance = new LoggedUser();
        }
        return instance;
    }
}
