package com.telerikacademy.jasmine.thebucketlistapp.tasks;

import android.app.Activity;
import android.app.ProgressDialog;

import com.telerik.everlive.sdk.core.model.system.AccessToken;
import com.telerik.everlive.sdk.core.model.system.User;
import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;
import com.telerikacademy.jasmine.thebucketlistapp.activities.LoginActivity;
import com.telerikacademy.jasmine.thebucketlistapp.models.LoggedUser;
import com.telerikacademy.jasmine.thebucketlistapp.persisters.RemoteDbManager;

public class LoginRequestResultCallbackAction extends RequestResultCallbackAction<AccessToken> {
    private Activity activity;
    private ProgressDialog progressDialog;

    public LoginRequestResultCallbackAction(Activity activity, ProgressDialog progressDialog) {
        this.activity = activity;
        this.progressDialog = progressDialog;
    }

    @Override
    public void invoke(RequestResult<AccessToken> accessTokenRequestResult) {


        if (accessTokenRequestResult.getSuccess()) {

            RemoteDbManager.getInstance().getMe(new RequestResultCallbackAction() {
                @Override
                public void invoke(RequestResult requestResult) {
                    User me = (User) requestResult.getValue();
                    LoggedUser.getInstance().setLoggedUser(me);

                    progressDialog.dismiss();

                    LoginActivity.startMainActivity(activity);
                }
            });
        } else {
            progressDialog.dismiss();

            final String errorMessage = accessTokenRequestResult.getError().getMessage();

            this.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LoginActivity.showAlert(activity, errorMessage);
                }
            });
        }
    }
}

