package com.telerikacademy.jasmine.thebucketlistapp.tasks;

import android.app.Activity;
import android.app.ProgressDialog;

import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;
import com.telerikacademy.jasmine.thebucketlistapp.R;
import com.telerikacademy.jasmine.thebucketlistapp.activities.LoginActivity;

public class RegisterRequestResultCallBackAction extends RequestResultCallbackAction {

    private Activity activity;
    private ProgressDialog progressDialog;

    public RegisterRequestResultCallBackAction(LoginActivity loginActivity, ProgressDialog registerProgressDialog) {
        this.activity = loginActivity;
        this.progressDialog = registerProgressDialog;
    }

    @Override
    public void invoke(RequestResult requestResult) {
        this.progressDialog.dismiss();

        if (requestResult.getSuccess()) {
            final int messageId = R.string.registerSuccess;

            this.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LoginActivity.showAlert(activity, messageId);
                }
            });
        } else {
            final String errorMessage = requestResult.getError().getMessage();

            this.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LoginActivity.showAlert(activity, errorMessage);
                }
            });
        }
    }
}
