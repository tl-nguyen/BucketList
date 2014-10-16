package com.telerikacademy.jasmine.thebucketlistapp.activities.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.telerik.everlive.sdk.core.model.system.User;
import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;
import com.telerikacademy.jasmine.thebucketlistapp.R;
import com.telerikacademy.jasmine.thebucketlistapp.models.Brag;
import com.telerikacademy.jasmine.thebucketlistapp.models.LoggedUser;
import com.telerikacademy.jasmine.thebucketlistapp.persisters.RemoteDbManager;
import com.telerikacademy.jasmine.thebucketlistapp.utils.BragAdapter;

import java.util.ArrayList;

public class BraggersFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView mBragListView;
    private View mRootView;

    private BragAdapter bragAdapter;

    public BragAdapter getBragAdapter() {
        return bragAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mRootView= inflater.inflate(R.layout.fragment_braggers, container, false);

        initializeComponents();

        return this.mRootView;
    }

    private void initializeComponents() {
        this.mBragListView = (ListView) mRootView.findViewById(R.id.lvBrags);

        this.bragAdapter = new BragAdapter(this.getActivity(),
                R.layout.fragment_list_row_brag,
                LoggedUser.getInstance().getBrags());

        this.mBragListView.setAdapter(this.bragAdapter);

        this.loadBrags(mBragListView, this.getActivity(), this);

        mBragListView.setOnItemClickListener(this);
    }

    private void loadBrags(final ListView listView, final Activity activity, final BraggersFragment bragsFragment) {
        RemoteDbManager.getInstance().retrieveBrags(new RequestResultCallbackAction<ArrayList<Brag>>() {

            @Override
            public void invoke(final RequestResult<ArrayList<Brag>> requestResult) {

                if (requestResult.getSuccess()) {
                    LoggedUser.getInstance().getBrags().clear();

                    LoggedUser.getInstance().setCurrentBragsCount(requestResult.getValue().size());

                    for (final Brag brag : requestResult.getValue()) {

                        RemoteDbManager.getInstance().getBragOwner(brag, new RequestResultCallbackAction<User>() {
                            @Override
                            public void invoke(final RequestResult requestResult) {
                                if (requestResult.getSuccess()) {
                                    brag.setAuthorName(((User) requestResult.getValue()).getDisplayName());

                                    listView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            bragsFragment.getBragAdapter().notifyDataSetChanged();
                                        }
                                    });
                                } else {
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(activity, requestResult.getError().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });

                        LoggedUser.getInstance().getBrags().add(brag);
                    }
                } else {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, requestResult.getError().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.loadBrags(mBragListView, this.getActivity(), this);
    }

    private void showAlert(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
