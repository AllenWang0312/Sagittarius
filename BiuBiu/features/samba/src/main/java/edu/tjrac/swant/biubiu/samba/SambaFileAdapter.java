package edu.tjrac.swant.biubiu.samba;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import jcifs.smb.SmbFile;

public class SambaFileAdapter extends BaseQuickAdapter<SmbFile, BaseViewHolder> {

    public SambaFileAdapter(@Nullable List<SmbFile> data) {
        super(R.layout.item_samba_file, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SmbFile item) {
        helper.setText(R.id.tv_name,item.getName()).setText(R.id.tv_subs,item.getPath());
    }
}
