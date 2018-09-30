package com.example.a91599.appname.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.a91599.appname.R;
import com.example.a91599.appname.Service.PreferenceService;
import com.example.a91599.appname.fragment.MainFragment;
import com.example.a91599.appname.fragment.PersonFragment;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private MainFragment mainFragment;
    private PersonFragment personFragment;
    private int currentId = R.id.tv_main;
    private TextView tvMain,  tvPerson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tvMain = (TextView) findViewById(R.id.tv_main);
        tvMain.setSelected(true);//首页默认选中
        tvPerson = (TextView) findViewById(R.id.tv_person);
        mainFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.main_container, mainFragment).commit();
        tvMain.setOnClickListener(this);
        tvPerson.setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
        if (view.getId() != currentId) {//如果当前选中跟上次选中的一样,不需要处理
            changeSelect(view.getId());//改变图标跟文字颜色的选中
            changeFragment(view.getId());//fragment的切换
            currentId = view.getId();//设置选中id
        }
    }
    private void changeFragment(int resId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();//开启一个Fragment事务
        hideFragments(transaction);//隐藏所有fragment
        if(resId==R.id.tv_main){//主页
            if(mainFragment==null){//如果为空先添加进来.不为空直接显示
                mainFragment = new MainFragment();
                transaction.add(R.id.main_container,mainFragment);
            }else {
                transaction.show(mainFragment);
            }
        }else if(resId==R.id.tv_person){//我的
            if(personFragment==null){
                personFragment = new PersonFragment();
                transaction.add(R.id.main_container,personFragment) ;
            }else {
                transaction.show(personFragment);
            }
        }
        transaction.commit();//一定要记得提交事务
    }
    private void changeSelect(int resId) {
        tvMain.setSelected(false);
        tvPerson.setSelected(false);

        switch (resId) {
            case R.id.tv_main:
                tvMain.setSelected(true);
                break;
            case R.id.tv_person:
                tvPerson.setSelected(true);
                break;
        }
    }

    private void hideFragments(FragmentTransaction transaction){
        if (mainFragment != null)//不为空才隐藏,如果不判断第一次会有空指针异常
            transaction.hide(mainFragment);
        if (personFragment != null)
            transaction.hide(personFragment);
    }
}
