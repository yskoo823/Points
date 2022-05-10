package edu.sjsu.android.Points;

import static edu.sjsu.android.Points.LocationsProvider.CONTENT_URI;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;

public class MyTask extends AsyncTask<ContentValues, Void, Void> {

    private Activity activity;

    public MyTask(Activity activity) {
        this.activity = activity;
    }
    @Override
    protected Void doInBackground(ContentValues... contentValues) {
        if (contentValues.length > 0) {
            activity.getContentResolver().insert(CONTENT_URI, contentValues[0]);
        }
        else {
            activity.getContentResolver().delete(CONTENT_URI, null, null);
        }
        return null;
    }
}
