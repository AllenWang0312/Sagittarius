package edu.tjrac.swant.filesystem.adapter;

import androidx.annotation.Nullable;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import edu.tjrac.swant.wjzx.R;

import java.util.List;

/**
 * Created by wpc on 2018/1/15 0015.
 */

public class PathRecycAdapter extends BaseQuickAdapter<String, BaseViewHolder> {



    public PathRecycAdapter(@Nullable List<String> data) {
        super(R.layout.item_path, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, String item) {
        TextView view=helper.getView(R.id.tv_path_item);
        view.setText(item);
//        helper.setText(R.id.tv_path_item, item);
//        if(getItemViewType(helper.getPosition())==1){
//            view.setBackground(mContext.getResources().getDrawable(R.drawable.bg_black_radius));
//            Drawable drawable=mContext.getResources().getDrawable(R.drawable.delete);
//            view.setCompoundDrawables(null,null,drawable,null);
//        }

    }
}
