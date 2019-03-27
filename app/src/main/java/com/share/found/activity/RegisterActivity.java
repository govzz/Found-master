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
        onSetTitle("Sign in");
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
                    onToast("User name can NOT be empty");
                    return;
                }
                if (TextUtils.isEmpty(phone)){
                    onToast("Phone Number can NOT be empty");
                    return;
                }
                if (TextUtils.isEmpty(pwd)||TextUtils.isEmpty(pwdAgain)){
                    onToast("Password can NOT be empty");
                    return;
                }
                if (!pwd.equals(pwdAgain)){
                    onToast("Password do not match");
                    return;
                }
                showProgressDialog(RegisterActivity.this,"Sign up...");
                User user = new User();
                user.setUsername(name);
                user.setMobilePhoneNumber(phone);
                user.setPassword(pwd);
                user.signUp(new SaveListener<BmobUser>() {
                    @Override
                    public void done(BmobUser bmobUser, BmobException e) {
                        hidProgressDialog();
                        if (e==null){
                            onToast("Sign up Successfully");
                            finish();
                        }else{
                            onToast("Sing up Failed"+e.getLocalizedMessage());
                        }
                    }
                });

                break;

        }
    }
}
