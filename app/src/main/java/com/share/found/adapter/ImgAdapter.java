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
import com.share.found.bean.LostAndFound;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;


public class ImgAdapter extends RecyclerView.Adapter<ImgAdapter.ViewHolder> {
    private final LayoutInflater mInflater;
    public Context context;
    public List<LostAndFound> shopList;
    public ImgAdapter(Context context, List<LostAndFound> shopList) {
        this.context = context;
        this.shopList = shopList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_img, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        LostAndFound img = shopList.get(position);
        holder.tv_name.setText(img.getUser().getUsername());
        holder.tv_content.setText(img.getTitle()+"----"+img.getTime());
        List<String> list = img.getTag();

        holder.tv_tag.setAdapter(new TagAdapter<String>(list) {
            @Override
            public View getView(FlowLayout parent, int position, String o) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv,
                        holder.tv_tag, false);
                tv.setText(o);
                return tv;
            }
        });
        holder.tv_time.setText(img.getCreatedAt());
        if (img.getUser().getAvatar()==null){
            holder.profile_image.setImageResource(R.drawable.defalut_head);
        }else {
            Glide.with(context).load(img.getUser().getAvatar()).error(R.drawable.defalut_head).into(holder.profile_image);
        }
        if (img.getFile()==null){
            holder.profile_image.setImageResource(R.drawable.defalut_head);
        }else {
            Glide.with(context).load(img.getFile().getUrl()).error(R.drawable.icon_fail).into(holder.iv_img);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(view,position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mOnItemLongClickListener!=null){
                    mOnItemLongClickListener.onItemClick(view,position);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_img ;
        private ImageView profile_image ;
        private TextView tv_name ;
        private TextView tv_content ;
        private TextView tv_time ;
        private TagFlowLayout tv_tag ;

        public ViewHolder(View view) {
            super(view);
            iv_img = (ImageView) view.findViewById(R.id.iv_img);
            profile_image = (ImageView) view.findViewById(R.id.profile_image);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_tag = (TagFlowLayout) view.findViewById(R.id.tv_tag);
        }
    }
    public  interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    private OnItemClickListener mOnItemClickListener = null;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    public  interface OnItemLongClickListener {
        void onItemClick(View view, int position);
    }
    private OnItemLongClickListener mOnItemLongClickListener = null;
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }
}
