package com.share.found.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.share.found.R;
import com.share.found.adapter.ImgAdapter;
import com.share.found.base.BaseActivity;
import com.share.found.bean.LostAndFound;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class SearchActivity extends BaseActivity {


    @BindView(R.id.at_title)
    TextView mAtTitle;
    @BindView(R.id.at_toolbar)
    Toolbar mAtToolbar;
    @BindView(R.id.et_write_pwd)
    EditText mEtWritePwd;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerview;
    @BindView(R.id.sw_refresh)
    SwipeRefreshLayout mSwRefresh;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    private SearchActivity context;
    private ArrayList<LostAndFound> list = new ArrayList<LostAndFound>();
    private ImgAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_myserach);
        ButterKnife.bind(this);
        onSetTitle("搜索");
        initView();
    }


    private void initView() {
        mSwRefresh.setEnabled(false);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new ImgAdapter(SearchActivity.this, list);
        recyclerview.setAdapter(adapter);
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setText("搜索");
        adapter.setOnItemClickListener(new ImgAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LostAndFound data = list.get(position);
                Intent intent = new Intent(SearchActivity.this,TrendDetailActivity.class);
                intent.putExtra("data",data.getObjectId());
                startActivity(intent);
            }
        });
    }

    private void getCommentList(final String content) {
        if (TextUtils.isEmpty(content)) {
            onToast("条件为空");
            return;
        }
        BmobQuery<LostAndFound> query = new BmobQuery<>();
        query.order("-createdAt");
        query.include("user");
        query.findObjects(new FindListener<LostAndFound>() {

            @Override
            public void done(List<LostAndFound> imgs, BmobException e) {
                if (e == null) {
                    list.clear();
                    for (LostAndFound tr : imgs) {
                        List<String> tagList = tr.getTag();
                        StringBuffer stringBuffer = new StringBuffer();
                        for (String str:
                                tagList ) {
                            stringBuffer.append(str);
                        }
                        if (tr.getTitle().contains(content)||stringBuffer.toString().contains(content)) {
                            list.add(tr);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                }
            }

        });
    }



    @OnClick(R.id.tv_right)
    public void onClick() {
        String content = mEtWritePwd.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(context, "请输入关键字", Toast.LENGTH_SHORT).show();
            return;
        }
        getCommentList(content);
    }


}
