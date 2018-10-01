package com.example.a91599.appname.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.a91599.appname.Bean.NewsBean;
import com.example.a91599.appname.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MySearchAdapter extends BaseAdapter {
    Context context;
    List<NewsBean> list;

    public MySearchAdapter(Context context, List<NewsBean> list) {
        this.context =context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MySearchAdapter.Hand hand;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_search, null);
            hand = new MySearchAdapter.Hand();
            hand.title =(TextView)convertView.findViewById(R.id.title);
            hand.time = (TextView)convertView.findViewById(R.id.time);
            hand.img = (ImageView)convertView.findViewById(R.id.img);
            hand.summary =(TextView)convertView.findViewById(R.id.summary);
            convertView.setTag(hand);
        } else {
            hand = (MySearchAdapter.Hand) convertView.getTag();
        }
        Glide.with(context)
                .load(list.get(position).getCompany_image())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(hand.img);
        hand.title.setText(list.get(position).getTitle());
        hand.summary.setText(list.get(position).getSummary());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(list.get(position).getCreatetime()*1000);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String dateStr = sdf.format(calendar.getTime());
        hand.time.setText(dateStr);
        return convertView;
    }

    public void clear(){
        list.clear();
    }

    public void addItem(NewsBean item) {
        list.add(item);
    }

    class Hand {
        ImageView img;
        TextView title;
        TextView summary;
        TextView time;
    }
}
