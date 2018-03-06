package com.sxshi.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.sxshi.giflib.GifLoader;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button btn_normal, btn_btn_compress, btn_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.iv_pic);
        btn_normal = findViewById(R.id.btn_normal);
        btn_btn_compress = findViewById(R.id.btn_compress);
        btn_list = findViewById(R.id.btn_list);

        GifLoader gifLoader = new GifLoader();
        gifLoader.displayGif("test url");

        btn_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.girl);
            }
        });

        btn_btn_compress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageLoader.getInstance(MainActivity.this).loadBitmap(R.drawable.coffee, imageView);
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
