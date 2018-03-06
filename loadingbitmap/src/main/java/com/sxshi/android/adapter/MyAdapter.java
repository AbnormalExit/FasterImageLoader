package com.sxshi.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sxshi.android.ImageLoader;
import com.sxshi.android.R;
import com.sxshi.android.bean.News;
import com.sxshi.android.cache.DoubleCache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxshi on 2018-2-12.
 */

public class MyAdapter extends BaseAdapter {
    private List<News> mNews = new ArrayList<>();
    private Context mContext;

    public void setImages(List<News> images) {
        this.mNews = images;
        this.notifyDataSetChanged();
    }

    public MyAdapter(Context context) {
        this.mContext = context.getApplicationContext();
    }

    @Override
    public int getCount() {
        return mNews == null ? 0 : mNews.size();
    }

    @Override
    public News getItem(int position) {
        return mNews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_news, null);
            holder.iv_thum = convertView.findViewById(R.id.iv_thum);
            holder.tv_detial = convertView.findViewById(R.id.tv_detial);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        News news = mNews.get(position);
        holder.tv_detial.setText(news.getNewsDetial());
        ImageLoader.getInstance(mContext)
                .setImageCache(new DoubleCache(mContext))
                .loadImage(news.getImageUrl(), holder.iv_thum);
        return convertView;
    }

    static class Holder {
        ImageView iv_thum;
        TextView tv_detial;
    }
}
