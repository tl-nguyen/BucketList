package com.telerikacademy.jasmine.thebucketlistapp.activities.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.telerik.everlive.sdk.core.model.system.User;
import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;
import com.telerikacademy.jasmine.thebucketlistapp.R;
import com.telerikacademy.jasmine.thebucketlistapp.activities.MainActivity;
import com.telerikacademy.jasmine.thebucketlistapp.models.Goal;
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

        this.ideaAdapter = new IdeaAdapter(this.getActivity(),
                R.layout.fragment_list_row_idea,
                LoggedUser.getInstance().getIdeas());

        this.mIdeaListView.setAdapter(this.ideaAdapter);

        this.loadIdeas(mIdeaListView, this.getActivity(), this);

        mIdeaListView.setOnItemClickListener(this);
    }

    private void loadIdeas(final ListView listView, final Activity activity, final IdeasFragment ideasFragment) {
        RemoteDbManager.getInstance().retrieveIdeas(new RequestResultCallbackAction<ArrayList<Idea>>() {

            @Override
            public void invoke(final RequestResult<ArrayList<Idea>> requestResult) {

                if (requestResult.getSuccess()) {
                    LoggedUser.getInstance().getIdeas().clear();

                    for (final Idea idea : requestResult.getValue()) {

                        RemoteDbManager.getInstance().getIdeaOwner(idea, new RequestResultCallbackAction<User>() {
                            @Override
                            public void invoke(final RequestResult requestResult) {
                                if (requestResult.getSuccess()) {
                                    idea.setAuthorName(((User) requestResult.getValue()).getDisplayName());

                                    listView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            ideasFragment.getIdeaAdapter().notifyDataSetChanged();
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

                        LoggedUser.getInstance().getIdeas().add(idea);
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
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        LayoutInflater inflater = LayoutInflater.from(this.getActivity());
        View alertView = inflater.inflate(R.layout.idea_detail, null);
        final Idea selectedIdea = LoggedUser.getInstance().getIdeas().get(position);
        final Activity activity = this.getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setView(alertView);

        // Add the buttons
        builder.setPositiveButton(R.string.set_goal, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Goal newGoal = new Goal(selectedIdea.getTitle(),
                        selectedIdea.getDescription(),
                        selectedIdea.getId());

                RemoteDbManager.getInstance().createGoal(newGoal, new RequestResultCallbackAction() {

                    @Override
                    public void invoke(final RequestResult requestResult) {
                        if (requestResult.getSuccess()) {
                            LoggedUser.getInstance().getIdeas().remove(position);

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showAlert(activity, String.format(getString(R.string.success_set_idea_as_goal), selectedIdea.getTitle()));

                                    mIdeaListView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            IdeasFragment.this.getIdeaAdapter().notifyDataSetChanged();
                                        }
                                    });
                                }
                            });
                        } else {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showAlert(activity, requestResult.getError().toString());
                                }
                            });
                        }
                    }
                });
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        // Create the AlertDialog
        AlertDialog dialog = builder.create();

        dialog.setTitle(getString(R.string.idea_detail));

        TextView ideaTitle = (TextView) alertView.findViewById(R.id.ideaDetailTitle);
        TextView ideaDescription = (TextView) alertView.findViewById(R.id.ideaDetailDescription);
        ideaTitle.setText(selectedIdea.getTitle());
        ideaDescription.setText(selectedIdea.getDescription());

        dialog.show();
    }

    private void showAlert(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
