package com.share.found.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.share.found.R;
import com.share.found.bean.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private List<User> users = new ArrayList<>();
    Context context;

    public SearchUserAdapter(Context context, List<User> list) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.users = list;
    }


    /**
     * 获取用户
     *
     * @param position
     * @return
     */
    public User getItem(int position) {
        return users.get(position);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_search_user, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final User user = (User)users.get(position);
        if (user.getAvatar()!=null){
            Glide.with(context)
                    .load(user.getAvatar())
                    .placeholder(R.drawable.defalut_head)
                    .into(holder.mAvatar);
        }else{
            holder.mAvatar.setImageResource(R.drawable.defalut_head);
        }
        holder.mName.setText(user.getUsername());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(view,position);
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.avatar)
        ImageView mAvatar;
        @BindView(R.id.name)
        TextView mName;
        @BindView(R.id.tv_distance)
        TextView mTvDistance;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
    public  interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    private OnItemClickListener mOnItemClickListener = null;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
