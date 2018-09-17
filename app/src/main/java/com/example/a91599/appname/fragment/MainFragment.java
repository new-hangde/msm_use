package com.example.a91599.appname.fragment;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.a91599.appname.Adapter.MessageGroupFragmentAdapter;
import com.example.a91599.appname.R;
import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment{
    private ViewPager vPager;
    private ImageView ivShapeCircle;
    private TextView tvTotal,tvRecommend,tvSearch;
    private List<Fragment> list = new ArrayList<>();
    private int offset = 0;
    private int currentIndex=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, null);
        vPager = (ViewPager) rootView.findViewById(R.id.viewpager_home);
        TotalListFragment totalListFragment = new TotalListFragment();
        RecommendListFragment recommendListFragment = new RecommendListFragment();
        SearchFragment searchFragment = new SearchFragment();
        list.add(totalListFragment);
        list.add(recommendListFragment);
        list.add(searchFragment);
        MessageGroupFragmentAdapter adapter = new MessageGroupFragmentAdapter(getActivity().getSupportFragmentManager(), list);
        vPager.setAdapter(adapter);
        vPager.setOffscreenPageLimit(2);
        vPager.setCurrentItem(0);
        //noinspection deprecation
        vPager.setOnPageChangeListener(pageChangeListener);
        ivShapeCircle = (ImageView) rootView.findViewById(R.id.iv_shape_circle);
        tvTotal=(TextView) rootView.findViewById(R.id.tv_total);
        tvRecommend=(TextView) rootView.findViewById(R.id.tv_recommend);
        tvTotal.setSelected(true);//推荐默认选中
        tvSearch=(TextView) rootView.findViewById(R.id.tv_search);
        tvTotal.setOnClickListener(clickListener);
        tvRecommend.setOnClickListener(clickListener);
        tvSearch.setOnClickListener(clickListener);


        initCursorPosition();
        return rootView;
    }

    private View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_total:
                    vPager.setCurrentItem(0);
                    break;
                case R.id.tv_recommend:
                    vPager.setCurrentItem(1);
                    break;
                case R.id.tv_search:
                    vPager.setCurrentItem(2);
                    break;
            }
        }
    };

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int index) {
            changeTextColor(index);
            translateAnimation(index);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };



    private void initCursorPosition() {
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        Matrix matrix = new Matrix();
        //标题栏我用weight设置权重  分成5份
        //(width / 5) * 2  这里表示标题栏两个控件的宽度
        //(width / 10)  标题栏一个控件的2分之一
        //7  约等于原点宽度的一半
        matrix.postTranslate((width / 6)-7,0);//图片平移
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
                        animation = new TranslateAnimation(offset,0 , 0, 0);
                    } else if (currentIndex == 2) {//从同城移动到关注   X坐标向左移动216*2  记住起始x坐标是同城那里
                        animation = new TranslateAnimation(offset*2, 0, 0, 0);
                    }
                    break;
                case 1:
                    if (currentIndex == 0) {//从关注移动到推荐   X坐标向右移动216
                        animation = new TranslateAnimation(0, offset, 0, 0);
                    } else if (currentIndex == 2) {//从同城移动到推荐   X坐标向左移动216
                        animation = new TranslateAnimation(offset*2, offset, 0, 0);
                    }
                    break;
                case 2:
                    if (currentIndex == 0) {//从关注移动到同城   X坐标向右移动216*2  记住起始x坐标是关注那里
                        animation = new TranslateAnimation(0, offset*2, 0, 0);
                    } else if (currentIndex == 1) {//从推荐移动到同城   X坐标向右移动216
                        animation = new TranslateAnimation(offset, offset*2, 0, 0);
                    }
                    break;
            }
            if (animation != null) {
                animation.setFillAfter(true);
            }
            if (animation != null) {
                animation.setDuration(300);
            }
            ivShapeCircle.startAnimation(animation);
            currentIndex = index;
        }
    }







}
