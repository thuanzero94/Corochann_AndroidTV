package com.example.corochann_androidtv.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v17.leanback.app.DetailsFragment;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.DetailsOverviewRow;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnActionClickedListener;
import android.support.v17.leanback.widget.SparseArrayObjectAdapter;
import android.util.Log;

import com.example.corochann_androidtv.R;
import com.example.corochann_androidtv.Utils;
import com.example.corochann_androidtv.data.MovieProvider;
import com.example.corochann_androidtv.model.Movie;
import com.example.corochann_androidtv.ui.background.PicassoBackgroundManager;
import com.example.corochann_androidtv.ui.presenter.CardPresenter;
import com.example.corochann_androidtv.ui.presenter.CustomDetailsOverviewRowPresenter;
import com.example.corochann_androidtv.ui.presenter.DetailsDescriptionPresenter;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by alan on 30/08/2017.
 */

public class VideoDetailsFragment extends DetailsFragment {
    private static final String TAG = VideoDetailsFragment.class.getSimpleName();

    private static final int DETAIL_THUMB_WIDTH = 274;
    private static final int DETAIL_THUMB_HEIGHT = 274;

    private static final String MOVIE = "Movie";
    private static final int ACTION_PLAY_VIDEO = 0;

    private CustomDetailsOverviewRowPresenter mDorPresenter;
    //private CustomFullWidthDetailsOverviewRowPresenter mFwdorPresenter;
    private PicassoBackgroundManager mPicassoBackgroundManager;

    private Movie mSelectedMovie;
    private DetailsRowBuilderTask mDetailsRowBuilderTask;

    @Override
    public void onCreate(Bundle savedInstanceState){
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        //mFwdorPresenter = new CustomFullWidthDetailsOverviewRowPresenter(new DetailsDescriptionPresenter());
        mDorPresenter = new CustomDetailsOverviewRowPresenter(new DetailsDescriptionPresenter(), getActivity());

        mPicassoBackgroundManager = new PicassoBackgroundManager(getActivity());
        mSelectedMovie = (Movie)getActivity().getIntent().getSerializableExtra(MOVIE);

        mDetailsRowBuilderTask = (DetailsRowBuilderTask) new DetailsRowBuilderTask().execute(mSelectedMovie);
        mPicassoBackgroundManager.updateBackgroundWithDelay(mSelectedMovie.getCardImageUrl());
    }

    @Override
    public void onStop() {
        mDetailsRowBuilderTask.cancel(true);
        super.onStop();
    }

    private class DetailsRowBuilderTask extends AsyncTask<Movie, Integer, DetailsOverviewRow> {
        @Override
        protected DetailsOverviewRow doInBackground(Movie... params) {
            DetailsOverviewRow row = new DetailsOverviewRow(mSelectedMovie);
            try {
                Bitmap poster = Picasso.with(getActivity())
                        .load(mSelectedMovie.getCardImageUrl())
                        .resize(Utils.convertDpToPixel(getActivity().getApplicationContext(), DETAIL_THUMB_WIDTH),
                                Utils.convertDpToPixel(getActivity().getApplicationContext(), DETAIL_THUMB_HEIGHT))
                        .centerCrop()
                        .get();
                row.setImageBitmap(getActivity(), poster);
            } catch (IOException e) {
                Log.w(TAG, e.toString());
            }


            return row;
        }

        @Override
        protected void onPostExecute(DetailsOverviewRow row) {
                    /* 1st row: DetailsOverviewRow */
            SparseArrayObjectAdapter sparseArrayObjectAdapter = new SparseArrayObjectAdapter();
            sparseArrayObjectAdapter.set(0, new Action(ACTION_PLAY_VIDEO, "Play Video"));
            sparseArrayObjectAdapter.set(1, new Action(1, "Action 2", "label"));
            sparseArrayObjectAdapter.set(2, new Action(2, "Action 3", "label"));

            row.setActionsAdapter(sparseArrayObjectAdapter);

            mDorPresenter.setOnActionClickedListener(new OnActionClickedListener() {
                @Override
                public void onActionClicked(Action action) {
                    if(action.getId() == ACTION_PLAY_VIDEO) {
                        Intent intent = new Intent(getActivity(), PlaybackOverlayActivity.class);
                        intent.putExtra(getResources().getString(R.string.movie), mSelectedMovie);
                        intent.putExtra(getResources().getString(R.string.should_start), true);
                        startActivity(intent);
                    }
                }
            });

        /* 2nd row: ListRow */
            HeaderItem headerItem = new HeaderItem(0, "Related Videos");
            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());
            /*for(int i = 0; i < 3; i++){
                Movie movie = new Movie();
                if(i%3 == 0) {
                    movie.setCardImageUrl("http://heimkehrend.raindrop.jp/kl-hacker/wp-content/uploads/2014/08/DSC02580.jpg");
                } else if (i%3 == 1) {
                    movie.setCardImageUrl("http://heimkehrend.raindrop.jp/kl-hacker/wp-content/uploads/2014/08/DSC02630.jpg");
                } else {
                    movie.setCardImageUrl("http://heimkehrend.raindrop.jp/kl-hacker/wp-content/uploads/2014/08/DSC02529.jpg");
                }
                movie.setTitle("title" + i);
                movie.setStudio("studio" + i);
                listRowAdapter.add(movie);
            }*/
            ArrayList<Movie> mItems = MovieProvider.getMovieItems();
            for (Movie movie : mItems) {
                listRowAdapter.add(movie);
            }


            ClassPresenterSelector classPresenterSelector = new ClassPresenterSelector();
            //mFwdorPresenter.setInitialState(FullWidthDetailsOverviewRowPresenter.STATE_SMALL);
            //Log.e(TAG, "mFwdorPresenter.getInitialState: " +mFwdorPresenter.getInitialState());

            classPresenterSelector.addClassPresenter(DetailsOverviewRow.class, mDorPresenter);
            //classPresenterSelector.addClassPresenter(DetailsOverviewRow.class, mFwdorPresenter);
            classPresenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter());

            ArrayObjectAdapter adapter = new ArrayObjectAdapter(classPresenterSelector);
            /* 1st row */
            adapter.add(row);
            /* 2nd row */
            adapter.add(new ListRow(headerItem, listRowAdapter));
            /* 3rd row */
            //adapter.add(new ListRow(headerItem, listRowAdapter));
            setAdapter(adapter);

        }
    }
}
