package com.sxshi.giflib;

import android.graphics.Bitmap;

/**
 * Created by sxshi on 2018-3-6.
 */

public class GifLoader {

    static {
        System.loadLibrary("gifloader");
    }


    private long gifFileType;

    public GifLoader(String filePath) {
        gifFileType = displayGif(filePath);
    }

    public int getWidth() {
        return getWidth(gifFileType);
    }

    public int getHeight() {
        return getHeight(gifFileType);
    }

    public int updateFrame(Bitmap bitmap) {
        return updateFrame(gifFileType, bitmap);
    }

    public static native int getWidth(long gifFileType);

    public static native int getHeight(long gifFileType);

    public static native int updateFrame(long gifFileType, Bitmap bitmap);


    public static native long displayGif(String fileName);
}
