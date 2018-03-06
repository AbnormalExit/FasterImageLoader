package com.sxshi.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sxshi.android.adapter.MyAdapter;
import com.sxshi.android.bean.News;
import com.sxshi.android.model.Images;

import java.util.ArrayList;
import java.util.List;

public class ImagesActivity extends AppCompatActivity {
    private MyAdapter adapter;
    private ListView lv_images;

    private List<News> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        adapter = new MyAdapter(this);
        lv_images = findViewById(R.id.lv_images);

        for (int i = 0; i < Images.imageThumbUrls.length; i++) {
            News news = new News();
            news.setImageUrl(Images.imageThumbUrls[i]);
            news.setDetialUrl(Images.imageUrls[i]);
            news.setNewsDetial("这是第" + (i + 1) + "张图片");
            data.add(news);
        }
        adapter.setImages(data);
        lv_images.setAdapter(adapter);

        initListener();
    }

    private void initListener() {
        final Intent intent = new Intent();
        lv_images.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News news = data.get(position);
                intent.setClass(ImagesActivity.this, ImageDetialActivity.class);
                intent.putExtra("news", news);
                startActivity(intent);
            }
        });
    }

}
