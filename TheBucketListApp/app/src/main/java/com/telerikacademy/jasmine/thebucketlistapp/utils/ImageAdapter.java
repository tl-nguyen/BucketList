package com.telerikacademy.jasmine.thebucketlistapp.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.telerikacademy.jasmine.thebucketlistapp.R;
import com.telerikacademy.jasmine.thebucketlistapp.models.Idea;

import java.text.DateFormat;
import java.util.List;

public class ImageAdapter extends ArrayAdapter<Bitmap> {

    private Context context;
    private int layoutId;
    private List<Bitmap> images;

    public ImageAdapter(Context context, int resourceId, List<Bitmap> images) {
        super(context, resourceId, images);
        this.context = context;
        this.layoutId = resourceId;
        this.images = images;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cell = convertView;
        ImageHolder holder;

        if (cell == null) {
            LayoutInflater inflater = ((Activity) this.context).getLayoutInflater();
            cell = inflater.inflate(this.layoutId, parent, false);

            holder = new ImageHolder();
            holder.image = (ImageView) cell.findViewById(R.id.ivGoalImage);

            cell.setTag(holder);
        } else {
            holder = (ImageHolder) cell.getTag();
        }

        Bitmap image = this.images.get(position);

        if (image != null) {
            holder.image.setImageBitmap(image);
        }

        return cell;
    }

    static class ImageHolder {
        private ImageView image;
    }
}
