package com.telerikacademy.jasmine.thebucketlistapp.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;
import com.telerikacademy.jasmine.thebucketlistapp.R;
import com.telerikacademy.jasmine.thebucketlistapp.models.Brag;
import com.telerikacademy.jasmine.thebucketlistapp.models.LoggedUser;
import com.telerikacademy.jasmine.thebucketlistapp.persisters.RemoteDbManager;

import java.util.ArrayList;

public class MainService extends Service {

    private int currentBragCount;

    public MainService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        retrieveLastBrags();

        return super.onStartCommand(intent, flags, startId);
    }

    public void retrieveLastBrags () {
        currentBragCount = LoggedUser.getInstance().getCurrentBragsCount();

        RemoteDbManager.getInstance().retrieveBrags(new RequestResultCallbackAction<ArrayList<Brag>>() {

            @Override
            public void invoke(RequestResult<ArrayList<Brag>> requestResult) {
                if (requestResult.getSuccess()) {
                    if (currentBragCount!= -1 && requestResult.getValue().size() > currentBragCount) {
                        currentBragCount = requestResult.getValue().size();
                        LoggedUser.getInstance().setCurrentBragsCount(currentBragCount);
                        createSimpleNotification(getString(R.string.new_brags_notification));
                    }
                }
            }
        });
    }

    public void createSimpleNotification(String bragAuthor) {
        Notification.Builder builder =
                new Notification.Builder(this)
                        .setSmallIcon(R.drawable.starwhite)
                        .setContentTitle("New Brags")
                        .setContentText(bragAuthor);

        Notification notification = builder.build();

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                .notify(R.id.simple_notification_id, notification);
    }
}
