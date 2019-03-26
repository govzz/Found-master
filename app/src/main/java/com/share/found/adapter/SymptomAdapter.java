package com.share.found.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.share.found.R;
import com.share.found.bean.Symptom;

import java.util.List;


public class SymptomAdapter extends RecyclerView.Adapter<SymptomAdapter.ViewHolder> {
    private final LayoutInflater mInflater;
    public Context context;
    public List<Symptom> shopList;
    public SymptomAdapter(Context context, List<Symptom> shopList) {
        this.context = context;
        this.shopList = shopList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_symptom, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Symptom symptom = shopList.get(position);
        holder.tv_time.setText(symptom.getType());

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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_time ;

        public ViewHolder(View view) {
            super(view);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
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
