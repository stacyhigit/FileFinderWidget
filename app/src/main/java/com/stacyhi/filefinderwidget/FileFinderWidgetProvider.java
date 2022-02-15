package com.stacyhi.filefinderwidget;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class FileFinderWidgetProvider extends AppWidgetProvider {
    private static final String TAG = "FileFinderWidgetProvider";
    public static final String ACTION_REFRESH = "com.stacyhi.filefinderwidget.actionRefresh";
    public static final String ACTION_FILE_SELECTED = "com.stacyhi.filefinderwidget.actionFileSelected";
    public static final String ACTION_SELECT_ALL = "com.stacyhi.filefinderwidget.actionSelectAll";
    public static final String ACTION_SELECT_DOCUMENTS = "com.stacyhi.filefinderwidget.actionSelectDocuments";
    public static final String ACTION_SELECT_IMAGES = "com.stacyhi.filefinderwidget.actionSelectImages";
    public static final String ACTION_SELECT_VIDEOS = "com.stacyhi.filefinderwidget.actionSelectVideos";
    public static final String ACTION_SELECT_MUSIC = "com.stacyhi.filefinderwidget.actionSelectMusic";
    public static final String ACTION_SELECT_DOWNLOADS = "com.stacyhi.filefinderwidget.actionSelectDownloads";
    public static final String EXTRA_ITEM_FILE_PATH = "com.stacyhi.incoming.extraItemFilePath";
    public static final String SELECT_ALL_FILES="All Files";
    public static final String SELECT_DOCUMENTS="Documents";
    public static final String SELECT_IMAGES="Images";
    public static final String SELECT_VIDEOS="Videos";
    public static final String SELECT_MUSIC="Music";
    public static final String SELECT_DOWNLOADS="Downloads";

    @SuppressLint("UnspecifiedImmutableFlag")
    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent fileServiceIntent = new Intent(context, FileWidgetService.class);
        fileServiceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        fileServiceIntent.setData(Uri.parse(fileServiceIntent.toUri(Intent.URI_INTENT_SCHEME)));

        Intent refreshIntent = new Intent(context, FileFinderWidgetProvider.class);
        refreshIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        refreshIntent.setAction(ACTION_REFRESH);
        PendingIntent refreshPendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            refreshPendingIntent = PendingIntent.getBroadcast(
                    context, appWidgetId, refreshIntent,  PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            refreshPendingIntent = PendingIntent.getBroadcast(
                    context, appWidgetId, refreshIntent,  PendingIntent.FLAG_UPDATE_CURRENT);
        }

        Intent selectAllIntent = new Intent(context, FileFinderWidgetProvider.class);
        selectAllIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        selectAllIntent.setAction(ACTION_SELECT_ALL);
        PendingIntent selectAllPendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            selectAllPendingIntent = PendingIntent.getBroadcast(
                    context, appWidgetId, selectAllIntent,  PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            selectAllPendingIntent = PendingIntent.getBroadcast(
                    context, appWidgetId, selectAllIntent,  PendingIntent.FLAG_UPDATE_CURRENT);
        }


        Intent selectDocumentsIntent = new Intent(context, FileFinderWidgetProvider.class);
        selectDocumentsIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        selectDocumentsIntent.setAction(ACTION_SELECT_DOCUMENTS);
        PendingIntent selectDocumentsPendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            selectDocumentsPendingIntent = PendingIntent.getBroadcast(
                    context, appWidgetId, selectDocumentsIntent,  PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            selectDocumentsPendingIntent = PendingIntent.getBroadcast(
                    context, appWidgetId, selectDocumentsIntent,  PendingIntent.FLAG_UPDATE_CURRENT);
        }


        Intent selectImagesIntent = new Intent(context, FileFinderWidgetProvider.class);
        selectImagesIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        selectImagesIntent.setAction(ACTION_SELECT_IMAGES);
        PendingIntent selectImagesPendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            selectImagesPendingIntent = PendingIntent.getBroadcast(
                    context, appWidgetId, selectImagesIntent,  PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            selectImagesPendingIntent = PendingIntent.getBroadcast(
                    context, appWidgetId, selectImagesIntent,  PendingIntent.FLAG_UPDATE_CURRENT);
        }

        Intent selectVideosIntent = new Intent(context, FileFinderWidgetProvider.class);
        selectVideosIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        selectVideosIntent.setAction(ACTION_SELECT_VIDEOS);
        PendingIntent selectVideosPendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            selectVideosPendingIntent = PendingIntent.getBroadcast(
                    context, appWidgetId, selectVideosIntent,  PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            selectVideosPendingIntent = PendingIntent.getBroadcast(
                    context, appWidgetId, selectVideosIntent,  PendingIntent.FLAG_UPDATE_CURRENT);
        }

        Intent selectMusicIntent = new Intent(context, FileFinderWidgetProvider.class);
        selectMusicIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        selectMusicIntent.setAction(ACTION_SELECT_MUSIC);
        PendingIntent selectMusicPendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            selectMusicPendingIntent = PendingIntent.getBroadcast(
                    context, appWidgetId, selectMusicIntent,  PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            selectMusicPendingIntent = PendingIntent.getBroadcast(
                    context, appWidgetId, selectMusicIntent,  PendingIntent.FLAG_UPDATE_CURRENT);
        }

        Intent selectDownloadsIntent = new Intent(context, FileFinderWidgetProvider.class);
        selectDownloadsIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        selectDownloadsIntent.setAction(ACTION_SELECT_DOWNLOADS);
        PendingIntent selectDownloadsPendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            selectDownloadsPendingIntent = PendingIntent.getBroadcast(
                    context, appWidgetId, selectDownloadsIntent,  PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            selectDownloadsPendingIntent = PendingIntent.getBroadcast(
                    context, appWidgetId, selectDownloadsIntent,  PendingIntent.FLAG_UPDATE_CURRENT);
        }

        Intent fileClickIntent = new Intent(context, FileFinderWidgetProvider.class);
        fileClickIntent.setAction(ACTION_FILE_SELECTED);
        PendingIntent fileClickPendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            fileClickPendingIntent = PendingIntent.getBroadcast(context, appWidgetId, fileClickIntent, PendingIntent.FLAG_MUTABLE);
        } else {
            fileClickPendingIntent = PendingIntent.getBroadcast(context, appWidgetId, fileClickIntent, 0);
        }

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.file_finder_widget_provider);
        views.setRemoteAdapter(R.id.lv_files, fileServiceIntent);
        views.setEmptyView(R.id.lv_files, R.id.tv_files_empty_listview);

        views.setOnClickPendingIntent(R.id.iv_refresh, refreshPendingIntent);
        views.setOnClickPendingIntent(R.id.iv_all, selectAllPendingIntent);
        views.setOnClickPendingIntent(R.id.iv_docs, selectDocumentsPendingIntent);
        views.setOnClickPendingIntent(R.id.iv_images, selectImagesPendingIntent);
        views.setOnClickPendingIntent(R.id.iv_video, selectVideosPendingIntent);
        views.setOnClickPendingIntent(R.id.iv_music, selectMusicPendingIntent);
        views.setOnClickPendingIntent(R.id.iv_downloads, selectDownloadsPendingIntent);

        views.setPendingIntentTemplate(R.id.lv_files, fileClickPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_files);

        SharedPreferences prefs = context.getSharedPreferences(FileFinderConfigureActivity.PREFS_NAME, MODE_PRIVATE);
        String selected = prefs.getString(FileFinderConfigureActivity.KEY_SELECTED + appWidgetId, context.getString(R.string.all_files));

        switch (selected) {
            case SELECT_ALL_FILES:
                selectAll(context, appWidgetId);
                break;
            case SELECT_DOCUMENTS:
                selectDocuments(context, appWidgetId);
                break;
            case SELECT_IMAGES:
                selectImages(context, appWidgetId);
                break;
            case SELECT_VIDEOS:
                selectVideos(context, appWidgetId);
                break;
            case SELECT_MUSIC:
                selectMusic(context, appWidgetId);
                break;
            case SELECT_DOWNLOADS:
                selectDownloads(context, appWidgetId);
                break;
        }

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @SuppressLint("ApplySharedPref")
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(TAG, "onReceive: INTENT " + intent.getAction());
        Bundle extras = intent.getExtras();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidgetComponentName = new ComponentName(context.getPackageName(), getClass().getName());

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName);
        int appWidgetId = 0;
        if (extras != null) {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        SharedPreferences.Editor prefsEditor = context.getSharedPreferences(FileFinderConfigureActivity.PREFS_NAME, MODE_PRIVATE).edit();

        switch (intent.getAction()) {
            case ACTION_REFRESH:
                if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_files);
                } else {
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_files);
                }
                break;
            case ACTION_SELECT_ALL:
                prefsEditor.putString(FileFinderConfigureActivity.KEY_SELECTED + appWidgetId, context.getString(R.string.all_files)).commit();
                selectAll(context, appWidgetId);
                break;
            case ACTION_SELECT_DOCUMENTS:
                prefsEditor.putString(FileFinderConfigureActivity.KEY_SELECTED + appWidgetId, context.getString(R.string.documents)).commit();
                selectDocuments(context, appWidgetId);
                break;
            case ACTION_SELECT_IMAGES:
                Log.d(TAG, "onReceive: IMAGES");
                prefsEditor.putString(FileFinderConfigureActivity.KEY_SELECTED + appWidgetId, context.getString(R.string.images)).commit();
                selectImages(context, appWidgetId);
                break;
            case ACTION_SELECT_VIDEOS:
                prefsEditor.putString(FileFinderConfigureActivity.KEY_SELECTED + appWidgetId, context.getString(R.string.videos)).commit();
                selectVideos(context, appWidgetId);
                break;
            case ACTION_SELECT_MUSIC:
                prefsEditor.putString(FileFinderConfigureActivity.KEY_SELECTED + appWidgetId, context.getString(R.string.music)).commit();
                selectMusic(context, appWidgetId);
                break;
            case ACTION_SELECT_DOWNLOADS:
                prefsEditor.putString(FileFinderConfigureActivity.KEY_SELECTED + appWidgetId, context.getString(R.string.downloads)).commit();
                selectDownloads(context, appWidgetId);
                break;
            case ACTION_FILE_SELECTED:
                Log.d(TAG, "onReceive: ACTION_FILE_SELECTED");
                String filePath = intent.getExtras().getString(EXTRA_ITEM_FILE_PATH, null);
                Intent intentFileDetail = new Intent(context, FileDetailActivity.class);
                intentFileDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentFileDetail.putExtra("filePath", filePath);
                context.startActivity(intentFileDetail);
            default:
                Log.d(TAG, "onReceive: default");
                break;
        }
    }

    private void selectAll(Context context, int appWidgetId) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.file_finder_widget_provider);
        views.setInt(R.id.iv_all,"setBackgroundResource", R.drawable.ic_baseline_circle_24);
        views.setInt(R.id.iv_docs, "setBackgroundResource", R.drawable.ic_baseline_circle_24_inactive);
        views.setInt(R.id.iv_images, "setBackgroundResource", R.drawable.ic_baseline_circle_24_inactive);
        views.setInt(R.id.iv_video, "setBackgroundResource", R.drawable.ic_baseline_circle_24_inactive);
        views.setInt(R.id.iv_music, "setBackgroundResource", R.drawable.ic_baseline_circle_24_inactive);
        views.setInt(R.id.iv_downloads, "setBackgroundResource", R.drawable.ic_baseline_circle_24_inactive);
        appWidgetManager.partiallyUpdateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_files);
    }

    private void selectDocuments(Context context, int appWidgetId) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.file_finder_widget_provider);
        views.setInt(R.id.iv_docs, "setBackgroundResource", R.drawable.ic_baseline_circle_24);
        views.setInt(R.id.iv_all, "setBackgroundResource", R.drawable.ic_baseline_circle_24_inactive);
        views.setInt(R.id.iv_images, "setBackgroundResource", R.drawable.ic_baseline_circle_24_inactive);
        views.setInt(R.id.iv_video, "setBackgroundResource", R.drawable.ic_baseline_circle_24_inactive);
        views.setInt(R.id.iv_music, "setBackgroundResource", R.drawable.ic_baseline_circle_24_inactive);
        views.setInt(R.id.iv_downloads, "setBackgroundResource", R.drawable.ic_baseline_circle_24_inactive);
        appWidgetManager.partiallyUpdateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_files);
    }

    private void selectImages(Context context, int appWidgetId) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.file_finder_widget_provider);
        views.setInt(R.id.iv_images, "setBackgroundResource", R.drawable.ic_baseline_circle_24);
        views.setInt(R.id.iv_all, "setBackgroundResource", R.drawable.ic_baseline_circle_24_inactive);
        views.setInt(R.id.iv_docs, "setBackgroundResource", R.drawable.ic_baseline_circle_24_inactive);
        views.setInt(R.id.iv_video, "setBackgroundResource", R.drawable.ic_baseline_circle_24_inactive);
        views.setInt(R.id.iv_music, "setBackgroundResource", R.drawable.ic_baseline_circle_24_inactive);
        views.setInt(R.id.iv_downloads, "setBackgroundResource", R.drawable.ic_baseline_circle_24_inactive);
        appWidgetManager.partiallyUpdateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_files);
    }

    private void selectVideos(Context context, int appWidgetId) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.file_finder_widget_provider);
        views.setInt(R.id.iv_video, "setBackgroundResource", R.drawable.ic_baseline_circle_24);
        views.setInt(R.id.iv_all, "setBackgroundResource", R.drawable.ic_baseline_circle_24_inactive);
        views.setInt(R.id.iv_docs, "setBackgroundResource", R.drawable.ic_baseline_circle_24_inactive);
        views.setInt(R.id.iv_images, "setBackgroundResource", R.drawable.ic_baseline_circle_24_inactive);
        views.setInt(R.id.iv_music, "setBackgroundResource", R.drawable.ic_baseline_circle_24_inactive);
        views.setInt(R.id.iv_downloads, "setBackgroundResource", R.drawable.ic_baseline_circle_24_inactive);
        appWidgetManager.partiallyUpdateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_files);
    }

    private void selectMusic(Context context, int appWidgetId) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.file_finder_widget_provider);
        views.setInt(R.id.iv_music, "setBackgroundResource", R.drawable.ic_baseline_circle_24);
        views.setInt(R.id.iv_all, "setBackgroundResource", R.drawable.ic_baseline_circle_24_inactive);
        views.setInt(R.id.iv_docs, "setBackgroundResource", R.drawable.ic_baseline_circle_24_inactive);
        views.setInt(R.id.iv_images, "setBackgroundResource", R.drawable.ic_baseline_circle_24_inactive);
        views.setInt(R.id.iv_video, "setBackgroundResource", R.drawable.ic_baseline_circle_24_inactive);
        views.setInt(R.id.iv_downloads, "setBackgroundResource", R.drawable.ic_baseline_circle_24_inactive);
        appWidgetManager.partiallyUpdateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_files);
    }

    private void selectDownloads(Context context, int appWidgetId) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.file_finder_widget_provider);
        views.setInt(R.id.iv_downloads, "setBackgroundResource", R.drawable.ic_baseline_circle_24);
        views.setInt(R.id.iv_all, "setBackgroundResource", R.drawable.ic_baseline_circle_24_inactive);
        views.setInt(R.id.iv_docs, "setBackgroundResource", R.drawable.ic_baseline_circle_24_inactive);
        views.setInt(R.id.iv_images, "setBackgroundResource", R.drawable.ic_baseline_circle_24_inactive);
        views.setInt(R.id.iv_video, "setBackgroundResource", R.drawable.ic_baseline_circle_24_inactive);
        views.setInt(R.id.iv_music, "setBackgroundResource", R.drawable.ic_baseline_circle_24_inactive);
        appWidgetManager.partiallyUpdateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_files);
    }

}