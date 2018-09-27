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
import com.example.a91599.appname.R;
import com.example.a91599.appname.Service.PreferenceService;
import com.example.a91599.appname.Service.RetrofitBuild;
import com.example.a91599.appname.Service.RetrofitService;

import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText ed_phone_num,ed_pass,ed_pass_again;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ed_pass =(EditText)findViewById(R.id.ed_pass);
        ed_pass_again = (EditText)findViewById(R.id.ed_pass_again);
        ed_phone_num = (EditText)findViewById(R.id.account_register);
        Button btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }


        });
    }

    public void register(){
        final String phone_number =ed_phone_num.getText().toString();
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
            RetrofitBuild retrofitBuild = new RetrofitBuild();
            RetrofitService service = retrofitBuild.service();
            retrofit2.Call<ApiResult> call =service.register(phone_number,password);
            call.enqueue(new Callback<ApiResult>() {
                @Override
                public void onResponse(@NonNull retrofit2.Call<ApiResult> call, @NonNull Response<ApiResult> response) {
                    if (response.isSuccessful() && response.body().isSuccessful()){
                        PreferenceService.putString("configuration","configuration","login");
                        PreferenceService.putString("phone","phone",ed_phone_num.getText().toString());
                        Log.v("msg",response.toString());
                        Toast.makeText(getApplicationContext(), "注册成功",
                                Toast.LENGTH_SHORT).show();
                        InputMethodManager imm =(InputMethodManager)getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(ed_pass_again.getWindowToken(), 0);
                        Intent intent = new Intent(RegisterActivity.this,HomeActivity.class);
                        ComponentName cn = intent.getComponent();
                        Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                        startActivity(mainIntent);
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(), response.body().getMsg(),
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull retrofit2.Call<ApiResult> call, @NonNull Throwable t) {
                    t.printStackTrace();
                }
            });
        }

    }
}
