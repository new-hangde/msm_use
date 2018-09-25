package com.example.a91599.appname.Activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.a91599.appname.R;
import com.example.a91599.appname.Service.PreferenceService;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class MainActivity extends AppCompatActivity {
    EditText user;
    TextView tv_pass_login;
    TextView tv_register;
    EditText codeVal;
    Button check;
    Button btn;
    final String country ="86";
    int i=60;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceService.init(this);
        user=(EditText) findViewById(R.id.user);
        check =(Button) findViewById(R.id.check);
        codeVal =(EditText) findViewById(R.id.code);
        btn =(Button)findViewById(R.id.btn);
        tv_pass_login =(TextView)findViewById(R.id.tv_pass_login);
        tv_register =(TextView)findViewById(R.id.tv_register);
        tv_register.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_pass_login.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        String configuration=PreferenceService.getString("configuration","configuration","");
        Log.v("configuration","configuration:"+configuration);
        if (configuration.length() != 0) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
        SMSSDK.registerEventHandler(eh);
        check.setOnClickListener(clickListener);
        btn.setOnClickListener(clickListener);
        tv_pass_login.setOnClickListener(clickListener);
        tv_register.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String phone = user.getText().toString();
            switch (v.getId()){
                case R.id.check:
                    if(TextUtils.isEmpty(phone)){
                        Toast.makeText(getApplicationContext(), "手机号码不能为空",
                                Toast.LENGTH_SHORT).show();
                    } else{
                        SMSSDK.getVerificationCode(country, phone);
                        check.setClickable(false);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (; i >0; i--) {
                                    handler.sendEmptyMessage(-1);
                                    if (i <= 0) {
                                        break;
                                    }
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                handler.sendEmptyMessage(-2);//倒计时结束执行
                            }
                        }).start();
                    }
                    break;

                case R.id.btn:
                    String code = codeVal.getText().toString();
                    if(TextUtils.isEmpty(phone)){
                        Toast.makeText(getApplicationContext(), "手机号码不能为空",
                                Toast.LENGTH_SHORT).show();
                    }else if(TextUtils.isEmpty(code)){
                        Toast.makeText(getApplicationContext(), "验证码不能为空",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        SMSSDK.submitVerificationCode(country,phone,code);
                    }
                    break;

                case R.id.tv_pass_login:
                    Intent intent1 = new Intent(MainActivity.this,PasswordLoginActivity.class);
                    startActivity(intent1);
                    break;

                case R.id.tv_register:
                    Intent intent2 = new Intent(MainActivity.this,RegisterActivity.class);
                    startActivity(intent2);
                    break;
            }
        }
    };
     Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -1) {
                //进行倒计时
                check.setText( ""+i+"s");
            } else if (msg.what == -2) {
                //重新发送验证码
                check.setText("重新发送");
                check.setClickable(true);
                i = 60;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    PreferenceService.putString("configuration","configuration","login");
                    PreferenceService.putString("phone","phone",user.getText().toString());
                    Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(intent);
                    // 提交验证码成功,直接登录
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {

                } else if (result == SMSSDK.RESULT_ERROR) {
                    try {
                        Throwable throwable = (Throwable) data;
                        throwable.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    ((Throwable) data).printStackTrace();
                }
            }
        }
    };




    EventHandler eh=new EventHandler(){
        @Override
        public void afterEvent(int event, int result, Object data) {
            Message msg = new Message();
            msg.arg1 = event;
            msg.arg2 = result;
            msg.obj = data;
            handler.sendMessage(msg);
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterEventHandler(eh);
    }

}
