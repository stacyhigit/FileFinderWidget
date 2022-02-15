package com.stacyhi.filefinderwidget;

import static android.content.Context.MODE_PRIVATE;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.io.File;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FileWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FileDataProvider(this.getApplicationContext(), intent);
    }
}

    class FileDataProvider implements RemoteViewsService.RemoteViewsFactory {
        private static final String TAG = "FileDataProvider";
        private final Context mContext;
        private final int appWidgetId;
        private List<FileDetail> fileList = new ArrayList<>();
        private final SharedPreferences prefs;
        private String selected = "All Files";


        public FileDataProvider(Context mContext, Intent intent) {
            this.mContext = mContext;
            this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            prefs = mContext.getSharedPreferences(FileFinderConfigureActivity.PREFS_NAME, MODE_PRIVATE);
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            Log.d(TAG, "onDataSetChanged: START");
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);

            selected = prefs.getString(FileFinderConfigureActivity.KEY_SELECTED + appWidgetId, mContext.getString(R.string.all_files));

            RemoteViews loadingView = new RemoteViews(mContext.getPackageName(), R.layout.file_finder_widget_provider);
            loadingView.setViewVisibility(R.id.pb_refresh, View.VISIBLE);
            loadingView.setTextViewText(R.id.tv_files_empty_listview, "Loading");
            appWidgetManager.partiallyUpdateAppWidget(appWidgetId, loadingView);

            RemoteViews loadingDoneView = new RemoteViews(mContext.getPackageName(), R.layout.file_finder_widget_provider);
            if (PermissionManager.checkPermissions(mContext, PermissionManager.permissionsStorage)) {
                fileList = getFiles();
                Log.d(TAG, "onDataSetChanged: fileList " + fileList.size());
                if (fileList.size() == 0) {
                    Log.d(TAG, "onDataSetChanged: fileList size " + fileList.size());
                    loadingDoneView.setTextViewText(R.id.tv_files_empty_listview, "No files found");
                } else {
                    loadingDoneView.setTextViewText(R.id.tv_files_empty_listview, "Loading");
                }

                loadingDoneView.setScrollPosition(R.id.lv_files, 0);

        } else {
            loadingDoneView.setTextViewText(R.id.tv_files_empty_listview, mContext.getResources().getString(R.string.add_permissions));
            fileList.clear();
        }

            loadingDoneView.setViewVisibility(R.id.pb_refresh, View.INVISIBLE);
            appWidgetManager.partiallyUpdateAppWidget(appWidgetId, loadingDoneView);
            Log.d(TAG, "onDataSetChanged: END");
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            return fileList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (position >= fileList.size()) {
                return getLoadingView();
            }
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.file_row);
            DateTimeFormatter dtfYearMonthDayTime = DateTimeFormatter.ofPattern("yyyy MMM d h:mm a");

            try {
                FileDetail fileDetail = fileList.get(position);
                if (fileDetail.getFile() == null) {
                    views.setTextViewText(R.id.tv_file_name, selected);
                    views.setViewVisibility(R.id.iv_file_image, View.GONE);
                    views.setViewVisibility(R.id.tv_file_date, View.GONE);

                } else {
                    views.setViewVisibility(R.id.iv_file_image, View.VISIBLE);
                    views.setViewVisibility(R.id.tv_file_date, View.VISIBLE);

                    FileImage fileImage = fileDetail.getImage();

                    if (fileImage.getBitmap() != null) {
                        views.setImageViewBitmap(R.id.iv_file_image, fileImage.getBitmap());
                    } else {
                        views.setImageViewResource(R.id.iv_file_image, fileImage.getDrawable());
                    }

                    views.setTextViewText(R.id.tv_file_name, fileDetail.getFile().getName());
                    views.setTextViewText(R.id.tv_file_date, dtfYearMonthDayTime.format(Instant.ofEpochMilli(fileDetail.lastModified()).atZone(ZoneId.systemDefault())));
                    Intent fillIntent = new Intent();
                    fillIntent.setData(Uri.parse(fillIntent.toUri(Intent.URI_INTENT_SCHEME)));
                    fillIntent.putExtra(FileFinderWidgetProvider.EXTRA_ITEM_FILE_PATH, fileDetail.getFile().getAbsolutePath());
                    views.setOnClickFillInIntent(R.id.layout_file_row, fillIntent);
                }

                return views;
            } catch (Exception e) {
                fileList.clear();
                Log.e(TAG, "getViewAt: ERROR");
                e.printStackTrace();
                setErrorView("Error loading files");
                return views;
            }
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        private List<FileDetail> getFiles() {

            File pdfFile = new File("/storage/emulated/0/Download/sample.pdf");
            Log.d(TAG, "getFiles: /storage/emulated/0/Download/sample.pdf " + pdfFile.length() );

            int fileCountPosition = prefs.getInt(FileFinderConfigureActivity.KEY_FILE_COUNT + appWidgetId, 2);
            String fileCount = mContext.getResources().getStringArray(R.array.display_numbers)[fileCountPosition];

            File directory = new File(Environment.getExternalStorageDirectory().toString());
            FileWalker fileWalker = new FileWalker();
            fileWalker.walk(mContext, directory, appWidgetId);
            FileDetail[] fileList = fileWalker.getFileList().toArray(new FileDetail[0]);
            Log.d(TAG, "getFiles: fileList " + fileList.length);

            List<FileDetail> sortedList = new ArrayList<>();

            Log.d(TAG, "getFiles: selected " + selected);

            switch (selected) {
                case FileFinderWidgetProvider.SELECT_ALL_FILES:
                case FileFinderWidgetProvider.SELECT_DOWNLOADS: // filtered in FileWalker
                    sortedList = Arrays.stream(fileList)
                        .sorted(Comparator.comparingLong(FileDetail::lastModified).reversed())
                        .limit(Long.parseLong(fileCount))
                        .collect(Collectors.toList());
                    break;
                case FileFinderWidgetProvider.SELECT_DOCUMENTS:
                    sortedList = Arrays.stream(fileList)
                            .sorted(Comparator.comparingLong(FileDetail::lastModified).reversed())
                            .filter(FileDetail::isDocument)
                            .limit(Long.parseLong(fileCount))
                            .collect(Collectors.toList());
                    break;
                case FileFinderWidgetProvider.SELECT_IMAGES:
                    sortedList = Arrays.stream(fileList)
                            .sorted(Comparator.comparingLong(FileDetail::lastModified).reversed())
                            .filter(fileDetail -> fileDetail.getType().startsWith("image/"))
                            .limit(Long.parseLong(fileCount))
                            .collect(Collectors.toList());
                    break;
                case FileFinderWidgetProvider.SELECT_VIDEOS:
                    sortedList = Arrays.stream(fileList)
                            .sorted(Comparator.comparingLong(FileDetail::lastModified).reversed())
                            .filter(fileDetail -> fileDetail.getType().startsWith("video/"))
                            .limit(Long.parseLong(fileCount))
                            .collect(Collectors.toList());
                    break;
                case FileFinderWidgetProvider.SELECT_MUSIC:
                    sortedList = Arrays.stream(fileList)
                            .sorted(Comparator.comparingLong(FileDetail::lastModified).reversed())
                            .filter(fileDetail -> fileDetail.getType().startsWith("audio/"))
                            .limit(Long.parseLong(fileCount))
                            .collect(Collectors.toList());
                    break;
            }
            sortedList.add(0, new FileDetail(mContext, null)); // placeholder for header
            return sortedList;
        }

        public void setErrorView(String errorMessage) {
            Log.e(TAG, "setErrorView: " + errorMessage);
            fileList.clear();
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
            RemoteViews errorView = new RemoteViews(mContext.getPackageName(), R.layout.file_finder_widget_provider);
            errorView.setTextViewText(R.id.tv_files_empty_listview, errorMessage);
            errorView.setViewVisibility(R.id.tv_files_empty_listview, View.VISIBLE);
            errorView.setViewVisibility(R.id.pb_refresh, View.INVISIBLE);
            appWidgetManager.partiallyUpdateAppWidget(appWidgetId, errorView);
        }

}
