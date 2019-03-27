package com.share.found.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.share.found.R;
import com.share.found.base.BaseActivity;


public class ImageBigActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bigimage);
        onSetTitle("Photo");
        PhotoView iv =(PhotoView) findViewById(R.id.photo_view);
        String url = getIntent().getStringExtra("url");
        Glide.with(this).load(url).error(R.drawable.icon_fail).into(iv);

    }
}
