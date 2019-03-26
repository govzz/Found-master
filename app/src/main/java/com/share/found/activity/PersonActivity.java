package com.share.found.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.share.found.R;
import com.share.found.base.BaseActivity;
import com.share.found.bean.User;
import com.share.found.utils.ActivityManager;

import java.io.File;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;


public class PersonActivity extends BaseActivity implements View.OnClickListener {
    private PersonActivity context;
    private TextView tv_phone;
    private TextView tv_name;
    private static final int Take_Photo = 0;
    private ImageView mIvImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        ActivityManager.addActivity(this);
        setContentView(R.layout.activity_person_info);
        onSetTitle("个人信息");
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        User user = BmobUser.getCurrentUser(User.class);
        tv_name.setText(BmobUser.getCurrentUser().getUsername());
        tv_phone.setText(BmobUser.getCurrentUser().getMobilePhoneNumber() + "");
        User person = BmobUser.getCurrentUser(User.class);
        String picFile = person.getAvatar();
        if (!TextUtils.isEmpty(picFile)){
            Glide.with(PersonActivity.this)
                    .load(picFile)
                    .placeholder(R.drawable.defalut_head)
                    .into(mIvImg);
        }
    }

    private void initView() {
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        mIvImg = (ImageView) findViewById(R.id.iv_img);
        tv_name = (TextView) findViewById(R.id.tv_name);
        RelativeLayout rl_pwd = (RelativeLayout) findViewById(R.id.rl_pwd);
        RelativeLayout rl_pic = (RelativeLayout) findViewById(R.id.rl_pic);
        RelativeLayout rL_mysend = (RelativeLayout) findViewById(R.id.rL_mysend);
        RelativeLayout rl_logout = (RelativeLayout) findViewById(R.id.rl_logout);
        RelativeLayout rl_conversation = (RelativeLayout) findViewById(R.id.rl_conversation);
        rl_pwd.setOnClickListener(this);
        rl_pic.setOnClickListener(this);
        rL_mysend.setOnClickListener(this);
        rl_logout.setOnClickListener(this);
        rl_conversation.setOnClickListener(this);
        RelativeLayout rl_phone = (RelativeLayout) findViewById(R.id.rl_phone);
        rl_phone.setOnClickListener(this);
    }

    @SuppressLint("SdCardPath")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Take_Photo:
                    if (data != null) {
                        String imageName = data.getStringExtra("imageName");
                        //返回数据了
                        Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/share/"
                                + imageName);
                        mIvImg.setImageBitmap(bitmap);
                        updateAvatarInServer(imageName);

                    }
                    break;


            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void updateAvatarInServer(String imageName) {
        final BmobFile bmobFile = new BmobFile(new File("/sdcard/share/" + imageName));
        bmobFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                User person = new User();
                person.setAvatar(bmobFile.getFileUrl());
                BmobUser user = BmobUser.getCurrentUser();
                person.update(user.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Toast.makeText(context, "图片上传成功", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "图片上传失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_logout:
                new AlertDialog.Builder(PersonActivity.this)
                        .setTitle("提示")
                        .setMessage("确定退出吗?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(PersonActivity.this, LoginActivity.class));
                                ActivityManager.finishAll();
                                BmobIM.getInstance().disConnect();
                                dialogInterface.dismiss();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
                break;
            case R.id.rL_mysend:
                Intent sendIntent = new Intent();
                sendIntent.setClass(context,MySendActivity.class);
                startActivity(sendIntent);
                break;
            case R.id.rl_pic:
                Intent intent = new Intent();
                intent.setClass(context, SubmitImageActivity.class);
                startActivityForResult(intent, Take_Photo);
                break;
            case R.id.rl_phone:
                Intent PIntent = new Intent();
                PIntent.setClass(context,ChangeActivity.class);
                PIntent.putExtra("type","phone");
                startActivity(PIntent);
                break;
            case R.id.rl_pwd:
                Intent passWordIntent = new Intent();
                passWordIntent.setClass(context,ChangeActivity.class);
                passWordIntent.putExtra("type","password");
                startActivity(passWordIntent);
                break;
            case R.id.rl_conversation:
                Intent cIntent = new Intent();
                cIntent.setClass(context,ConversationActivity.class);
                startActivity(cIntent);
                break;


        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.removeActivity(this);
    }
}
