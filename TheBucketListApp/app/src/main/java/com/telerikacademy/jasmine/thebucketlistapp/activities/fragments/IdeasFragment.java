package com.telerikacademy.jasmine.thebucketlistapp.activities.fragments;

import android.app.Activity;
import android.app.Fragment;
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
import com.telerikacademy.jasmine.thebucketlistapp.models.Idea;
import com.telerikacademy.jasmine.thebucketlistapp.models.LoggedUser;
import com.telerikacademy.jasmine.thebucketlistapp.persisters.RemoteDbManager;
import com.telerikacademy.jasmine.thebucketlistapp.utils.IdeaAdapter;

import java.util.ArrayList;

public class IdeasFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ListView mIdeaListView;
    private View mRootView;

    private IdeaAdapter ideaAdapter;

    public IdeaAdapter getIdeaAdapter() {
        return ideaAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mRootView = inflater.inflate(R.layout.fragment_ideas, container, false);

        initializeComponents();

        return this.mRootView;
    }

    private void initializeComponents () {
        this.mIdeaListView = (ListView) mRootView.findViewById(R.id.lvGoals);

        this.ideaAdapter = new IdeaAdapter(this.getActivity(), R.layout.fragment_list_row_idea, LoggedUser.getInstance().getIdeas());

        this.mIdeaListView.setAdapter(this.ideaAdapter);

        this.loadIdeas(mIdeaListView, this.getActivity(), this);

        mIdeaListView.setOnItemClickListener(this);
    }

    private void loadIdeas(final ListView listView, final Activity activity, final IdeasFragment ideasFragment) {
        RemoteDbManager.getInstance().retrieveIdeas(new RequestResultCallbackAction<ArrayList<Idea>>() {

            @Override
            public void invoke(RequestResult<ArrayList<Idea>> requestResult) {

                if (requestResult.getSuccess()) {
                    LoggedUser.getInstance().getIdeas().clear();

                    for (final Idea idea : requestResult.getValue()) {

                        RemoteDbManager.getInstance().getIdeaOwner(idea, new RequestResultCallbackAction<User>() {
                            @Override
                            public void invoke(RequestResult requestResult) {
                                if (requestResult.getSuccess()) {
                                    idea.setAuthorName(((User) requestResult.getValue()).getDisplayName());

                                    listView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            ideasFragment.getIdeaAdapter().notifyDataSetChanged();
                                        }
                                    });

                                } else {
                                    Toast.makeText(activity, requestResult.getError().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        LoggedUser.getInstance().getIdeas().add(idea);
                    }
                } else {
                    Toast.makeText(activity, requestResult.getError().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
