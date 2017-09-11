package com.example.corochann_androidtv.ui;

import android.app.Activity;
import android.os.Bundle;

import com.example.corochann_androidtv.R;

public class DetailsActivity extends Activity {

    public static final String VIDEO = "Video";
    public static final String MOVIE = "Movie";
    public static final String SHARED_ELEMENT_NAME = "hero";
    public static final String NOTIFICATION_ID = "ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
    }
}
