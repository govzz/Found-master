package com.share.found.adapter.base;

import java.util.List;

/**
 * Created by CQU_20161586 ZhaoYimin on 2019/3/20.
 */
public interface IMutlipleItem<T> {

    /**
     * 多种布局的layout文件
     * @param viewtype
     * @return
     */
    int getItemLayoutId(int viewtype);

    /**
     * 多种布局类型
     * @param postion
     * @param t
     * @return
     */
    int getItemViewType(int postion, T t);

    /**
     * 返回布局个数
     * @param list
     * @return
     */
    int getItemCount(List<T> list);
}
