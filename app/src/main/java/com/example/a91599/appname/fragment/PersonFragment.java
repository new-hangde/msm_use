package com.example.a91599.appname.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.a91599.appname.Activity.AddActivity;
import com.example.a91599.appname.Activity.HistoryActivity;
import com.example.a91599.appname.Activity.MainActivity;
import com.example.a91599.appname.R;
import com.example.a91599.appname.Service.PreferenceService;



public class PersonFragment extends Fragment {
    private TextView head_appear;
    private Button exit;
    String configuration=PreferenceService.getString("configuration","configuration","");
    String phone=PreferenceService.getString("phone","phone","");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.person_layout, null);
        PreferenceService.init(getContext());
        head_appear =(TextView)rootView.findViewById(R.id.person_tv);
        TextView history = (TextView) rootView.findViewById(R.id.history);
        TextView follow = (TextView) rootView.findViewById(R.id.follow);
        TextView setting = (TextView) rootView.findViewById(R.id.setting);
        TextView upload = (TextView) rootView.findViewById(R.id.upload);
        exit = (Button)rootView.findViewById(R.id.login_condition);
        TextSet();
        upload.setOnClickListener(clickListener);
        history.setOnClickListener(clickListener);
        follow.setOnClickListener(clickListener);
        setting.setOnClickListener(clickListener);
        exit.setOnClickListener(clickListener);
        return rootView;
    }
    private View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.follow:
                    break;
                case R.id.setting:
                    break;
                case R.id.history:
                    Intent intent = new Intent(getContext(), HistoryActivity.class);
                    startActivity(intent);
                    break;
                case R.id.upload:
                    Intent intent1 = new Intent(getContext(), AddActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.login_condition:
                    Log.v("configuration",configuration);
                    if(configuration.equals("login")){
                        PreferenceService.putString("configuration","configuration","exit");
                        PreferenceService.putString("token","token","");
                        TextSet();
                    }else if (configuration.equals("exit")){
                        startActivity(new Intent(getContext(), MainActivity.class));
                    }
                    break;
            }

        }
    };

    public void TextSet(){
        configuration=PreferenceService.getString("configuration","configuration","");
        Log.v("configuration",configuration);
        if (configuration.equals("login")) {
            head_appear.setText("欢迎您，"+phone);
            exit.setText("退出登录");
        }else if (configuration.equals("exit")){
            head_appear.setText("请登录！");
            exit.setText("点击登录");
        }
    }


}
