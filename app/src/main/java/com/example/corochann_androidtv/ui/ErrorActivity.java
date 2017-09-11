package com.example.corochann_androidtv.ui;

import android.app.Activity;
import android.os.Bundle;

import com.example.corochann_androidtv.R;

/**
 * Created by alan on 30/08/2017.
 */

public class ErrorActivity extends Activity {
    private static final String TAG = ErrorActivity.class.getSimpleName();

    private ErrorFragment mErrorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testError();
    }

    private void testError(){
        mErrorFragment = new ErrorFragment();
        getFragmentManager().beginTransaction().add(R.id.main_browse_fragment, mErrorFragment).commit();
    }
}
