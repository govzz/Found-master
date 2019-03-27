package com.share.found.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.share.found.R;
import com.share.found.bean.Comment;
import com.share.found.bean.LostAndFound;
import com.share.found.bean.User;
import com.share.found.activity.ChatActivity;
import com.share.found.activity.ImageBigActivity;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;


public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    private Context mContext;//承载上下文
    private List<Comment> mDataList;//动态数组承载数据使用
    private LayoutInflater mLayoutInflater;
    private int mHeaderCount  =1;
    private LostAndFound img ;

    public CommentAdapter(Context mContext, LostAndFound img, List<Comment> mDataList){
        this.mContext=mContext;
        this.mDataList=mDataList;
        this.img=img;
        mLayoutInflater= LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType ==ITEM_TYPE_HEADER) {
            return new HeaderViewHolder(mLayoutInflater.inflate(R.layout.item_trend, parent, false));
        } else if (viewType == ITEM_TYPE_CONTENT) {
            return  new CommentAdapter.ViewHolder(mLayoutInflater.inflate(R.layout.comment_item, parent, false));
        }
        return null;
    }

    //将数据绑定到控件
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            if (null==img) {
                return;
            }
            final HeaderViewHolder vh= (HeaderViewHolder) holder;
            vh.person_name.setText(img.getUser().getUsername());
            vh.iv_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, ImageBigActivity.class);
                    intent.putExtra("url",img.getFile().getUrl());
                    mContext.startActivity(intent);
                }
            });
            vh.time.setText(img.getCreatedAt());
            vh.trend_title.setText(img.getTitle()+"\n"+img.getTime());
            List<String> list = img.getTag();
            vh.trend_content.setAdapter(new TagAdapter<String>(list) {
                @Override
                public View getView(FlowLayout parent, int position, String o) {
                    TextView tv = (TextView) mLayoutInflater.inflate(R.layout.tv,
                            vh.trend_content, false);
                    tv.setText(o);
                    return tv;
                }
            });
            vh.tv_address.setText(img.getAddress());
            if (img.getUser().getAvatar()==null){
                vh.img.setImageResource(R.drawable.defalut_head);
            }else{
                Glide.with(mContext).load(img.getUser().getAvatar()).error(R.drawable.defalut_head).into(vh.img);
            }
            if (img.getFile()==null){
                vh.iv_img.setVisibility(View.GONE);
            }else{
                vh.iv_img.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(img.getFile().getUrl()).error(R.drawable.icon_fail).into(vh.iv_img);
            }
            vh.iv_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = img.getUser();
                    if (BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
                        Toast.makeText(mContext,"Not connected to IM server",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    BmobIMUserInfo info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar());
                    // 会话：4.1、创建一个常态会话入口，陌生人聊天
                    Intent intent = new Intent(mContext,ChatActivity.class);
                    BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(info, null);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("c", conversationEntrance);
                    intent.putExtra("data",bundle);
                    mContext.startActivity(intent);
                }
            });
            vh.iv_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(img.getPhone())){
                        return;
                    }
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + img.getPhone()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });

        } else if (holder instanceof CommentAdapter.ViewHolder) {
            final  Comment commentItem=mDataList.get(position-1);
            if (null==commentItem) {
                return;
            }
            CommentAdapter.ViewHolder vh= (CommentAdapter.ViewHolder) holder;
            vh.name.setText(commentItem.getUser().getUsername());
            vh.content.setText(commentItem.getContent());
            if (commentItem.getUser().getAvatar()==null){
                vh.avatarView.setImageResource(R.drawable.defalut_head);
            }else{
                Glide.with(mContext).load(commentItem.getUser().getAvatar()).error(R.drawable.defalut_head).into(vh.avatarView);
            }

            vh.timeView.setText(commentItem.getCreatedAt());
        }



    }
    @Override
    public int getItemCount() {
        return mHeaderCount + mDataList.size();
    }


    //找到控件，将其初始化
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView avatarView ;
        TextView name;
        TextView content;
        TextView timeView;
        public ViewHolder(View itemView) {
            super(itemView);
            avatarView= (ImageView) itemView.findViewById(R.id.avatarView);
            name= (TextView) itemView.findViewById(R.id.name);
            content= (TextView) itemView.findViewById(R.id.content);
            timeView= (TextView) itemView.findViewById(R.id.timeView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderCount != 0 && position < mHeaderCount) {
            //头部View
            return ITEM_TYPE_HEADER;
        }  else {
            //内容View
            return ITEM_TYPE_CONTENT;
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        ImageView iv_img;
        ImageView iv_chat;
        ImageView iv_call;
        private TextView person_name ;
        private TextView trend_title ;
        private TextView tv_address ;
        TagFlowLayout trend_content ;
        private TextView time ;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            img= (ImageView) itemView.findViewById(R.id.person_pic);
            iv_img= (ImageView) itemView.findViewById(R.id.iv_img);
            iv_chat= (ImageView) itemView.findViewById(R.id.iv_chat);
            iv_call= (ImageView) itemView.findViewById(R.id.iv_call);
            person_name= (TextView) itemView.findViewById(R.id.person_name);
            trend_title= (TextView) itemView.findViewById(R.id.trend_title);
            tv_address= (TextView) itemView.findViewById(R.id.tv_address);
            trend_content= (TagFlowLayout) itemView.findViewById(R.id.tv_tag);
            time= (TextView) itemView.findViewById(R.id.tv_time);
        }
    }
}