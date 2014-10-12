package com.telerikacademy.jasmine.thebucketlistapp.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.telerikacademy.jasmine.thebucketlistapp.R;
import com.telerikacademy.jasmine.thebucketlistapp.models.Idea;

import java.text.DateFormat;
import java.util.List;

public class IdeaAdapter extends ArrayAdapter<Idea> {
    private Context context;
    private int layoutId;
    private List<Idea> ideas;

    public IdeaAdapter(Context context, int resourceId, List<Idea> ideas) {
        super(context, resourceId, ideas);
        this.context = context;
        this.layoutId = resourceId;
        this.ideas = ideas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        IdeaHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) this.context).getLayoutInflater();
            row = inflater.inflate(this.layoutId, parent, false);

            holder = new IdeaHolder();
            holder.title = (TextView) row.findViewById(R.id.tvRowIdeaTitle);
            holder.author = (TextView) row.findViewById(R.id.tvRowIdeaAuthor);
            holder.createdAt = (TextView) row.findViewById(R.id.tvCreatedAt);

            row.setTag(holder);
        } else {
            holder = (IdeaHolder) row.getTag();
        }

        Idea idea = this.ideas.get(position);

        if (idea != null) {
            holder.title.setText(idea.getTitle());
            holder.author.setText("By: " + idea.getAuthorName());
            holder.createdAt.setText("Created At: " + DateFormat.
                    getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).
                    format(idea.getCreatedAt()));
        }

        return row;
    }

    static class IdeaHolder {
        private TextView title;
        private TextView author;
        private TextView createdAt;
    }
}
