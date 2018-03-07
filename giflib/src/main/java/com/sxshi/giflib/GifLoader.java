package com.sxshi.giflib;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ImageView;


/**
 * Created by sxshi on 2018-3-6.
 */

public class GifLoader {

    private GifHandler gifHandler = new GifHandler();
    private ImageView iv;

    private long gifPlaySpeed;/*设置加载的gif 速度*/


    private String filpath;/*需要加载的gif文件路径*/

    private Bitmap bitmap;


    private int gifPlayerCount=2;/*默认设置gif播放次数*/

    private int currentPlayer=0;/*当前播放多少次*/

    static {
        System.loadLibrary("gifloader");
    }

    private long gifFileType;

    private GifLoader(Config config) {
        if (config != null) {
            this.iv = config.iv;
            this.gifPlaySpeed = config.gifPlayerSpeed;
            this.filpath = config.filpath;
            this.gifFileType = getGifFileType(this.filpath);
        }
    }

    private native int getWidth(long gifFileType);

    private native int getHeight(long gifFileType);

    private native int updateFrame(long gifFileType, Bitmap bitmap);

    public native long getGifFileType(String fileName);

    public void displayGif() {
        int width = getWidth(this.gifFileType);
        int height = getHeight(this.gifFileType);
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int nextFrame = updateFrame(this.gifFileType, bitmap);

        if (iv != null) {
            iv.setImageBitmap(bitmap);
            /**
             *   设置gif加载速度 最慢就是加载完整张图片的时间
             */
            if (gifPlaySpeed>0&&gifPlaySpeed<=nextFrame){
                gifHandler.sendEmptyMessageDelayed(1, gifPlaySpeed);
            }else {
                gifHandler.sendEmptyMessageDelayed(1, nextFrame);
            }
        }
    }

    final class GifHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            int mNextFrame = updateFrame(gifFileType, bitmap);
            if (iv != null) {
                iv.setImageBitmap(bitmap);
                if (gifPlaySpeed>0&&gifPlaySpeed<=mNextFrame){
                    gifHandler.sendEmptyMessageDelayed(1, gifPlaySpeed);
                }else {
                    gifHandler.sendEmptyMessageDelayed(1, mNextFrame);
                }
            }
        }
    }

    public static class Config {

        private ImageView iv;

        private long gifPlayerSpeed;/*需要加载的gif 帧数*/


        private String filpath;/*需要加载的gif文件路径*/

        public Config setmGifPlayerSpeed(long gifPlayerSpeed) {
            this.gifPlayerSpeed = gifPlayerSpeed;
            return this;
        }

        public Config setFilpath(String filpath) {
            this.filpath = filpath;
            return this;
        }

        public Config into(ImageView imageView) {
            this.iv = imageView;
            return this;
        }

        public ImageView getIv() {
            return iv;
        }

        public long getGifPlayerSpeed() {
            return gifPlayerSpeed;
        }



        public String getFilpath() {
            return filpath;
        }

        public GifLoader build() {
            if (null == iv) {
                throw new IllegalArgumentException("please set ImageView load gif");
            }
            if (TextUtils.isEmpty(filpath)) {
                throw new IllegalArgumentException("please set gif filepath");
            }
            return new GifLoader(this);
        }
    }
}
