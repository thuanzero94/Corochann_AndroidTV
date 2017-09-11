package com.example.corochann_androidtv.ui;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;
import android.util.Log;

import com.example.corochann_androidtv.R;

import java.util.List;

/**
 * Created by alan on 04/09/2017.
 */

public class GuidedStepActivity extends Activity{
    private static final String TAG = GuidedStepActivity.class.getSimpleName();

    /* Action ID definition */
    private static final int ACTION_CONTINUE = 0;
    private static final int ACTION_BACK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        if( savedInstanceState == null){
            GuidedStepFragment.add(getFragmentManager(), new FirstStepFragment());
        }
    }

    private static void addAction(List actions, long id, String title, String desc){
        actions.add(new GuidedAction.Builder()
                .id(id)
                .title(title)
                .description(desc)
                .build());
    }

    public static class FirstStepFragment extends GuidedStepFragment{
        @NonNull
        @Override
        public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState){
            String title = "Title";
            String breadcrumb = "breadcrumd";
            String description = "Description";
            Drawable icon = getActivity().getDrawable(R.drawable.ic_main_icon);

            return new GuidanceStylist.Guidance(title, description, breadcrumb, icon);
        }

        @Override
        public void onCreateActions(@NonNull List actions, Bundle savedInstanceState){
            addAction(actions, ACTION_CONTINUE, "Continue", "Go to SecondStepFragment");
            addAction(actions, ACTION_BACK, "Cancel", "Go back");
        }

        @Override
        public void onGuidedActionClicked(GuidedAction action){
            switch ((int) action.getId()){
                case ACTION_CONTINUE:
                    // FragmentManager fm = getFragmentManager();
                    // GuidedStepFragment.add(fm, new SecondStepFragment());
                    break;
                case ACTION_BACK:
                    getActivity().finish();
                    break;
                default:
                    Log.w(TAG, "Action is not defined");
                    break;
            }
        }
    }
}
