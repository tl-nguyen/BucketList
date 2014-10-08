package com.telerikacademy.jasmine.thebucketlistapp.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import com.telerik.everlive.sdk.core.model.system.AccessToken;
import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;
import com.telerikacademy.jasmine.thebucketlistapp.activities.LoginActivity;

public class LoginRequestResultCallbackAction extends RequestResultCallbackAction<AccessToken> {
    private String loginMethod;
    private Activity activity;
    private ProgressDialog progressDialog;

    public LoginRequestResultCallbackAction(Activity activity, String loginMethod, ProgressDialog progressDialog) {
        this.activity = activity;
        this.loginMethod = loginMethod;
        this.progressDialog = progressDialog;
    }

    @Override
    public void invoke(RequestResult<AccessToken> accessTokenRequestResult) {
        progressDialog.dismiss();

        if (accessTokenRequestResult.getSuccess()) {
            Log.d("BucketList", "login success");
        } else {
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

