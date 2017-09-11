package com.example.corochann_androidtv.ui;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.SpeechRecognitionCallback;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.corochann_androidtv.Utils;
import com.example.corochann_androidtv.data.MovieProvider;
import com.example.corochann_androidtv.model.Movie;
import com.example.corochann_androidtv.ui.presenter.CardPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by alan on 08/09/2017.
 */

public class SearchFragment extends android.support.v17.leanback.app.SearchFragment
        implements android.support.v17.leanback.app.SearchFragment.SearchResultProvider{
    private static final String TAG = SearchFragment.class.getSimpleName();

    private static final int REQUEST_SPEECH = 0x00000010;
    private ArrayObjectAdapter mRowsAdapter;

    private ArrayList<Movie> mItems = MovieProvider.getMovieItems();

    private String mQuery;

    private static final long SEARCH_DELAY_MS = 1000L;
    private final Handler mHandler = new Handler();
    private final Runnable mDelayedLoad = new Runnable() {
        @Override
        public void run() {
            loadRows();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        setSearchResultProvider(this);

        setOnItemViewClickedListener(new ItemViewClickedListener());

        if( !Utils.hasPermission(getActivity(), Manifest.permission.RECORD_AUDIO)){
            // SpeechRecognitionCallback is not required and if not provided recognition will be handled
            // using internal speech recognizer, in which case you must have RECORD_AUDIO permission
            setSpeechRecognitionCallback(new SpeechRecognitionCallback() {
                @Override
                public void recognizeSpeech() {
                    Log.v(TAG, "recognizeSpeech");
                    try{
                        startActivityForResult(getRecognizerIntent(), REQUEST_SPEECH);
                    } catch (ActivityNotFoundException e){
                        Log.e(TAG , "Cannot find activity for speech recognizer", e);
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "onActivityResult requestCode=" + requestCode +
                " resultCode=" + resultCode +
                " data=" + data);

        switch (requestCode) {
            case REQUEST_SPEECH:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        setSearchQuery(data, true);
                        break;
                }
        }
    }

    @Override
    public ObjectAdapter getResultsAdapter(){
        Log.d(TAG, "getResultsAdapter");
       // Log.d(TAG, mRowsAdapter.toString());

        /*// It should return search result here,
        // but static Movie Item list will be returned here now for practice.
        ArrayList<Movie> mItems = MovieProvider.getMovieItems();
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());
        listRowAdapter.addAll(0,mItems);
        HeaderItem header = new HeaderItem("Search results");
        mRowsAdapter.add(new ListRow(header, listRowAdapter));
*/
        return mRowsAdapter;
    }

    @Override
    public boolean onQueryTextChange(String newQuery) {
        Log.i(TAG, String.format("Search Query Text Change %s", newQuery));
        loadQueryWithDelay(newQuery, SEARCH_DELAY_MS);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.i(TAG, String.format("Search Query Text Change %s", query));
        // No need to delay(wait) loadQuery, since the query typing has completed.
        loadQueryWithDelay(query, 0);
        return true;
    }

    private void loadRows(){
        //offload processing from the UI thread
        new AsyncTask<String, Void, ListRow> (){
            private final String query = mQuery;

            @Override
            protected void onPreExecute(){
                mRowsAdapter.clear();
            }

            @Override
            protected ListRow doInBackground(String... params) {
                final List<Movie> result = new ArrayList<>();
                for(Movie movie : mItems){
                    // Main logic of search is here.
                    // Just check that "query" is contained in Title or Description or not. (NOTE: excluded studio information here)
                    if (movie.getTitle().toLowerCase(Locale.ENGLISH)
                            .contains(query.toLowerCase(Locale.ENGLISH))
                            || movie.getDescription().toLowerCase(Locale.ENGLISH)
                            .contains(query.toLowerCase(Locale.ENGLISH))) {
                        result.add(movie);
                    }
                }

                ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());
                listRowAdapter.addAll(0, result);
                HeaderItem header = new HeaderItem("Search Results");
                return new ListRow(header, listRowAdapter);
            }

            @Override
            protected void onPostExecute(ListRow listRow){
                mRowsAdapter.add(listRow);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void loadQueryWithDelay(String query, long delay){
        mHandler.removeCallbacks(mDelayedLoad);
        if( !TextUtils.isEmpty(query) && !query.equals("nil")){
            mQuery = query;
            mHandler.postDelayed(mDelayedLoad, delay);
        }
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener{
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {

            if(item instanceof Movie){
                Movie movie = (Movie) item;
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.MOVIE, movie);

                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        ((ImageCardView) itemViewHolder.view).getMainImageView(),
                        DetailsActivity.SHARED_ELEMENT_NAME).toBundle();
                getActivity().startActivity(intent, bundle);
            } else Toast.makeText(getActivity(), ((String) item), Toast.LENGTH_SHORT).show();
        }
    }
}
