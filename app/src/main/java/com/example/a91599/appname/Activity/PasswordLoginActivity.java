package com.example.a91599.appname.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a91599.appname.Bean.ApiResult;
import com.example.a91599.appname.Bean.User;
import com.example.a91599.appname.Bean.User;
import com.example.a91599.appname.R;
import com.example.a91599.appname.Service.LoginService;
import com.example.a91599.appname.Service.PreferenceService;
import com.example.a91599.appname.Service.RetrofitUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PasswordLoginActivity extends AppCompatActivity {
    private EditText ed_phone_number,ed_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_login);
        ed_phone_number = (EditText)findViewById(R.id.ed_phone_num_l);
        ed_password = (EditText)findViewById(R.id.ed_pass_l);
        Button btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }
    public void login(){
        final String phone_number = ed_phone_number.getText().toString();
        String password = ed_password.getText().toString();
        if (TextUtils.isEmpty(phone_number)){
            Toast.makeText(getApplicationContext(), "手机号码不能为空",
                    Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(), "密码不能为空",
                    Toast.LENGTH_SHORT).show();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://job.zhuyefeng.com/")  //要访问的主机地址，注意以 /（斜线） 结束，不然可能会抛出异常
                .addConverterFactory(GsonConverterFactory.create()) //添加Gson
                .client(RetrofitUtils.getInstance().addTimeOut(30).addHttpLog().build())
                .build();
        LoginService loginService = retrofit.create(LoginService.class);
        Call<ApiResult<List<User>>> call =loginService.getResult(phone_number,password);
        call.enqueue(new Callback<ApiResult<List<User>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResult<List<User>>> call, @NonNull Response<ApiResult<List<User>>> response) {
                if(response.isSuccessful()){
                    PreferenceService.putString("configuration","configuration","login");
                    PreferenceService.putString("phone","phone",ed_phone_number.getText().toString());
                    Intent intent = new Intent(PasswordLoginActivity.this,HomeActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(), "" +
                                    "密码错误",
                            Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ApiResult<List<User>>> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
