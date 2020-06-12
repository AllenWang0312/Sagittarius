package edu.tjrac.swant.bluetooth.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;
import edu.tjrac.swant.baselib.util.TimeUtils;
import edu.tjrac.swant.bluetooth.R;
import edu.tjrac.swant.bluetooth.bean.ScanInfo;

/**
 * Created by wpc on 2018/2/2 0002.
 */

public class ScanInfoHistoryAdapter extends BaseQuickAdapter<ScanInfo, BaseViewHolder> {
    public ScanInfoHistoryAdapter(@Nullable List<ScanInfo> data) {
        super(R.layout.item_scan_info_history, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ScanInfo item) {
        helper.setText(R.id.time, TimeUtils.getTimeWithFormat(item.getTagTime(), "hh:MM:ss.mm"));
        helper.setText(R.id.div_time, String.valueOf(item.getTagTime() - item.getLastTagTime()) + "ms");
        helper.setText(R.id.rssi, item.getDbm() + "dBm");
    }
}
