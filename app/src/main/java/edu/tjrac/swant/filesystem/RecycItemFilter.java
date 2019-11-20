package edu.tjrac.swant.filesystem;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wpc on 2018/4/20.
 */

public abstract class RecycItemFilter<T extends Object> {

    RecyclerView mRecyclerView;
    List<T> mDatas;

    public RecycItemFilter(RecyclerView recyclerView, List<T> datas) {
        mRecyclerView = recyclerView;
        mDatas = datas;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public void setDatas(ArrayList<T> datas) {
        mDatas = datas;
    }

    int index=-1;
    ArrayList<Integer> finds;

    public void find(String filter) {
        if(finds==null){
            finds=new ArrayList<>();
        }else {
            finds.clear();
        }
        for (int i = 0; i < mDatas.size(); i++) {
            if (equal(mDatas.get(i), filter)) {
                finds.add(i);
            }
        }
    }

    public void first() {
        index = 0;
        mRecyclerView.scrollToPosition(finds.get(index));
    }

    public void skipToNext() {

        if (index == finds.size()) {
            index = 0;
        } else {
            index++;
        }
        mRecyclerView.scrollToPosition(finds.get(index));
    }


    public abstract boolean equal(T data, String filter);
}
