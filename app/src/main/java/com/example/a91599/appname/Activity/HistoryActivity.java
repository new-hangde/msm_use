package com.example.a91599.appname.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a91599.appname.Adapter.MyAdapter;
import com.example.a91599.appname.Bean.NewsBean;
import com.example.a91599.appname.DBHelper.DBDao;
import com.example.a91599.appname.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private TextView tv_clear;
    private ListView lv_history;
    private MyAdapter adapter;
    DBDao dbDao = new DBDao(this);
    private List<NewsBean> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        tv_clear =(TextView)findViewById(R.id.tv_clear);
        lv_history =(ListView)findViewById(R.id.lv_history);
        ListSet();
    }

    @Override
    protected void onResume(){
        super.onResume();
        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbDao.clearAll();
                adapter.clear();
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void ListSet(){
        list = dbDao.query();
        if (adapter==null){
            adapter = new MyAdapter(this,list);
            lv_history.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }else {
            for (int i = 0; i < list.size(); i++) {
                NewsBean bean = new NewsBean();
                bean.setCompany_image(list.get(i).getCompany_image());
                bean.setSummary(list.get(i).getSummary());
                bean.setTitle(list.get(i).getTitle());
                bean.setUpdatetime(list.get(i).getUpdatetime());
                bean.setFavourite_count(list.get(i).getFavourite_count());
                adapter.addItem(bean);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
