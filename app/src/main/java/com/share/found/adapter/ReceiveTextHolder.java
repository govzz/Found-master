package com.share.found.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.share.found.R;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;

/**
 * 接收到的文本类型
 */
public class ReceiveTextHolder extends BaseViewHolder {

  @BindView(R.id.iv_avatar)
  protected ImageView iv_avatar;

  @BindView(R.id.tv_time)
  protected TextView tv_time;

  @BindView(R.id.tv_message)
  protected TextView tv_message;

  public ReceiveTextHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
    super(context, root, R.layout.item_chat_received_message,onRecyclerViewListener);
  }

  @OnClick({R.id.iv_avatar})
  public void onAvatarClick(View view) {

  }

  @Override
  public void bindData(Object o) {
    final BmobIMMessage message = (BmobIMMessage)o;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    String time = dateFormat.format(message.getCreateTime());
    tv_time.setText(time);
    final BmobIMUserInfo info = message.getBmobIMUserInfo();
    if (info.getAvatar()==null){
      iv_avatar.setImageResource(R.drawable.defalut_head);
    }else{
      Glide.with(context)
              .load(info.getAvatar())
              .placeholder(R.drawable.defalut_head)
              .into(iv_avatar);
    }
    String content =  message.getContent();
    tv_message.setText(content);
    iv_avatar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (info==null){
          toast("The information from message is empty");
          return;
        }
        toast("Click" + info.getName() + "Profile Photo");
      }
    });
    tv_message.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          toast("Click"+message.getContent());
          if(onRecyclerViewListener!=null){
            onRecyclerViewListener.onItemClick(getAdapterPosition());
          }
        }
    });

    tv_message.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
          if (onRecyclerViewListener != null) {
            onRecyclerViewListener.onItemLongClick(getAdapterPosition());
          }
          return true;
        }
    });
  }

  public void showTime(boolean isShow) {
    tv_time.setVisibility(isShow ? View.VISIBLE : View.GONE);
  }
}