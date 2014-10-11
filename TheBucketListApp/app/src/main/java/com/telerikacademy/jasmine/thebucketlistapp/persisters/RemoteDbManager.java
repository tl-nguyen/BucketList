package com.telerikacademy.jasmine.thebucketlistapp.persisters;

import com.telerik.everlive.sdk.core.EverliveApp;
import com.telerik.everlive.sdk.core.model.system.User;
import com.telerik.everlive.sdk.core.query.definition.UserSecretInfo;
import com.telerik.everlive.sdk.core.query.definition.filtering.simple.ValueCondition;
import com.telerik.everlive.sdk.core.query.definition.filtering.simple.ValueConditionOperator;
import com.telerik.everlive.sdk.core.query.definition.sorting.SortDirection;
import com.telerik.everlive.sdk.core.query.definition.sorting.SortingDefinition;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;
import com.telerikacademy.jasmine.thebucketlistapp.models.Goal;
import com.telerikacademy.jasmine.thebucketlistapp.models.Idea;
import com.telerikacademy.jasmine.thebucketlistapp.models.LoggedUser;

public class RemoteDbManager {
    private static RemoteDbManager instance;

    private EverliveApp everlive;

    private RemoteDbManager() {
    }

    public static RemoteDbManager getInstance() {
        if (instance == null) {
            instance = new RemoteDbManager();
        }
        return instance;
    }

    public void setEverlive(String apiKey) {
        this.everlive = new EverliveApp(apiKey);
    }

    public void getMe(RequestResultCallbackAction callbackAction) {
        this.everlive.
                workWith().
                users().
                getMe().
                executeAsync(callbackAction);
    }

    public void register(User user, UserSecretInfo secretInfo, RequestResultCallbackAction callbackAction) {
        this.everlive.workWith().
                users().
                create(user, secretInfo).
                executeAsync(callbackAction);
    }

    public void login(String userName, String password, RequestResultCallbackAction callbackAction) {
        this.everlive.workWith().
                authentication().
                login(userName, password).
                executeAsync(callbackAction);
    }

    public void createIdea(Idea idea, RequestResultCallbackAction callbackAction) {
        this.everlive.workWith().
                data(Idea.class).
                create(idea).
                executeAsync(callbackAction);
    }

    public void createGoal(Goal goal, RequestResultCallbackAction callbackAction) {
        this.everlive.workWith().
                data(Goal.class).
                create(goal).
                executeAsync(callbackAction);
    }

    public void retrieveGoals(RequestResultCallbackAction callbackAction) {
        String loggedUserId = LoggedUser.getInstance().getLoggedUser().getId().toString();
        SortingDefinition sortAsc = new SortingDefinition("CreatedAt", SortDirection.Descending);

        this.everlive.workWith().
                data(Goal.class).
                getAll().
                where(new ValueCondition("Owner", loggedUserId, ValueConditionOperator.EqualTo)).
                sort(sortAsc).
                executeAsync(callbackAction);
    }
}
