package edu.tjrac.swant.todo.adapter;

import android.support.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import edu.tjrac.swant.todo.bean.WebInfo;
import edu.tjrac.swant.wjzx.R;

import java.util.List;

/**
 * Created by wpc on 2018/5/15.
 */

public class WebWorkSpaceAdapter extends BaseQuickAdapter<WebInfo,BaseViewHolder> {
    public WebWorkSpaceAdapter(@Nullable List<WebInfo> data) {
        super(R.layout.item_web_workspace, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WebInfo item) {
        helper.setText(R.id.tv_title,item.getTitle());

    }
}
