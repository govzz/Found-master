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
import cn.bmob.newim.bean.BmobIMLocationMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;

/**
 * 接收到的位置类型
 */
public class ReceiveLocationHolder extends BaseViewHolder {

  @BindView(R.id.iv_avatar)
  protected ImageView iv_avatar;

  @BindView(R.id.tv_time)
  protected TextView tv_time;

  @BindView(R.id.tv_location)
  protected TextView tv_location;
  Context context;
  public ReceiveLocationHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
    super(context, root, R.layout.item_chat_received_location,onRecyclerViewListener);
    this.context =context;
  }

  @Override
  public void bindData(Object o) {
    BmobIMMessage msg = (BmobIMMessage)o;
    //用户信息的获取必须在buildFromDB之前，否则会报错'Entity is detached from DAO context'
    final BmobIMUserInfo info = msg.getBmobIMUserInfo();
    //加载头像
    if (info.getAvatar()==null){
      iv_avatar.setImageResource(R.drawable.defalut_head);
    }else{
      Glide.with(context)
              .load(info.getAvatar())
              .placeholder(R.drawable.defalut_head)
              .into(iv_avatar);
    }
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    String time = dateFormat.format(msg.getCreateTime());
    tv_time.setText(time);
    //
    final BmobIMLocationMessage message = BmobIMLocationMessage.buildFromDB(msg);
    tv_location.setText(message.getAddress());
    //
    tv_location.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        toast("Longitude：" + message.getLongitude() + ",Latitude：" + message.getLatitude());
        if(onRecyclerViewListener!=null){
          onRecyclerViewListener.onItemClick(getAdapterPosition());
        }
      }
    });
    tv_location.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        if (onRecyclerViewListener != null) {
          onRecyclerViewListener.onItemLongClick(getAdapterPosition());
        }
        return true;
      }
    });

    iv_avatar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        toast("Click"+info.getName()+"Profile Photo");
      }
    });
  }

  public void showTime(boolean isShow) {
    tv_time.setVisibility(isShow ? View.VISIBLE : View.GONE);
  }
}