package com.share.found.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.share.found.base.HomEvent;
import com.share.found.R;
import com.share.found.base.BaseActivity;
import com.share.found.bean.LostAndFound;
import com.share.found.bean.User;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;


public class ReleaseActivity extends BaseActivity {
    private EditText et_title;
    private TextView et_content;
    private ImageView iv_img;
    private static final int Take_Photo = 0;
    private BmobFile bmobFile;
    private boolean fileSuccess = false;
    private TextView tv_address;
    private TextView tv_add;
    private List<String> tag = new ArrayList<>();
    private TextView tv_time;
    private TextView et_phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        onSetTitle("Release");
        initView();
    }


    public BmobGeoPoint bmobGeoPoint;


    private void initView() {
        et_title = findViewById(R.id.et_title);
        et_phone = findViewById(R.id.et_phone);
        et_content = findViewById(R.id.et_content);
        tv_time = findViewById(R.id.tv_time);
        tv_add = findViewById(R.id.tv_add);
        iv_img = findViewById(R.id.iv_img);
        tv_address = findViewById(R.id.tv_address);
        tv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ReleaseActivity.this,LocationActivity.class),100);
            }
        });
        iv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ReleaseActivity.this, SubmitImageActivity.class);
                startActivityForResult(intent, Take_Photo);
            }
        });
        Button send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = et_title.getText().toString();
                String content = et_content.getText().toString();
                String phone = et_phone.getText().toString();
                String time = tv_time.getText().toString();
                String address = tv_address.getText().toString().trim();
                if (TextUtils.isEmpty(title)||TextUtils.isEmpty(content)){
                    onToast("Title and content can not be empty");
                    return;
                }
                if (TextUtils.isEmpty(phone)){
                    onToast("Enter your Phone number");
                    return;
                }
                if (TextUtils.isEmpty(address)){
                    onToast("Select Address");
                    return;
                }
                if (!fileSuccess){
                    onToast("Choose Photo");
                }


                upLoad(title,time,address,bmobGeoPoint,phone);
            }
        });
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText = new EditText(ReleaseActivity.this);
                new AlertDialog.Builder(ReleaseActivity.this)
                        .setTitle("Add tags")
                        .setView(editText)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String text = editText.getText().toString().trim();
                                if (TextUtils.isEmpty(text)){
                                    onToast("Enter Tags");
                                    return;
                                }else{
                                    tag.add(text);
                                    et_content.append("#"+text);
                                    dialogInterface.dismiss();
                                }

                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            }
        });
        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideInput();
                TimePickerView pvTime = new TimePickerBuilder(ReleaseActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        tv_time.setText(getTime(date));
                    }
                }) .setType(new boolean[]{true, true, true, true, true, false})
                        .setLabel("YEAR","MONTH","DAY","HOUR","MINUTE","")
                        .build();
                pvTime.show();
            }

        });
    }
    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }
    private  void hideInput(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm!=null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }
    private void upLoad(String title, String time,String address,BmobGeoPoint bmobGeoPoint,String phone) {
        showProgressDialog(ReleaseActivity.this,"Release in progress");
        LostAndFound lostAndFound =  new LostAndFound();
        lostAndFound.setTag(tag);
        lostAndFound.setTitle(title);
        lostAndFound.setAddress(address);
        lostAndFound.setPhone(phone);
        lostAndFound.setTime(time);
        lostAndFound.setFile(bmobFile);
        lostAndFound.setGeoPoint(bmobGeoPoint);
        lostAndFound.setUser(BmobUser.getCurrentUser(User.class));
        lostAndFound.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                hidProgressDialog();
                if (e==null){
                    onToast("Release Successfully");
                    EventBus.getDefault().post(new HomEvent());
                    finish();
                }else{
                    onToast("Release Failed");
                }
            }
        });
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
                        updateAvatarInServer(imageName);

                    }
                    break;

                case 100:
                    String address = data.getStringExtra("address");
                    double lat = data.getDoubleExtra("lat",0);
                    double log = data.getDoubleExtra("log",0);
                    tv_address.setText(address);
                    bmobGeoPoint =  new BmobGeoPoint(log,lat);
                    break;
                default:
                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void updateAvatarInServer(final String imageName) {
        bmobFile = new BmobFile(new File("/sdcard/share/" + imageName));
        bmobFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    onToast("Update Successfully");
                    Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/share/"
                            + imageName);
                    iv_img.setImageBitmap(bitmap);
                    fileSuccess = true;
                }else{
                    onToast("Update Failed");
                }
            }
        });
    }
}
