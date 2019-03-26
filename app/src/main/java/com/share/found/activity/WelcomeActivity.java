package com.share.found.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;


import com.share.found.R;
import com.share.found.utils.StatusBarUtil;

import java.io.File;

import cn.bmob.v3.BmobUser;

public class WelcomeActivity extends AppCompatActivity {

    private ImageView iv_guide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        StatusBarUtil.setTranslucent(WelcomeActivity.this,0);
        iv_guide = (ImageView) findViewById(R.id.iv_guide);
        initFile();
        final ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scaleAnim.setFillAfter(true);
        scaleAnim.setDuration(3000);
        scaleAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                if (BmobUser.getCurrentUser()!=null) {
                    startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                    finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        iv_guide.startAnimation(scaleAnim);
    }
    private void initFile() {
        File dir = new File("/sdcard/share");
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
}
