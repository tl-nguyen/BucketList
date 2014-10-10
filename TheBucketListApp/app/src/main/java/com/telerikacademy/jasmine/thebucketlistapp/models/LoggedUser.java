package com.telerikacademy.jasmine.thebucketlistapp.models;

import com.telerik.everlive.sdk.core.model.system.User;

public class LoggedUser {
    private static LoggedUser instance;

    private User loggedUser;

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    private LoggedUser() {
    }

    public static LoggedUser getInstance() {
        if (instance == null) {
            instance = new LoggedUser();
        }
        return instance;
    }
}
