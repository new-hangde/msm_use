package com.example.a91599.appname.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.a91599.appname.Activity.ShowActivity;
import com.example.a91599.appname.Adapter.MyAdapter;
import com.example.a91599.appname.Bean.ApiResult;
import com.example.a91599.appname.Bean.NewsBean;
import com.example.a91599.appname.DBHelper.DBDao;
import com.example.a91599.appname.R;
import com.example.a91599.appname.Service.RetrofitBuild;
import com.example.a91599.appname.Service.RetrofitService;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchFragment extends Fragment implements View.OnClickListener {
    private MyAdapter adapter = null;
    private ListView listView;
    private EditText ed_search;
    public ImageButton ib_search;
    private RefreshLayout mRefreshLayout;
    private  int pageIndex =1;
    final int pageSize =15;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, null);
        listView =(ListView)rootView.findViewById(R.id.list_search);
        ed_search =(EditText)rootView.findViewById(R.id.ed_search);
        ib_search =(ImageButton)rootView.findViewById(R.id.ib_search);
        adapter = new MyAdapter(getContext(),new ArrayList<NewsBean>());
        ib_search.setOnClickListener(this);
        mRefreshLayout = (RefreshLayout)rootView.findViewById(R.id.refreshLayout_search);
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageIndex = 1;
                searchListShow(pageIndex,ed_search.getText().toString());
            }
        });
        //加载更多
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageIndex++;
                Log.v("page",""+pageIndex);
                searchListShow(pageIndex,ed_search.getText().toString());
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsBean newsBean =(NewsBean)parent.getItemAtPosition(position);
                DBDao dbDao =new DBDao(getContext());
                dbDao.insert(newsBean);
                Intent intent = new Intent(getContext(), ShowActivity.class);
                intent.putExtra("link",newsBean.getLink());
                startActivity(intent);
            }
        });
        return rootView;
    }

    private void searchListShow(final int page,String title){
        RetrofitBuild retrofitBuild = new RetrofitBuild();
        RetrofitService service = retrofitBuild.service();
        Call<ApiResult<List<NewsBean>>> call = service.getResult(page,title);
        call.enqueue(new Callback<ApiResult<List<NewsBean>>>() {
            @Override
            public void onResponse(Call<ApiResult<List<NewsBean>>> call, Response<ApiResult<List<NewsBean>>> response) {
                if (response.isSuccessful()){
                    List<NewsBean> list=response.body().getData();
                    Log.d("sxl", list != null ? list.toString() :"null");
                    if(list!=null){
                        Log.v("Size",""+list.size());
                        for(int i=0;i<list.size();i++){
                            NewsBean newsBean = list.get(i);
                            adapter.addItem(newsBean);
                        }
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        if (list.size()<pageSize){
                            Toast.makeText(getContext(),"最后一页",Toast.LENGTH_SHORT).show();
                            mRefreshLayout.setEnableLoadmore(false);
                        }else if(list.size()==pageSize){
                            mRefreshLayout.setEnableLoadmore(true);
                        }
                    }else{
                        pageIndex--;
                        Toast.makeText(getContext(),"没有了",Toast.LENGTH_SHORT).show();
                        mRefreshLayout.setEnableLoadmore(false);
                    }
                }
                Log.v("Size",adapter != null ? adapter.toString().trim() :"null");
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

    @Override
    public void onClick(View v) {
        mRefreshLayout.setEnableLoadmore(true);
        adapter.clear();
        pageIndex=1;
        String title = ed_search.getText().toString();
        if (!title.equals("")){
            searchListShow(pageIndex,title);
        }
    }
}
