package edu.tjrac.swant.filesystem.adapter;

import android.widget.ImageView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import edu.tjrac.swant.filesystem.FileSystemHelper;
import edu.tjrac.swant.wjzx.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wpc on 2018/1/12 0012.
 */

public class ClipBoardRecycAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    HashMap<String, Long> cut_paths;
    HashMap<String, Long> copy_paths;

    public ClipBoardRecycAdapter(ArrayList<String> data) {
        super(R.layout.item_clipboard, data);
    }

    public void bindToPathsType(HashMap<String, Long> cut_paths, HashMap<String, Long> copy_paths) {
        this.cut_paths = cut_paths;
        this.copy_paths = copy_paths;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        File file = new File(item);
        ImageView fileImage = helper.getView(R.id.iv_file);
//        ResourcesUtils.getResId(mContext,""
       FileSystemHelper.INSTANCE. loadFileIconToImageView(file, fileImage, mContext);
        helper.setText(R.id.iv_filename, file.getName());
        helper.setChecked(R.id.cb_type, cut_paths.keySet().contains(item));
    }

    @Override
    public void remove(int position) {
        String item = getItem(position);

        if (cut_paths.keySet().contains(item)) {
            cut_paths.remove(item);
        } else {
            copy_paths.remove(item);
        }

        super.remove(position);
    }
}
