package com.sxshi.android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.sxshi.android.utils.FileUtils;
import com.sxshi.android.utils.ImageUtils;
import com.sxshi.giflib.GifLoader;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ImageView imageView;
    private Button btn_normal, btn_btn_compress, btn_list, btn_gif;
    private GifLoader gifLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.iv_pic);
        btn_gif = findViewById(R.id.btn_gif);
        btn_normal = findViewById(R.id.btn_normal);
        btn_btn_compress = findViewById(R.id.btn_compress);
        btn_list = findViewById(R.id.btn_list);

        btn_gif.setOnClickListener(view -> {

            File file = new File(Environment.getExternalStorageDirectory(), "demo.gif");
            if (!file.exists()) {
                Log.e(TAG, "file not found");
                return;
            }
            gifLoader = new GifLoader.Config()
                    .setFilpath(file.getAbsolutePath())
                    .into(imageView)
                    .build();
            gifLoader.displayGif();
        });

        btn_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.demo);
            }
        });

        btn_btn_compress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageLoader.getInstance(MainActivity.this).loadBitmap(R.drawable.demo, imageView);
            }
        });

        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String url = "https://s9.postimg.org/s2w6g18cf/coffee.jpg";
//                ImageLoader.getInstance(MainActivity.this).loadImage(url, imageView);
                Intent intent = new Intent(MainActivity.this, ImagesActivity.class);
                startActivity(intent);
            }
        });
    }


}
