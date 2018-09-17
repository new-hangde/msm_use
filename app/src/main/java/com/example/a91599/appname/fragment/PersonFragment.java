package com.example.a91599.appname.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a91599.appname.Activity.AddActivity;
import com.example.a91599.appname.Activity.HistoryActivity;
import com.example.a91599.appname.Activity.HomeActivity;
import com.example.a91599.appname.R;
import com.example.a91599.appname.Service.PreferenceService;



public class PersonFragment extends Fragment {
    private TextView head_appear,history,follow,setting,upload;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.person_layout, null);
        PreferenceService.init(getContext());
        head_appear =(TextView)rootView.findViewById(R.id.person_tv);
        history =(TextView)rootView.findViewById(R.id.history);
        follow =(TextView)rootView.findViewById(R.id.follow);
        setting =(TextView)rootView.findViewById(R.id.setting);
        upload = (TextView)rootView.findViewById(R.id.upload);
        String phone=PreferenceService.getString("phone","phone","");
        if (phone.length() != 0) {
            head_appear.setText("欢迎您，"+phone);
        }
        upload.setOnClickListener(clickListener);
        history.setOnClickListener(clickListener);
        follow.setOnClickListener(clickListener);
        setting.setOnClickListener(clickListener);
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
            }

        }
    };


}
