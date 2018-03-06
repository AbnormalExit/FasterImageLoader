package com.sxshi.giflib;

/**
 * Created by sxshi on 2018-3-6.
 */

public class GifLoader {
    static {
        System.loadLibrary("gifloader");
    }
    public  native void displayGif(String fileName);
}
