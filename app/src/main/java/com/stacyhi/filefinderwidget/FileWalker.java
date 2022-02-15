package com.stacyhi.filefinderwidget;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileWalker {
    final List<FileDetail> allFiles = new ArrayList<>();

    public void walk(Context context, File root, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(FileFinderConfigureActivity.PREFS_NAME, MODE_PRIVATE);

        boolean excludeSystemFiles = prefs.getBoolean(FileFinderConfigureActivity.KEY_EXCLUDE_SYSTEM_FILES + appWidgetId, true);
        String exclude = excludeSystemFiles ? "^/storage/emulated/0/Android$": "";

        String selected = prefs.getString(FileFinderConfigureActivity.KEY_SELECTED + appWidgetId, context.getString(R.string.all_files));
        String include = selected.equals(FileFinderWidgetProvider.SELECT_DOWNLOADS) ? "/storage/emulated/0/Download" : "/";

        File[] listFiles = root.listFiles();
        try {
            if (listFiles != null) {
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        if (!file.getAbsolutePath().matches(exclude) &&
                                file.getAbsolutePath().startsWith(include) &&
                                !file.getName().startsWith(".")) {
                            walk(context, file, appWidgetId);
                        }
                    } else {
                        if (!file.getName().startsWith(".")) {
                            allFiles.add(new FileDetail(context, file));
                        }
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    public List<FileDetail> getFileList(){
        return allFiles;
    }

}

