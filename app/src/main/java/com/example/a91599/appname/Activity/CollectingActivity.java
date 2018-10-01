package com.example.a91599.appname.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.a91599.appname.Adapter.MySearchAdapter;
import com.example.a91599.appname.Bean.ApiResult;
import com.example.a91599.appname.Bean.Collect;
import com.example.a91599.appname.Bean.NewsBean;
import com.example.a91599.appname.DBHelper.DBDao;
import com.example.a91599.appname.R;
import com.example.a91599.appname.Service.PreferenceService;
import com.example.a91599.appname.Service.RetrofitBuild;
import com.example.a91599.appname.Service.RetrofitService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectingActivity extends AppCompatActivity {
    private ListView listView;
    private MySearchAdapter adapter = null;
    private String token =PreferenceService.getString("token","token","");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collecting);
        listView = (ListView)findViewById(R.id.list_collected);
        adapter = new MySearchAdapter(this,new ArrayList<NewsBean>());
        RetrofitBuild retrofitBuild = new RetrofitBuild();
        RetrofitService service = retrofitBuild.service();
        Call<ApiResult<List<Collect<NewsBean>>>> call =service.getCollected(token);
        call.enqueue(new Callback<ApiResult<List<Collect<NewsBean>>>>() {
            @Override
            public void onResponse(Call<ApiResult<List<Collect<NewsBean>>>> call, Response<ApiResult<List<Collect<NewsBean>>>> response) {
                if (response.isSuccessful()&&response.body().isSuccessful()){
                    List<Collect<NewsBean>> list=response.body().getData();
                    Log.d("sxl", list != null ? list.toString() :"null");
                    if(list!=null){
                        Log.v("Size",""+list.size());
                        for(int i=0;i<list.size();i++){
                            NewsBean newsBean = list.get(i).getData();
                            if(list.get(i).getData().getCompany_image()==null){
                                newsBean.setCompany_image("http://job.zhuyefeng.com/uploads/20181001/11a631c4438cae2dafc47bfac6d534e2.png");
                            }else if (!list.get(i).getData().getCompany_image().startsWith("http://")){
                                newsBean.setCompany_image("http://job.zhuyefeng.com"+list.get(i).getData().getCompany_image());
                            }
                            Log.v("Company_image",newsBean.getCompany_image());
                            adapter.addItem(newsBean);
                        }
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(getApplicationContext(),"没有收藏",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResult<List<Collect<NewsBean>>>> call, Throwable t) {
                t.printStackTrace();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsBean newsBean = (NewsBean) parent.getItemAtPosition(position);
                if (newsBean.getLink()==null){
                    Toast.makeText(getApplicationContext(),"该链接为空",Toast.LENGTH_SHORT).show();
                }else {
                    DBDao dbDao =new DBDao(getApplicationContext());
                    dbDao.insert(newsBean);
                    Intent intent = new Intent(getApplicationContext(), ShowActivity.class);
                    intent.putExtra("link",newsBean.getLink());
                    startActivity(intent);
                }
            }
        });

    }
}
