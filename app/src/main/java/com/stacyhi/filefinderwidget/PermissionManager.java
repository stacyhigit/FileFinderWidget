package com.stacyhi.filefinderwidget;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionManager extends AppCompatActivity {
    public static final int REQUEST_ALL_PERMISSIONS = 100;

    public static final String[] permissionsStorage = new String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public static boolean checkPermissions(Context mContext, String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
            return true;
        }
    }

    public void requestAppPermissions(String[] permissions, int request) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (! Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        } else {
            List<String> remainingPermissions = new ArrayList<>();
            for (String permission : permissions) {
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    remainingPermissions.add(permission);
                }
            }
            if (remainingPermissions.size() > 0) {
                requestPermissions(
                        remainingPermissions.toArray(new String[0]),
                        request);
            }
        }
    }

    public void openSettingsDialog(String permissionName){
        //If User was asked permission before and denied
        new AlertDialog.Builder(this)
                .setTitle("Permission needed")
                .setMessage("Please enable permissions: " + permissionName)
                .setPositiveButton("Open Setting", (dialogInterface, i) -> {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", PermissionManager.this.getPackageName(),
                            null);
                    intent.setData(uri);
                    PermissionManager.this.startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    public String getPermissionConfigName(String perm) {
        if ("android.permission.READ_EXTERNAL_STORAGE".equals(perm)) {
            return "Storage";
        }
        return "";
    }
}
