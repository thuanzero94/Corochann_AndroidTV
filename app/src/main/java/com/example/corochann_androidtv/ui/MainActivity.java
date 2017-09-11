package com.example.corochann_androidtv.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.corochann_androidtv.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onSearchRequested(){
        startActivity(new Intent(this, SearchActivity.class));
        return true;
    }
}
