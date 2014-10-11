package com.telerikacademy.jasmine.thebucketlistapp.models;

import com.telerik.everlive.sdk.core.model.system.User;

import java.util.ArrayList;
import java.util.List;

public class LoggedUser {
    private static LoggedUser instance;

    private User loggedUser;

    private List<Goal> goals;

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }

    private LoggedUser() {
        this.goals = new ArrayList<Goal>();
    }

    public static LoggedUser getInstance() {
        if (instance == null) {
            instance = new LoggedUser();
        }
        return instance;
    }
}
