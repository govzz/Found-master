package com.share.found.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.share.found.R;
import com.share.found.base.BaseActivity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;


public class ChangeActivity extends BaseActivity {
    EditText etWritePwd;
    Button btnLogin;

    private ChangeActivity context;
    private String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        context = this;
        init();

    }

    private void init() {
        type = getIntent().getStringExtra("type");
        etWritePwd = findViewById(R.id.et_write_pwd);
        btnLogin = findViewById(R.id.btn_login);
        switch (type){
            case "phone":
                onSetTitle("Change Phone Number");
                etWritePwd.setHint("Enter Phone Number");
                break;
            case "password":
                onSetTitle("Change Password");
                etWritePwd.setHint("Enter Password");
                break;

        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pwd = etWritePwd.getText().toString().trim();
                if (type.equals("phone")){
                    if (TextUtils.isEmpty(pwd)) {
                        Toast.makeText(context, "Can NOT be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    BmobUser newUser = new BmobUser();
                    newUser.setMobilePhoneNumber(pwd);
                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    newUser.update(bmobUser.getObjectId(),new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                onToast("Update Successful");
                                finish();
                            }else{
                                onToast("Update Failed:" + e.getMessage());
                            }
                        }
                    });
                }
                if (type.equals("password")){
                    if (TextUtils.isEmpty(pwd)) {
                        Toast.makeText(context, "Can NOT be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    BmobUser newUser = new BmobUser();
                    newUser.setPassword(pwd);
                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    newUser.update(bmobUser.getObjectId(),new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                onToast("Update Successful");
                                finish();
                            }else{
                                onToast("Update Failed:" + e.getMessage());
                            }
                        }
                    });

                }
            }
        });
    }



}
