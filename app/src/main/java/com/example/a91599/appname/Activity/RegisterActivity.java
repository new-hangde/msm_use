package com.example.a91599.appname.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a91599.appname.Bean.ApiResult;
import com.example.a91599.appname.Bean.User;
import com.example.a91599.appname.R;
import com.example.a91599.appname.Service.RegisterService;
import com.example.a91599.appname.Service.RetrofitUtils;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    private EditText ed_phone_num,ed_pass,ed_pass_again;
    private Button btn_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ed_pass =(EditText)findViewById(R.id.ed_pass);
        ed_pass_again = (EditText)findViewById(R.id.ed_pass_again);
        ed_phone_num = (EditText)findViewById(R.id.ed_phone_num);
         btn_register= (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }


        });
    }

    public void register(){
        String phone_number =ed_phone_num.getText().toString();
        String password = null;
        boolean flag;
        if (ed_pass.getText().toString().equals(ed_pass_again.getText().toString())){
             password = ed_pass.getText().toString();
            flag = true;
        } else {
            Toast.makeText(getApplicationContext(), "清保持两次密码一致",
                    Toast.LENGTH_SHORT).show();
            flag = false;
        }
         if (TextUtils.isEmpty(phone_number)){
            Toast.makeText(getApplicationContext(), "手机号码不能为空",
                    Toast.LENGTH_SHORT).show();
             flag =false;
        } else if (TextUtils.isEmpty(ed_pass.getText().toString())||
                TextUtils.isEmpty(ed_pass_again.getText().toString())){
            Toast.makeText(getApplicationContext(), "密码不能为空",
                    Toast.LENGTH_SHORT).show();
             flag =false;
        }
        Log.v("flag",flag+"");
        if (flag){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://job.zhuyefeng.com/")  //要访问的主机地址，注意以 /（斜线） 结束，不然可能会抛出异常
                    .addConverterFactory(GsonConverterFactory.create()) //添加Gson
                    .client(RetrofitUtils.getInstance().addTimeOut(30).addHttpLog().build())
                    .build();
            RegisterService registerService = retrofit.create(RegisterService.class);
            retrofit2.Call<ApiResult> call =registerService.getResult(phone_number,password);
            call.enqueue(new Callback<ApiResult>() {
                @Override
                public void onResponse(retrofit2.Call<ApiResult> call, Response<ApiResult> response) {
                    if (response.isSuccessful()){
                        Log.v("msg",response.toString());
                        Toast.makeText(getApplicationContext(), "注册成功",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<ApiResult> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(getApplicationContext(), "注册失败",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
