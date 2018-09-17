package com.example.a91599.appname.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.a91599.appname.Activity.ShowActivity;
import com.example.a91599.appname.Adapter.MyAdapter;
import com.example.a91599.appname.Bean.ApiResult;
import com.example.a91599.appname.Bean.NewsBean;
import com.example.a91599.appname.DBHelper.DBDao;
import com.example.a91599.appname.R;
import com.example.a91599.appname.Service.ShowService;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TotalListFragment extends Fragment {
    private MyAdapter adapter =null;
    private ListView listView;
    private RefreshLayout mRefreshLayout;
    private  int pageIndex =1;
    final int pageSize =15;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_total, null);
        listView =(ListView)rootView.findViewById(R.id.list_total);
        mRefreshLayout = (RefreshLayout)rootView.findViewById(R.id.refreshLayout);
        adapter = new MyAdapter(getContext(),new ArrayList<NewsBean>());
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageIndex = 1;
                totalListShow(pageIndex);
            }
        });
        //加载更多
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageIndex++;
                Log.v("page",""+pageIndex);
                totalListShow(pageIndex);
            }
        });

        mRefreshLayout.autoRefresh();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsBean newsBean = (NewsBean) parent.getItemAtPosition(position);
                DBDao dbDao =new DBDao(getContext());
                dbDao.insert(newsBean);
                Intent intent = new Intent(getContext(), ShowActivity.class);
                intent.putExtra("link",newsBean.getLink());
                startActivity(intent);
            }
        });
        return rootView;
    }

    public void totalListShow(final int page){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://job.zhuyefeng.com/")  //要访问的主机地址，注意以 /（斜线） 结束，不然可能会抛出异常
                .addConverterFactory(GsonConverterFactory.create()) //添加Gson
                .build();
        ShowService service = retrofit.create(ShowService.class);
        Call<ApiResult<List<NewsBean>>> call = service.getNews(page);
        call.enqueue(new Callback<ApiResult<List<NewsBean>>>() {
            @Override
            public void onResponse(Call<ApiResult<List<NewsBean>>> call, Response<ApiResult<List<NewsBean>>> response) {
                if (response.isSuccessful()){
                     List<NewsBean> list=response.body().getData();
                    Log.d("sxl", list != null ? list.toString() :"null");
                    if(list!=null){
                        Log.v("Size",""+list.size());
                        if( page ==1){
                            adapter.clear();
                        }
                        for(int i=0;i<list.size();i++){
                            NewsBean newsBean = list.get(i);
                            adapter.addItem(newsBean);
                        }
                        if (list.size()<pageSize){
                            Toast.makeText(getContext(),"最后一页",Toast.LENGTH_SHORT).show();
                            mRefreshLayout.setEnableLoadmore(false);
                        }else if(list.size()==pageSize){
                            mRefreshLayout.setEnableLoadmore(true);
                        }
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }else{
                        pageIndex--;
                        Toast.makeText(getContext(),"没有了",Toast.LENGTH_SHORT).show();
                        mRefreshLayout.setEnableLoadmore(false);
                    }
                }
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();
            }
            @Override
            public void onFailure(Call<ApiResult<List<NewsBean>>> call, Throwable t) {
                t.printStackTrace();
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();
            }
        });
    }
}
