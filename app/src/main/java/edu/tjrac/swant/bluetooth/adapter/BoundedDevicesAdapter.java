package edu.tjrac.swant.bluetooth.adapter;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import edu.tjrac.swant.wjzx.R;


/**
 * Created by wpc on 2018/1/26 0026.
 */

public class BoundedDevicesAdapter extends BaseQuickAdapter<BluetoothDevice, BaseViewHolder> {
    public BoundedDevicesAdapter(@Nullable List<BluetoothDevice> data) {
        super(R.layout.item_bond_device, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BluetoothDevice item) {
        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_address,  item.getAddress());
        helper.setText(R.id.tv_bound_state,item.getBondState()==BluetoothDevice.BOND_BONDED? "BONDED":"NOT BONDED");
//        helper.setText(R.id.tv_type, "type:" + item.getType() + "");
    }
}
