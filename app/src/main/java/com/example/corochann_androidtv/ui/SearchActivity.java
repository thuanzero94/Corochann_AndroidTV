package com.example.corochann_androidtv.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.corochann_androidtv.R;

public class SearchActivity extends Activity {
    private static final String TAG = SearchActivity.class.getSimpleName();
    private SearchFragment mSearchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSearchFragment = (SearchFragment) getFragmentManager().findFragmentById(R.id.search_fragment);
    }

    @Override
    public boolean onSearchRequested(){
        startActivity(new Intent(this, SearchActivity.class));
        return true;
    }
}
