package com.telerikacademy.jasmine.thebucketlistapp.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import com.telerik.everlive.sdk.core.model.system.File;
import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerikacademy.jasmine.thebucketlistapp.models.LoggedUser;
import com.telerikacademy.jasmine.thebucketlistapp.persisters.RemoteDbManager;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.UUID;

public class ImageLoader extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewWeakReference;
    private Context context;

    public ImageLoader(Context context, ImageView imageView) {
        this.imageViewWeakReference = new WeakReference<ImageView>(imageView);
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String pictureId = strings[0];
        try {
            if (pictureId != null) {
                Bitmap bitmap = LoggedUser.getInstance().getPictureById(UUID.fromString(pictureId));

                if (bitmap != null) {
                    return bitmap;
                }

                RequestResult userImageRequest = RemoteDbManager.getInstance().downloadPicture(pictureId);

                if (userImageRequest.getSuccess()) {
                    String url = ((File) userImageRequest.getValue()).getUri();
                    URL imageUrl = new URL(url);
                    InputStream inputStream = imageUrl.openStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);

                    LoggedUser.getInstance().addPicture(UUID.fromString(pictureId), bitmap);

                    return bitmap;
                }
            }

        } catch (Exception ex) {
            Log.e("Exception when downloading image: ", ex.getMessage().toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewWeakReference != null && bitmap != null) {
            final ImageView imageView = imageViewWeakReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}