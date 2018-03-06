package com.sxshi.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.sxshi.android.bean.News;
import com.sxshi.android.cache.DoubleCache;

public class ImageDetialActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detial);
        imageView = findViewById(R.id.iv_detial);

        Intent intent = getIntent();
        News news = intent.getParcelableExtra("news");

        ImageLoader.getInstance(this)
                .setImageCache(new DoubleCache(this))
                .loadImage(news.getDetialUrl(), imageView);
    }
}
