package com.telerikacademy.jasmine.thebucketlistapp.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.telerikacademy.jasmine.thebucketlistapp.R;
import com.telerikacademy.jasmine.thebucketlistapp.models.Brag;

import java.text.DateFormat;
import java.util.List;

public class BragAdapter extends ArrayAdapter<Brag> {
    private Context context;
    private int layoutId;
    private List<Brag> brags;

    public BragAdapter(Context context, int resourceId, List<Brag> brags) {
        super(context, resourceId, brags);
        this.context = context;
        this.layoutId = resourceId;
        this.brags = brags;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        BragHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) this.context).getLayoutInflater();
            row = inflater.inflate(this.layoutId, parent, false);

            holder = new BragHolder();
            holder.title = (TextView) row.findViewById(R.id.tvRowBragTitle);
            holder.author = (TextView) row.findViewById(R.id.tvRowBragAuthor);
            holder.createdAt = (TextView) row.findViewById(R.id.tvCreatedAt);

            row.setTag(holder);
        } else {
            holder = (BragHolder) row.getTag();
        }

        Brag brag = this.brags.get(position);

        if (brag != null) {
            holder.title.setText(brag.getTitle());
            holder.author.setText("By: " + brag.getAuthorName());
            holder.createdAt.setText(DateFormat.
                    getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).
                    format(brag.getCreatedAt()));
        }

        return row;
    }

    static class BragHolder {
        private TextView title;
        private TextView author;
        private TextView createdAt;
    }
}
