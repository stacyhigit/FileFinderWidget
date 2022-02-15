package com.stacyhi.filefinderwidget;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;

import com.stacyhi.filefinderwidget.databinding.ActivityFileFinderConfigureBinding;

public class FileFinderConfigureActivity extends PermissionManager {
    private static final String TAG = "FileFinderConfigureActivity";
    public static final String PREFS_NAME = "com.stacyhi.fileFinderWidget";
    public static final String KEY_EXCLUDE_SYSTEM_FILES = "excludeSystemFiles";
    public static final String KEY_FILE_COUNT = "fileCount";
    public static final String KEY_SELECTED = "selected";

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    Spinner spinnerFilesCount;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch switchExcludeSystemFiles;

    View.OnClickListener addOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = FileFinderConfigureActivity.this;

            SharedPreferences.Editor prefsEditor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
            prefsEditor.putInt(KEY_FILE_COUNT + mAppWidgetId, spinnerFilesCount.getSelectedItemPosition());
            prefsEditor.putBoolean(KEY_EXCLUDE_SYSTEM_FILES + mAppWidgetId, switchExcludeSystemFiles.isChecked());
            prefsEditor.apply();

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            FileFinderWidgetProvider fileFinderWidgetProvider = new FileFinderWidgetProvider();
            fileFinderWidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public FileFinderConfigureActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        SharedPreferences prefs = this.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        setResult(RESULT_CANCELED);

        ActivityFileFinderConfigureBinding binding = ActivityFileFinderConfigureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        spinnerFilesCount = binding.spinnerFilesCount;
        switchExcludeSystemFiles = binding.switchExcludeSystemFiles;
        binding.addButton.setOnClickListener(addOnClickListener);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        spinnerFilesCount.setSelection(prefs.getInt(KEY_FILE_COUNT + mAppWidgetId, 1));
        switchExcludeSystemFiles.setChecked(prefs.getBoolean(KEY_EXCLUDE_SYSTEM_FILES + mAppWidgetId, true));

        if (!checkPermissions(this, permissionsStorage)) {
            requestAppPermissions(permissionsStorage, REQUEST_ALL_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ALL_PERMISSIONS) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    if (shouldShowRequestPermissionRationale(permissions[i])) {
                        requestAppPermissions(permissionsStorage, REQUEST_ALL_PERMISSIONS);
                    } else {
                        openSettingsDialog(getPermissionConfigName(permissions[i]));
                    }
                    return;
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: Ok");
                }
            }
        }
    }
}