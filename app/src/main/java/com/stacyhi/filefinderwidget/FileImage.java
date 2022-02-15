package com.stacyhi.filefinderwidget;

import android.graphics.Bitmap;

public class FileImage {
    private final Bitmap bitmap;
    private final int drawable;

    public FileImage(Bitmap bitmap, int drawable) {
        this.bitmap = bitmap;
        this.drawable = drawable;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getDrawable() {
        return drawable;
    }
}
