package com.share.found.base;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.share.found.R;
import com.share.found.utils.DialogUtils;

public class BaseActivity extends AppCompatActivity {

    public void showProgressDialog(Context context , String content) {
        DialogUtils.showProgressDialog(context,content,false);
    }

    //设置标题
    public void onSetTitle(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.at_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.back_arr);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReturn();
            }
        });
        TextView mTitle = (TextView) toolbar.findViewById(R.id.at_title);
        mTitle.setText(title.toString());
    }
    public void onToast(String msg) {
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        hidProgressDialog();
    }
    public void onReturn(){
       finish();
    }

    public void hidProgressDialog() {
        DialogUtils.hideProgressDialog();
    }


}