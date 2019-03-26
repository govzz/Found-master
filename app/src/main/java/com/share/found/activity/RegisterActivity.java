package com.share.found.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.share.found.R;
import com.share.found.base.BaseActivity;
import com.share.found.bean.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private EditText etName;
    private EditText etPwd;
    private EditText etRepwd;
    private EditText etPhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        onSetTitle("注册");
        init();
    }

    private void init() {
        etName = (EditText)findViewById(R.id.id_et_username);
        etPwd = (EditText)findViewById(R.id.id_et_password);
        etPhone = (EditText)findViewById(R.id.id_et_phone);
        etRepwd = (EditText)findViewById(R.id.id_et_repassword);
        Button mBtnREgister = (Button) findViewById(R.id.id_btn_register);
        mBtnREgister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_btn_register:
                String name = etName.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String pwd = etPwd.getText().toString().trim();
                String pwdAgain = etRepwd.getText().toString().trim();
                if (TextUtils.isEmpty(name)){
                    onToast("用户名不能为空");
                    return;
                }
                if (TextUtils.isEmpty(phone)){
                    onToast("电话不能为空");
                    return;
                }
                if (TextUtils.isEmpty(pwd)||TextUtils.isEmpty(pwdAgain)){
                    onToast("密码不能为空");
                    return;
                }
                if (!pwd.equals(pwdAgain)){
                    onToast("2次密码不一致");
                    return;
                }
                showProgressDialog(RegisterActivity.this,"注册中...");
                User user = new User();
                user.setUsername(name);
                user.setMobilePhoneNumber(phone);
                user.setPassword(pwd);
                user.signUp(new SaveListener<BmobUser>() {
                    @Override
                    public void done(BmobUser bmobUser, BmobException e) {
                        hidProgressDialog();
                        if (e==null){
                            onToast("注册成功");
                            finish();
                        }else{
                            onToast("注册失败"+e.getLocalizedMessage());
                        }
                    }
                });

                break;

        }
    }
}
