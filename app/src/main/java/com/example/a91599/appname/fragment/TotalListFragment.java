package com.example.a91599.appname.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.a91599.appname.Adapter.MyAdapter;
import com.example.a91599.appname.Bean.ApiResult;
import com.example.a91599.appname.Bean.NewsBean;
import com.example.a91599.appname.R;
import com.example.a91599.appname.Service.ShowService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TotalListFragment extends Fragment {
    private MyAdapter adapter;
    private ListView listView;
    private List<NewsBean> list = new ArrayList<>();
    private int START = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_total, null);
        listView =(ListView)rootView.findViewById(R.id.list_total);
        TotalListShow();
        return rootView;
    }

    public void TotalListShow(){
        if (adapter!=null){
            adapter.clear();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://job.zhuyefeng.com/")  //要访问的主机地址，注意以 /（斜线） 结束，不然可能会抛出异常
                .addConverterFactory(GsonConverterFactory.create()) //添加Gson
                .build();
        ShowService service = retrofit.create(ShowService.class);
        Call<ApiResult<List<NewsBean>>> call = service.getNews();
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
                            for (int i = count; i < count + 10; i++) {
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
                            for (int i = count; i < list.size(); i++) {
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
                call.cancel();
            }
            @Override
            public void onFailure(Call<ApiResult<List<NewsBean>>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
