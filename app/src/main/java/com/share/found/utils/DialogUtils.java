package com.share.found.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.share.found.R;


/**
 * 对话框工具类
 */
public class DialogUtils {


    private static Dialog pd;

    private static TextView progressTvContent;

    /**
     * 隐藏进度对话框
     */
    public static void hideProgressDialog() {
        if (pd != null) {
            pd.cancel();
            pd = null;
        }
    }
    /**
     * 显示进度对话框
     * @param context
     * @param message
     * @param cancelable
     */
    public static void showProgressDialog(Context context, String message, boolean cancelable) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.dialog_custom_progress, null);
        progressTvContent = (TextView) convertView.findViewById(R.id.dialog_custom_progress_tv_content);
        progressTvContent.setText(message);

        if (pd == null) {
            pd = new Dialog(context, R.style.transparentWindow);
            //设置窗口样式
            Window window = pd.getWindow();
            //得到窗口属性
            WindowManager.LayoutParams wl = window.getAttributes();
            //设置窗口宽高，让按钮平铺屏幕
            wl.width = ViewGroup.LayoutParams.MATCH_PARENT;//使用match_parent
            wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;//使用wrap_content
            //通知属性改变，设置了属性才会生效
            pd.onWindowAttributesChanged(wl);
            pd.setCanceledOnTouchOutside(cancelable);
        }
        //设置对话框的内容视图
        pd.setContentView(convertView);
        pd.show();
    }
}
