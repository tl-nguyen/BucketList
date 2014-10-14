package com.telerikacademy.jasmine.thebucketlistapp.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.telerik.everlive.sdk.core.model.system.File;
import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;
import com.telerikacademy.jasmine.thebucketlistapp.R;
import com.telerikacademy.jasmine.thebucketlistapp.models.Goal;
import com.telerikacademy.jasmine.thebucketlistapp.models.LoggedUser;
import com.telerikacademy.jasmine.thebucketlistapp.persisters.RemoteDbManager;
import com.telerikacademy.jasmine.thebucketlistapp.tasks.ImageLoader;
import com.telerikacademy.jasmine.thebucketlistapp.utils.ImageAdapter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GoalDetailActivity extends Activity {

    private Goal mGoal;
    private Menu mMenu;
    private TextView mGoalTitle;
    private TextView mGoalDescription;
    private GridView mGoalImagesView;
    private ImageAdapter imageAdapter;
    private Context context = this;
    private List<Bitmap> goalImages;

    final int GET_CAM_IMG=2;
    final int GET_GAL_IMG=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goal_detail);

        int position = getIntent().getIntExtra(getString(R.string.GOAL_POSITION), -1);

        if (position == -1) {
            startGoalScreen();
        } else {
            mGoal = LoggedUser.getInstance().getGoals().get(position);
        }

        ActionBar actionbar = getActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
        }

        this.mGoalTitle = (TextView) findViewById(R.id.goalDetailTitle);
        this.mGoalDescription = (TextView) findViewById(R.id.goalDetailDescription);

        this.mGoalTitle.setText(mGoal.getTitle());
        this.mGoalDescription.setText(mGoal.getDescription());

        this.mGoalImagesView = (GridView) this.findViewById(R.id.gvGoalPictures);

        this.imageAdapter = new ImageAdapter(this, R.layout.grid_cell_goal_image, goalImages);

        this.mGoalImagesView.setAdapter(this.imageAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.mMenu = menu;
        getMenuInflater().inflate(R.menu.goal_detail, menu);

        if (mGoal.isDone()) {
            menu.findItem(R.id.set_as_completed).setVisible(false);
            menu.findItem(R.id.take_a_shot).setVisible(false);
        } else {
            menu.findItem(R.id.brag).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            startGoalScreen();
        } else if (id == R.id.action_logout) {
            userLogout();
        } else if (id == R.id.take_a_shot) {
            startCamera();
        } else if (id == R.id.set_as_completed) {
            setAsCompleted();
        } else if (id == R.id.brag) {
            //TODO: brag about this archivement
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == -1) {
            Bitmap bmp_image = null;

            switch (requestCode) {
                case GET_CAM_IMG:
                    bmp_image = getImageFromCamera(intent);
                    break;
                case GET_GAL_IMG:
                    bmp_image = getImageFromGallery(intent);
                    break;
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            if (bmp_image != null) {
                bmp_image.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                byte[] image = byteArrayOutputStream.toByteArray();
                InputStream stream = new ByteArrayInputStream(image);

                uploadImage(stream);
            }
        }
    }

    private void uploadImage(InputStream stream) {
        RemoteDbManager.getInstance().uploadImage(stream, new RequestResultCallbackAction() {
            @Override
            public void invoke(RequestResult requestResult) {

                if (requestResult.getSuccess()) {
                    ArrayList<File> files = (ArrayList<File>)requestResult.getValue();

                    File file = files.get(0);

                    UUID id = UUID.fromString(file.getId().toString());

                    mGoal.getPictures().add(id);

                    RemoteDbManager.getInstance().updateGoalPictures(mGoal, new RequestResultCallbackAction() {
                        @Override
                        public void invoke(RequestResult requestResult) {
                            if (requestResult.getSuccess()) {
                                //TODO: push it to GridView
                            }
                        }
                    });
                }
            }
        });
    }

    private void loadGoalImages() {
//        ImageLoader imageLoadTask = new ImageLoader(this, )
    }

    private Bitmap getImageFromCamera(Intent intent) {
        Uri selectedImage = intent.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(
                selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();

        return BitmapFactory.decodeFile(filePath);
    }

    private Bitmap getImageFromGallery(Intent intent) {
        Uri selectedImageUri = intent.getData();
        Bitmap bmp_image = null;

        if (Build.VERSION.SDK_INT < 19) {
            String selectedImagePath = getPath(selectedImageUri);

            bmp_image = BitmapFactory.decodeFile(selectedImagePath);
        }
        else {
            ParcelFileDescriptor parcelFileDescriptor;
            try {
                parcelFileDescriptor = getContentResolver().openFileDescriptor(selectedImageUri, "r");
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                bmp_image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                parcelFileDescriptor.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bmp_image;
    }

    private String getPath(Uri uri) {
        if( uri == null ) {
            return null;
        }
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    private void startGoalScreen() {
        Intent goalsScreen = new Intent(GoalDetailActivity.this, MainActivity.class);
        goalsScreen.putExtra(getString(R.string.FRAGMENT), getResources().getInteger(R.integer.GOALS_FRAGMENT));
        this.startActivity(goalsScreen);
    }

    private void showAlert(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    private void startCamera() {
        CharSequence[] names = { getString(R.string.from_galerry_select), getString(R.string.from_camera_select) };
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.select_option))
                .setItems(names, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int pos) {
                        if (pos == 0) {
                            Intent i = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, GET_GAL_IMG);
                        } else {
                            Intent i = new Intent(
                                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(i, GET_CAM_IMG);
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        }).create().show();
    }

    private void setAsCompleted() {
        this.mGoal.setDone(true);
        RemoteDbManager.getInstance().updateGoalToCompleted(mGoal, new RequestResultCallbackAction() {
            @Override
            public void invoke(final RequestResult requestResult) {
                if (requestResult.getSuccess()) {
                    GoalDetailActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMenu.findItem(R.id.set_as_completed).setVisible(false);
                            mMenu.findItem(R.id.take_a_shot).setVisible(false);
                            mMenu.findItem(R.id.brag).setVisible(true);
                            showAlert(GoalDetailActivity.this, String.format(getString(R.string.goal_completed), mGoal.getTitle()));
                        }
                    });
                }
            }
        });
    }

    private void userLogout() {
        RemoteDbManager.getInstance().logout();

        Intent loginScreen = new Intent(this, LoginActivity.class);
        startActivity(loginScreen);
    }
}
