package com.example.corochann_androidtv.recommendation;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.corochann_androidtv.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by alan on 05/09/2017.
 */

public class RecommendationBuilder {

    private static final String TAG = RecommendationBuilder.class.getSimpleName();
    private static final String BACKGROUND_URI_PREFIX = "content://com.corochann.androidtvapptutorial/";

    private Context mContext;

    private int mId;
    private int mPriority;
    private int mFastLanceColor;
    private int mSmallIcon;
    private String mTitle;
    private String mDescription;
    private Bitmap mCardImageBitmap;
    private String mBackgroundUri;
    private Bitmap mBackgroundBitmap;
    private String mGroupKey;
    private String mSort;
    private PendingIntent mIntent;

    public RecommendationBuilder(Context context){
        mContext = context;
        // default fast lane color
        setFastLaneColor(mContext.getResources().getColor(R.color.fastlane_background));
    }

    public RecommendationBuilder setFastLaneColor(int color){
        mFastLanceColor = color;
        return this;
    }

        /* context must not be null. It should be specified in constructor */
/*
    public RecommendationBuilder setContext(Context context) {
        mContext = context;
        return this;
    }
*/

    public RecommendationBuilder setId(int id) {
        mId = id;
        return this;
    }

    public RecommendationBuilder setPriority(int priority) {
        mPriority = priority;
        return this;
    }

    public RecommendationBuilder setTitle(String title) {
        mTitle = title;
        return this;
    }

    public RecommendationBuilder setDescription(String description) {
        mDescription = description;
        return this;
    }

    public RecommendationBuilder setBitmap(Bitmap bitmap) {
        mCardImageBitmap = bitmap;
        return this;
    }

    public RecommendationBuilder setBackground(String uri) {
        mBackgroundUri = uri;
        return this;
    }

    public RecommendationBuilder setBackground(Bitmap bitmap) {
        mBackgroundBitmap = bitmap;
        return this;
    }

    public RecommendationBuilder setIntent(PendingIntent intent) {
        mIntent = intent;
        return this;
    }

    public RecommendationBuilder setSmallIcon(int resourceId) {
        mSmallIcon = resourceId;
        return this;
    }

    public Notification build(){
        Bundle extras = new Bundle();
        File bitmapFile = getNotificationBackground(mContext, mId);

        if(mBackgroundBitmap != null){
            Log.d(TAG, "making URI for mBackgroundBitmap");
            extras.putString(Notification.EXTRA_BACKGROUND_IMAGE_URI, Uri.parse(BACKGROUND_URI_PREFIX + Integer.toString(mId)).toString());
        } else {
            Log.w(TAG, "mBackground is null");
        }

        // the following simulates group assignment into "Top", "Middle", "Bottom"
        // by checking mId and similarly sort order
        mGroupKey = (mId < 3) ? "Group1" : (mId < 5) ? "Group2" : "Group3";
        mSort = (mId < 3) ? "1.0" : (mId < 5) ? "0.7" : "0.3";

        // save bitmap into files for content provider to serve later
        try {
            bitmapFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(bitmapFile);
            mBackgroundBitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut); // <- background bitmap must be created by mBackgroundUri, and not  mCardImageBitmap
            fOut.flush();
            fOut.close();
        } catch (IOException ioe) {
            Log.d(TAG, "Exception caught writing bitmap to file!", ioe);
        }


        Notification notification = new NotificationCompat.BigPictureStyle(
                new NotificationCompat.Builder(mContext)
                .setAutoCancel(true)
                        .setContentTitle(mTitle)
                        .setContentText(mDescription)
                        .setPriority(mPriority)
                        .setLocalOnly(true)
                        .setOngoing(true)
                        .setGroup(mGroupKey)
                        .setSortKey(mSort)
                        .setColor(mContext.getResources().getColor(R.color.fastlane_background))
                        .setCategory(Notification.CATEGORY_RECOMMENDATION)
                        .setLargeIcon(mCardImageBitmap)
                        .setSmallIcon(mSmallIcon)
                        .setContentIntent(mIntent)
                        .setExtras(extras))
                .build();

        Log.d(TAG , "Building notification - " + this.toString());

        return notification;
    }

    public static class RecommendationBackgroundContentProvider extends ContentProvider{
        @Override
        public boolean onCreate() {
            return true;
        }

        @Override
        public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
            return null;
        }

        @Override
        public String getType(@NonNull Uri uri) {
            return null;
        }

        @Override
        public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
            return null;
        }

        @Override
        public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
            return 0;
        }

        @Override
        public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
            return 0;
        }

        @Override
        public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException{
            Log.i(TAG, "openFile");
            int backgroundId = Integer.parseInt(uri.getLastPathSegment());
            File bitmapFile = getNotificationBackground(getContext(), backgroundId);
            return ParcelFileDescriptor.open(bitmapFile, ParcelFileDescriptor.MODE_READ_ONLY);
        }
    }

    private static File getNotificationBackground(Context context, int notificationId){
        Log.i(TAG, "getNotificationBackground: " + context.getCacheDir() + "tmp" + Integer.toString(notificationId) + ".png");
        return new File(context.getCacheDir(), "tmp" + Integer.toString(notificationId) + ".png");
    }

}
