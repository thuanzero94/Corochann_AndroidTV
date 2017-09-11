package com.example.corochann_androidtv.ui.presenter;

import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter;

import com.example.corochann_androidtv.model.Movie;

public class DescriptionPresenter extends AbstractDetailsDescriptionPresenter {

    private static final String TAG = DescriptionPresenter.class.getSimpleName();

    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object item) {
        viewHolder.getTitle().setText(((Movie) item).getTitle());
        viewHolder.getSubtitle().setText(((Movie) item).getStudio());
    }
}