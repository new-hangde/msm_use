package com.example.a91599.appname.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a91599.appname.Bean.ApiResult;
import com.example.a91599.appname.R;
import com.example.a91599.appname.Service.RetrofitBuild;
import com.example.a91599.appname.Service.RetrofitService;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends AppCompatActivity {
    private EditText ed_image;
    private EditText ed_summary;
    private EditText ed_link;
    private EditText ed_title;
    private final int REQUEST_SYSTEM_PIC = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ed_image =(EditText)findViewById(R.id.ed_image);
        ed_link =(EditText)findViewById(R.id.ed_link);
        ed_summary =(EditText)findViewById(R.id.ed_summary);
        ed_title =(EditText)findViewById(R.id.ed_title);
        Button bt_add = (Button) findViewById(R.id.bt_add);
        Button bt_upload = (Button) findViewById(R.id.bt_upload);
        bt_upload.setOnClickListener(clickListener);
        bt_add.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.bt_add:
                    upload();
                    break;
                case R.id.bt_upload:
                    if (ContextCompat.checkSelfPermission(AddActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(AddActivity.this, new
                                String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    } else {
                        //打开系统相册
                        openAlbum();
                    }
                    break;
            }
        }
    };

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_SYSTEM_PIC);//打开系统相册

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SYSTEM_PIC && resultCode == RESULT_OK && null != data) {
            if (Build.VERSION.SDK_INT >= 19) {
                handleImageOnKitkat(data);
            } else {
                handleImageBeforeKitkat(data);
            }
        }
    }


    @TargetApi(25)
    private void handleImageOnKitkat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content:" +
                        "//downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是File类型的uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath);//根据图片路径显示图片

    }

    private void handleImageBeforeKitkat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);

    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            ed_image.setText(imagePath);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    private void upload(){
        Map<String, RequestBody> map = new HashMap<>();
        String title = ed_title.getText().toString();
        String summary =ed_summary.getText().toString();
        String link = ed_link.getText().toString();
        map.put("title", RequestBody.create(null, String.valueOf(title)));
        map.put("summary", RequestBody.create(null, String.valueOf(summary)));
        map.put("link", RequestBody.create(null, String.valueOf(link)));
        String path = ed_image.getText().toString();
        File file = new File(path);
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part imageBodyPart = MultipartBody.Part.createFormData("image", file.getName(), imageBody);
        RetrofitBuild retrofitBuild = new RetrofitBuild();
        RetrofitService service = retrofitBuild.service();
        retrofit2.Call<ApiResult> call =  service.upload(map,imageBodyPart);
        call.enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<ApiResult> call, @NonNull Response<ApiResult> response) {
                if (response.isSuccessful() && response.body().isSuccessful()){
                    Log.v("msg",response.toString());
                    ApiResult apiResult= (ApiResult) response.body();
                    if (apiResult==null){
                        Toast.makeText(AddActivity.this, "内容为空", Toast.LENGTH_SHORT).show();
                    }else{
                        Log.v(" ApiResult",apiResult.toString());
                        Log.v(" title",ed_title.getText().toString());
                        Log.v(" summary",ed_summary.getText().toString());
                        Log.v(" link",ed_link.getText().toString());
                    }

                    Toast.makeText(AddActivity.this, "success to upload", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddActivity.this,HomeActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onFailure(@NonNull retrofit2.Call<ApiResult> call, @NonNull Throwable t) {
                Toast.makeText(AddActivity.this, "failed to upload", Toast.LENGTH_SHORT).show();
                t.printStackTrace();

            }
        });
    }
}
