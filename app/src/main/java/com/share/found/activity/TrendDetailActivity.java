package com.share.found.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;


import com.share.found.R;
import com.share.found.adapter.CommentAdapter;
import com.share.found.base.BaseActivity;
import com.share.found.bean.Comment;
import com.share.found.bean.LostAndFound;
import com.share.found.bean.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;



public class TrendDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private String userId;
    private SwipeRefreshLayout swiperefreshlayout;
    private RecyclerView recyclerview;
    private List<Comment> list = new ArrayList<>();
    private CommentAdapter adapter;
    private EditText etComment;
    private String travelId;
    private LostAndFound img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend);
        onSetTitle("详情");
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        userId = sp.getString("userId","");
        init();
        getDetail();
    }


    private void getDetail() {
        BmobQuery<LostAndFound> query = new BmobQuery<LostAndFound>();
        query.include("user");
        query.addWhereEqualTo("objectId",travelId);
        query.findObjects(new FindListener<LostAndFound>() {

            @Override
            public void done(List<LostAndFound> diaries, BmobException e) {
                if (e == null) {
                    list.clear();
                    for (LostAndFound tr : diaries) {
                        img = tr;
                        adapter = new CommentAdapter(TrendDetailActivity.this,img,list);
                        recyclerview.setAdapter(adapter);
                        getCommentList();
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    swiperefreshlayout.setRefreshing(false);
                }
            }

        });

    }
    private void init() {
        travelId =  getIntent().getStringExtra("data");
        swiperefreshlayout = (SwipeRefreshLayout)findViewById(R.id.swiperefreshlayout);
        recyclerview = (RecyclerView)findViewById(R.id.recyclerview);
        etComment = (EditText)findViewById(R.id.et_comment);
        View headView = getLayoutInflater().inflate(R.layout.item_trend, null);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        swiperefreshlayout.setOnRefreshListener(this);
        findViewById(R.id.bt_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment();
            }
        });
    }

    @Override
    public void onRefresh() {
        getCommentList();
    }

    private void getCommentList() {
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.order("-createdAt");
        query.include("user");
        query.addWhereEqualTo("travelId",travelId);
        query.findObjects(new FindListener<Comment>() {

            @Override
            public void done(List<Comment> diaries, BmobException e) {
                if (e == null) {
                    list.clear();
                    for (Comment tr : diaries) {
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
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void comment(){
        String text = etComment.getText().toString();
        Comment comment = new Comment();
        comment.setContent(text);
        comment.setTravelId(travelId);
        comment.setUser(BmobUser.getCurrentUser(User.class));
        comment.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
                    hideSoftInputView();
                    getCommentList();
                    etComment.setText("");
                    etComment.clearFocus();
                    Toast.makeText(TrendDetailActivity.this,"评论成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(TrendDetailActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
