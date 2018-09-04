package com.example.a91599.appname;

import android.content.Context;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import cn.pedant.SweetAlert.SweetAlertDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 91599 on 2018/9/1.
 */

public class MainFragment extends Fragment implements View.OnClickListener{
    private int START = 0;
    private MyAdapter adapter;
    private ImageView ivShapeCircle;
    private TextView tvTotal,tvRecommend,tvSearch;
    private int offset = 0;
    private int currentIndex=1;
    private ListView listView;
    private SweetAlertDialog pDialog;
    private List<NewsBean> list = new ArrayList<>();
    private Gson gson;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, null);
        listView = (ListView)rootView.findViewById(R.id.viewer);
        ivShapeCircle = (ImageView) rootView.findViewById(R.id.iv_shape_circle);
        tvTotal=(TextView) rootView.findViewById(R.id.tv_total);
        tvRecommend=(TextView) rootView.findViewById(R.id.tv_recommend);
        tvTotal.setSelected(true);//推荐默认选中
        tvSearch=(TextView) rootView.findViewById(R.id.tv_search);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://job.zhuyefeng.com/")  //要访问的主机地址，注意以 /（斜线） 结束，不然可能会抛出异常
                .addConverterFactory(GsonConverterFactory.create()) //添加Gson
                .build();
        GitHubService service = retrofit.create(GitHubService.class);
        Call<ApiResult<List<NewsBean>>> call = service.getNew();
        call.enqueue(new Callback<ApiResult<List<NewsBean>>>() {
            @Override
            public void onResponse(Call<ApiResult<List<NewsBean>>> call, Response<ApiResult<List<NewsBean>>> response) {
                if (response.isSuccessful()){
                    list=response.body().getData();
                    Log.d("sxl", list != null ? list.toString() :"null");
                    if(adapter==null){
                        adapter = new MyAdapter(getContext(),list);
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }else {
                        int count = adapter.getCount();
                        if (count + 10 <= list.size()) {
                            for (int i = count + 1; i < count + 10; i++) {
                                NewsBean bean = new NewsBean();
                                bean.setTitle(list.get(i).getTitle());
                                bean.setSummary(list.get(i).getSummary());
                                bean.setCompany_image(list.get(i).getCompany_image());
                                bean.setUpdatetime(list.get(i).getUpdate_time());
                                bean.setFavourite_count(list.get(i).getFavourite_count());
                                adapter.addItem(bean);
                            }
                            adapter.notifyDataSetChanged();
                            listView.setSelection(START);
                        } else {
                            for (int i = count + 1; i <= list.size(); i++) {
                                NewsBean bean = new NewsBean();
                                bean.setCompany_image(list.get(i).getCompany_image());
                                bean.setSummary(list.get(i).getSummary());
                                bean.setTitle(list.get(i).getTitle());
                                bean.setUpdatetime(list.get(i).getUpdate_time());
                                bean.setFavourite_count(list.get(i).getFavourite_count());
                                adapter.addItem(bean);
                            }
                            adapter.notifyDataSetChanged();
                            listView.setSelection(START);
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<ApiResult<List<NewsBean>>> call, Throwable t) {
                t.printStackTrace();
            }
        });
        tvTotal.setOnClickListener(this);
        tvRecommend.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
        initCursorPosition();
        //setEnabled(currentIndex);
        return rootView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_total:
                changeTextColor(0);
                translateAnimation(0);
                break;
            case R.id.tv_recommend:
                changeTextColor(1);
                translateAnimation(1);
                break;
            case R.id.tv_search:
                changeTextColor(2);
                translateAnimation(2);
                break;
        }

    }

    private void initCursorPosition() {
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        Matrix matrix = new Matrix();
        //标题栏我用weight设置权重  分成5份
        //(width / 5) * 2  这里表示标题栏两个控件的宽度
        //(width / 10)  标题栏一个控件的2分之一
        //7  约等于原点宽度的一半
        matrix.postTranslate((width / 5) * 2 + (width / 10)-7,0);//图片平移
        ivShapeCircle.setImageMatrix(matrix);

        //一个控件的宽度  我的手机宽度是1080/5=216 不同的手机宽度会不一样哦
        offset=(width / 3);
    }

    private void changeTextColor(int index){
        tvTotal.setSelected(false);
        tvRecommend.setSelected(false);
        tvSearch.setSelected(false);
        switch (index) {
            case 0:
                tvTotal.setSelected(true);
                break;
            case 1:
                tvRecommend.setSelected(true);
                break;
            case 2:
                tvSearch.setSelected(true);
                break;
        }
    }

    private void translateAnimation(int index) {
        TranslateAnimation animation = null;
        if (index != currentIndex) {//做判定。防止重复点击，使程序崩溃
            switch (index) {
                case 0:
                    if (currentIndex == 1) {//从推荐移动到关注   X坐标向左移动216
                        animation = new TranslateAnimation(0, -offset, 0, 0);
                    } else if (currentIndex == 2) {//从同城移动到关注   X坐标向左移动216*2  记住起始x坐标是同城那里
                        animation = new TranslateAnimation(offset, -offset, 0, 0);
                    }
                    break;
                case 1:
                    if (currentIndex == 0) {//从关注移动到推荐   X坐标向右移动216
                        animation = new TranslateAnimation(-offset, 0, 0, 0);
                    } else if (currentIndex == 2) {//从同城移动到推荐   X坐标向左移动216
                        animation = new TranslateAnimation(offset, 0, 0, 0);
                    }
                    break;
                case 2:
                    if (currentIndex == 0) {//从关注移动到同城   X坐标向右移动216*2  记住起始x坐标是关注那里
                        animation = new TranslateAnimation(-offset, offset, 0, 0);
                    } else if (currentIndex == 1) {//从推荐移动到同城   X坐标向右移动216
                        animation = new TranslateAnimation(0, offset, 0, 0);
                    }
                    break;
            }
            animation.setFillAfter(true);
            animation.setDuration(300);
            ivShapeCircle.startAnimation(animation);
            currentIndex = index;
        }
    }

   public class MyAdapter extends BaseAdapter {
        Context context;
       List<NewsBean> list;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            Hand hand;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item, null);
                hand = new Hand();
                hand.title =(TextView)convertView.findViewById(R.id.title);
                hand.time = (TextView)convertView.findViewById(R.id.time);
                hand.img = (ImageView)convertView.findViewById(R.id.img);
                hand.summary =(TextView)convertView.findViewById(R.id.summary);
                hand.support =(TextView)convertView.findViewById(R.id.support);
               convertView.setTag(hand);
            } else {
                hand = (Hand) convertView.getTag();
            }
            Glide.with(context)
                    .load(list.get(position).getCompany_image())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(hand.img);
            hand.title.setText(list.get(position).getTitle());
            hand.summary.setText(list.get(position).getSummary());
            hand.support.setText("赞："+list.get(position).getFavourite_count());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(list.get(position).getCreatetime()*1000);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String dateStr = sdf.format(calendar.getTime());
            hand.time.setText(dateStr);

            return convertView;
        }

       private String format(int x) {
           String s = "" + x;
           if (s.length() == 1)
               s = "0" + s;
           return s;
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
       }
    }

}
