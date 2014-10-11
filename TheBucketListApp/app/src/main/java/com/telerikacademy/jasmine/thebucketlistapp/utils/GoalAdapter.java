package com.telerikacademy.jasmine.thebucketlistapp.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.telerikacademy.jasmine.thebucketlistapp.R;
import com.telerikacademy.jasmine.thebucketlistapp.models.Goal;

import java.util.List;

public class GoalAdapter extends ArrayAdapter<Goal> {
    private Context context;
    private int layoutId;
    private List<Goal> goals;

    public GoalAdapter(Context context, int resourceId, List<Goal> goals) {
        super(context, resourceId, goals);
        this.context = context;
        this.layoutId = resourceId;
        this.goals = goals;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        GoalHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) this.context).getLayoutInflater();
            row = inflater.inflate(this.layoutId, parent, false);

            holder = new GoalHolder();
            holder.title = (TextView) row.findViewById(R.id.tvRowGoalTitle);
            holder.description = (TextView) row.findViewById(R.id.tvRowGoalDescription);
            holder.isDone = (CheckBox) row.findViewById(R.id.cbIsDone);

            row.setTag(holder);
        } else {
            holder = (GoalHolder) row.getTag();
        }

        Goal goal = this.goals.get(position);

        if (goal != null) {
            holder.title.setText(goal.getTitle());
            holder.description.setText(goal.getDescription());
            holder.isDone.setChecked(goal.isDone());
        }

        return row;
    }

    static class GoalHolder {
        private TextView title;
        private TextView description;
        private CheckBox isDone;
    }
}
