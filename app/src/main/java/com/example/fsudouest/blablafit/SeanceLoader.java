package com.example.fsudouest.blablafit;

import android.content.Context;

import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class SeanceLoader extends AsyncTaskLoader<Seance> {


    /** Tag for log messages */
    private static final String LOG_TAG = SeanceLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link SeanceLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public SeanceLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public Seance loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a seance.
        //Seance seance = QueryUtils.createNewSeance(mUrl);
        return null;
    }
}
