package com.example.a91599.appname.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a91599.appname.Bean.ApiResult;
import com.example.a91599.appname.Bean.User;
import com.example.a91599.appname.R;
import com.example.a91599.appname.Service.PreferenceService;
import com.example.a91599.appname.Service.RetrofitBuild;
import com.example.a91599.appname.Service.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordLoginActivity extends AppCompatActivity {
    private EditText ed_phone_number,ed_password;
    String phone=PreferenceService.getString("phone","phone","");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceService.init(this);
        setContentView(R.layout.activity_password_login);
        ed_phone_number = (EditText)findViewById(R.id.account_input);
        ed_password = (EditText)findViewById(R.id.ed_pass_l);
        Button btn_login = (Button) findViewById(R.id.btn_login);
        if (phone.length()!=0)
            ed_phone_number.setText(phone);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }
    public void login(){
        final String phone_number = ed_phone_number.getText().toString();
        final String password = ed_password.getText().toString();
        if (TextUtils.isEmpty(phone_number)){
            Toast.makeText(getApplicationContext(), "手机号码不能为空",
                    Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(), "密码不能为空",
                    Toast.LENGTH_SHORT).show();
        }
        RetrofitBuild retrofitBuild = new RetrofitBuild();
        RetrofitService service = retrofitBuild.service();
        Call<ApiResult<User>> call =service.login(phone_number,password);
        call.enqueue(new Callback<ApiResult<User>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResult<User>> call, @NonNull Response<ApiResult<User>> response) {
                if(response.isSuccessful() && response.body().isSuccessful()){
                    PreferenceService.putString("configuration","configuration","login");
                    PreferenceService.putString("phone","phone",ed_phone_number.getText().toString());
                    String  token = response.body().getData().getToken();
                    PreferenceService .putString("token","token",token);
                    Log.v("token",token);
                    InputMethodManager imm =(InputMethodManager)getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(ed_password.getWindowToken(), 0);
                    Intent intent = new Intent(PasswordLoginActivity.this,HomeActivity.class);
                    ComponentName cn = intent.getComponent();
                    Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                    startActivity(mainIntent);
                }else {
                    Toast.makeText(getApplicationContext(), response.body().getMsg(),
                            Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ApiResult<User>> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
