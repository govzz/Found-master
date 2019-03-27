package com.share.found.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.share.found.base.HomEvent;
import com.share.found.R;
import com.share.found.adapter.ImgAdapter;
import com.share.found.base.BaseActivity;
import com.share.found.bean.LostAndFound;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;


public class MySendActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swiperefreshlayout;
    private RecyclerView recyclerview;
    private ImgAdapter adapter;
    private List<LostAndFound> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_send);
        onSetTitle("My release");
        init();
        getdata();
    }

    private void init() {
        swiperefreshlayout = (SwipeRefreshLayout)findViewById(R.id.swiperefreshlayout);
        recyclerview = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        adapter = new ImgAdapter(this,list);
        recyclerview.setAdapter(adapter);
        swiperefreshlayout.setOnRefreshListener(this);
        adapter.setOnItemClickListener(new ImgAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LostAndFound data = list.get(position);
                Intent intent = new Intent();
                intent.putExtra("data",data.getObjectId());
                intent.setClass(MySendActivity.this, TrendDetailActivity.class);
                startActivity(intent);
            }
        });
        adapter.setOnItemLongClickListener(new ImgAdapter.OnItemLongClickListener() {
            @Override
            public void onItemClick(View view, final int position) {

                new AlertDialog.Builder(MySendActivity.this)
                        .setTitle("Tips")
                        .setMessage("Are you sure to delete this record?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                delete(position);
                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            }
        });
    }

    private void delete(final int position) {
        LostAndFound img =  list.get(position);
        img.delete(img.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    EventBus.getDefault().post(new HomEvent());
                    onToast("Delete successfully");
                    list.remove(position);
                    adapter.notifyDataSetChanged();
                }

            }
        });
    }

    @Override
    public void onRefresh() {
        getdata();
    }
    private void getdata() {
        BmobQuery<LostAndFound> query = new BmobQuery<LostAndFound>();
        query.order("-createdAt");
        query.addWhereEqualTo("user", BmobUser.getCurrentUser());
        query.include("user");
        query.findObjects(new FindListener<LostAndFound>() {

            @Override
            public void done(List<LostAndFound> diaries, BmobException e) {
                if (e == null) {
                    list.clear();
                    for (LostAndFound tr : diaries) {
                        list.add(tr);
                    }
                    swiperefreshlayout.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                } else {
                    swiperefreshlayout.setRefreshing(false);
                }
            }

        });
    }
}
