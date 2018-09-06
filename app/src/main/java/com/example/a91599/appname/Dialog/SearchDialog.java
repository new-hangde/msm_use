package com.example.a91599.appname.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.a91599.appname.R;

/**
 * Created by 91599 on 2018/9/5.
 */

public class SearchDialog extends Dialog {
    private Context context;
    EditText edTitle;
    TextView tvSubmit;
    TextView tvCancel;
    private ClickListenerInterface clickListenerInterface;

    public interface ClickListenerInterface {
        public void doConfirm();
        public void doCancel();
    }

    public SearchDialog(Context context) {
        super(context, R.style.dialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init(){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.search_dialog, null);
        setContentView(view);
        edTitle = (EditText) view.findViewById(R.id.et_search);
        tvSubmit = (TextView) view.findViewById(R.id.tv_ok);
        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);

        tvSubmit.setOnClickListener(new clickListener());
        tvCancel.setOnClickListener(new clickListener());
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics();
        lp.width = (int) (d.widthPixels * 0.6);
        dialogWindow.setAttributes(lp);
    }

    public void setClickListener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }


    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id){
                case R.id.tv_cancel:
                    clickListenerInterface.doCancel();
                    break;
                case R.id.tv_ok:
                    clickListenerInterface.doConfirm();
                    break;
            }

        }
    };
}
