package com.example.a91599.appname.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.a91599.appname.Bean.ApiResult;
import com.example.a91599.appname.Bean.NewsBean;
import com.example.a91599.appname.R;
import com.example.a91599.appname.Service.PreferenceService;
import com.example.a91599.appname.Service.RetrofitBuild;
import com.example.a91599.appname.Service.RetrofitService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyAdapter extends BaseAdapter {
    Context context;
    List<NewsBean> list;
    private String token =PreferenceService.getString("token","token","");

    public MyAdapter(Context context, List<NewsBean> list) {
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
        final Hand hand;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_total, null);
            hand = new Hand();
            hand.title =(TextView)convertView.findViewById(R.id.title);
            hand.time = (TextView)convertView.findViewById(R.id.time);
            hand.img = (ImageView)convertView.findViewById(R.id.img);
            hand.summary =(TextView)convertView.findViewById(R.id.summary);
            hand.support =(TextView)convertView.findViewById(R.id.support);
            hand.collect = (TextView)convertView.findViewById(R.id.collect);
            convertView.setTag(hand);
        } else {
            hand = (Hand) convertView.getTag();
        }
        Glide.with(context)
                .load(list.get(position).getCompany_image())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(hand.img);
        final int num = list.get(position).getFavourite_count();
        hand.title.setText(list.get(position).getTitle());
        hand.summary.setText(token);
        hand.support.setText(num+"");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(list.get(position).getCreatetime()*1000);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String dateStr = sdf.format(calendar.getTime());
        hand.time.setText(dateStr);
        final int jib_id = list.get(position).getId();
        RetrofitBuild retrofitBuild= new RetrofitBuild();
        RetrofitService service = retrofitBuild.service();
        Call<ApiResult> call_query = service.isCollected(token,jib_id);
        call_query.enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
                if(response.isSuccessful()&&response.body().isSuccessful()){
                    hand.collect.setSelected(true);
                    hand.collect.setText("已收藏");
                }else {
                    hand.collect.setSelected(false);
                    hand.collect.setText("收藏");
                }
            }
            @Override
            public void onFailure(Call<ApiResult> call, Throwable t) {
                    t.printStackTrace();
            }
        });
        hand.collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hand.collect.isSelected()){
                    RetrofitBuild retrofitBuild= new RetrofitBuild();
                    RetrofitService service = retrofitBuild.service();
                    Call<ApiResult>call_cancel = service.cancelCollect(token,jib_id);
                    call_cancel.enqueue(new Callback<ApiResult>() {
                        @Override
                        public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
                            if(response.isSuccessful()&&response.body().isSuccessful()){
                                Toast.makeText(context,response.body().getMsg(),Toast.LENGTH_SHORT).show();
                                hand.collect.setSelected(false);
                                hand.collect.setText("收藏");
                            }else {
                                Toast.makeText(context,response.body().getMsg(),Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<ApiResult> call, Throwable t) {
                                t.printStackTrace();
                        }
                    });
                }else {
                    RetrofitBuild retrofitBuild= new RetrofitBuild();
                    RetrofitService retrofitService = retrofitBuild.service();
                   Call<ApiResult> call_add = retrofitService.collect(token,jib_id);
                    call_add.enqueue(new Callback<ApiResult>() {
                        @Override
                        public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
                            if(response.isSuccessful()&&response.body().isSuccessful()){
                                hand.collect.setSelected(true);
                                hand.collect.setText("已收藏");
                                Toast.makeText(context,response.body().getMsg(),Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(context,response.body().getMsg(),Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<ApiResult> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            }
        });

        hand.support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hand.support.isSelected()){
                    hand.support.setSelected(false);
                    hand.support.setText(num+"");
                }else {
                    hand.support.setSelected(true);
                    hand.support.setText(num+1+"");
                }
            }
        });
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
        TextView support;
        TextView time;
        TextView collect;
    }
}
