package com.example.a91599.appname;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class MainActivity extends AppCompatActivity {
    EditText user;
    EditText codeVal;
    Button check;
    Button btn;
    final String country ="86";
    int i=60;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user=(EditText) findViewById(R.id.user);
        check =(Button) findViewById(R.id.check);
        codeVal =(EditText) findViewById(R.id.code);
        btn =(Button)findViewById(R.id.btn);
        SMSSDK.registerEventHandler(eh);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = user.getText().toString();
                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(getApplicationContext(), "手机号码不能为空",
                            Toast.LENGTH_SHORT).show();
                } else{
                    SMSSDK.getVerificationCode(country, phone);
                    check.setClickable(false);
                    Looper.prepare();
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
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = user.getText().toString();
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
            }
        });
    }

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
                    Toast.makeText(getApplicationContext(), "登录成功！",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(intent);
                    // 提交验证码成功,直接登录
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    Toast.makeText(getApplicationContext(), "验证码已经发送",
                            Toast.LENGTH_SHORT).show();
                } else if (result == SMSSDK.RESULT_ERROR) {
                    try {
                        Throwable throwable = (Throwable) data;
                        throwable.printStackTrace();
                    } catch (Exception e) {
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
