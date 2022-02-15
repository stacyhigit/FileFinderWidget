package com.stacyhi.filefinderwidget;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import java.io.File;

public class FileDetail {
    private final Context context;
    private final File file;

    public FileDetail(Context context, File file) {
        this.file = file;
        this.context = context;
    }

    public FileImage getImage () {
        String type = getType();
        if (type != null) {
            if (type.equals("application/pdf")) {
                return new FileImage(null, R.drawable.ic_file_pdf);
            }

            switch (type.split("/")[0]) {
                case "audio":
                    try {
                        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                        mmr.setDataSource(file.getPath());
                        byte [] data = mmr.getEmbeddedPicture();
                        return new FileImage(BitmapFactory.decodeByteArray(data,0, data.length), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return new FileImage(null, R.drawable.ic_file_music);
                    }
                case "image":
                    try {
                        //return new FileImage(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(file.getAbsolutePath()), 100, 100), 0);
                        return new FileImage(BitmapFactory.decodeFile(file.getAbsolutePath()), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return new FileImage(null, R.drawable.ic_file_image);
                    }
                case "video":
                    try {
                        return new FileImage(ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(),
                                MediaStore.Video.Thumbnails.MINI_KIND), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return new FileImage(null, R.drawable.ic_file_video);
                    }
                default:
                    return new FileImage(null, R.drawable.ic_file);
            }
        }
        return new FileImage(null, R.drawable.ic_file);
    }

    public File getFile() {
        return file;
    }

    public Uri getUri() {
        return FileProvider.getUriForFile(context,
                BuildConfig.APPLICATION_ID + ".provider", file);
    }

    public String getType() {
        try {
            Uri uri = getUri();
            return context.getContentResolver().getType(uri);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isDocument(){
        String type = getType();
        return !type.startsWith("audio/") &&
                !type.startsWith("video/") &&
                !type.startsWith("image/") &&
                !type.equals("application/octet-stream");
    }

    public long lastModified(){ return file.lastModified(); }
}
