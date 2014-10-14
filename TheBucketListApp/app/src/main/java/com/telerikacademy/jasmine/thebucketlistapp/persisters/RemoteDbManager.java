package com.telerikacademy.jasmine.thebucketlistapp.persisters;

import com.telerik.everlive.sdk.core.EverliveApp;
import com.telerik.everlive.sdk.core.model.system.User;
import com.telerik.everlive.sdk.core.query.definition.UserSecretInfo;
import com.telerik.everlive.sdk.core.query.definition.filtering.Condition;
import com.telerik.everlive.sdk.core.query.definition.filtering.array.ArrayCondition;
import com.telerik.everlive.sdk.core.query.definition.filtering.array.ArrayConditionOperator;
import com.telerik.everlive.sdk.core.query.definition.filtering.compound.CompoundCondition;
import com.telerik.everlive.sdk.core.query.definition.filtering.compound.CompoundConditionOperator;
import com.telerik.everlive.sdk.core.query.definition.filtering.simple.ValueCondition;
import com.telerik.everlive.sdk.core.query.definition.filtering.simple.ValueConditionOperator;
import com.telerik.everlive.sdk.core.query.definition.sorting.SortDirection;
import com.telerik.everlive.sdk.core.query.definition.sorting.SortingDefinition;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;
import com.telerikacademy.jasmine.thebucketlistapp.models.Goal;
import com.telerikacademy.jasmine.thebucketlistapp.models.Idea;
import com.telerikacademy.jasmine.thebucketlistapp.models.LoggedUser;

import java.util.ArrayList;
import java.util.List;

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

    public void getIdeaOwner(final Idea idea, RequestResultCallbackAction callbackAction) {
        this.everlive.
                workWith().
                users().
                getById(idea.getOwner()).
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

    public void logout() {
        LoggedUser.getInstance().setLoggedUser(null);
        LoggedUser.getInstance().getGoals().clear();
        LoggedUser.getInstance().getIdeas().clear();

        this.everlive.workWith().authentication().logout();
    }

    public void changePassword(String username, String currentPassword, String newPassword){
        this.everlive.workWith().users().changePassword(username, currentPassword, newPassword).executeAsync();
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

    public void retrieveGoals(RequestResultCallbackAction<ArrayList<Goal>> callbackAction) {
        String loggedUserId = LoggedUser.getInstance().getLoggedUser().getId().toString();
        SortingDefinition sortDesc = new SortingDefinition("CreatedAt", SortDirection.Descending);
        ValueCondition ownerCondition = new ValueCondition("Owner", loggedUserId, ValueConditionOperator.EqualTo);

        this.everlive.workWith().
                data(Goal.class).
                get().
                where(ownerCondition).
                sort(sortDesc).
                executeAsync(callbackAction);
    }

    public void retrieveIdeas(RequestResultCallbackAction<ArrayList<Idea>> callbackAction) {
        String loggedUserId = LoggedUser.getInstance().getLoggedUser().getId().toString();
        List<String> goalIds = new ArrayList<String>();
        SortingDefinition sortDesc = new SortingDefinition("CreatedAt", SortDirection.Descending);
        List<Condition> conditions = new ArrayList<Condition>();

        for (Goal goal : LoggedUser.getInstance().getGoals()) {
            goalIds.add(goal.getIdeaId().toString());
        }

        conditions.add(new ArrayCondition("Id", ArrayConditionOperator.ValueIsNotIn, goalIds));
        conditions.add(new ValueCondition("Owner", loggedUserId, ValueConditionOperator.NotEqualTo));
        Condition compoundCondition = new CompoundCondition(CompoundConditionOperator.And, conditions);

        this.everlive.workWith().
                data(Idea.class).
                get().
                where(compoundCondition).
                sort(sortDesc).
                executeAsync(callbackAction);
    }

    public void deleteGoals(RequestResultCallbackAction callbackAction) {
        List<String> goalIdsToBeDeleted = new ArrayList<String>();
        List<Goal> goalsToBeDeleted = new ArrayList<Goal>();

        for (Goal goal : LoggedUser.getInstance().getGoals()) {
            if (goal.isSelected()) {
                goalIdsToBeDeleted.add(goal.getId().toString());
                goalsToBeDeleted.add(goal);
            }
        }

        for (Goal goal : goalsToBeDeleted) {
            LoggedUser.getInstance().getGoals().remove(goal);
        }

        Condition containsCondition = new ArrayCondition("Id", ArrayConditionOperator.ValueIsIn, goalIdsToBeDeleted);

        this.everlive.workWith().
                data(Goal.class).
                delete().
                where(containsCondition).
                executeAsync(callbackAction);
    }

    public void updateGoal(Goal goal, RequestResultCallbackAction callbackAction) {
        Goal updatedGoal = new Goal(goal.getTitle(), goal.getDescription(), goal.getIdeaId());
        updatedGoal.setDone(goal.isDone());

        this.everlive.
                workWith().
                data(Goal.class).
                updateById(goal.getId(), updatedGoal).
                executeAsync(callbackAction);
    }
}
