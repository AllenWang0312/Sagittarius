package edu.tjrac.swant.filesystem.adapter;//package color.measurement.com.from_cp20.manager.file;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import edu.tjrac.swant.baselib.common.interfaze.OnItemClickListener;
import edu.tjrac.swant.baselib.common.interfaze.OnItemLongClickListener;
import edu.tjrac.swant.wjzx.R;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by wpc on 2017/6/15.
 */

public class FileExploreAdapter extends RecyclerView.Adapter<FileExploreAdapter.FileViewHolder> {
    Context mContext;
    String path;
    File file;
    String[] childs;

    public boolean isMulti_selcet() {
        return multi_selcet;
    }

    public void setMulti_selcet(boolean multi_selcet) {
        this.multi_selcet = multi_selcet;
        select_index=new ArrayList<>();
        notifyDataSetChanged();
    }
    public boolean multi_selcet;

    public ArrayList<Integer> getSelect_index() {
        return select_index;
    }

    public void setSelect_index(ArrayList<Integer> select_index) {
        this.select_index = select_index;
    }

    ArrayList<Integer> select_index;

    OnItemClickListener mOnItemClick;

    OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    public FileExploreAdapter(Context context, String path, boolean multi_selcet) {
        mContext = context;
        this.multi_selcet = multi_selcet;
//        select_index=new ArrayList<>();
        this.path = path;
        file = new File(path);
        childs = file.list();
    }

    public void setOnItemClick(OnItemClickListener onItemClick) {
        mOnItemClick = onItemClick;
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FileViewHolder(LayoutInflater.from(mContext).inflate(R.layout.file_item, parent, false));
    }

    @Override
    public void onBindViewHolder(FileViewHolder holder, final int position) {
        String filename = childs[position];
        File f = new File(path, filename);
        if (f.isDirectory()) {
            Glide.with(mContext).load(R.mipmap.small_ico_folder).into(holder.icon);
//            holder.icon.setImageResource(R.mipmap.small_ico_folder);
        } else {
            if (filename.endsWith(".pdf")) {
                Glide.with(mContext).load(R.mipmap.small_ico_dps).into(holder.icon);
//                holder.icon.setImageResource(R.mipmap.small_ico_dps);
            } else if (filename.endsWith(".doc")) {
                Glide.with(mContext).load(R.mipmap.small_ico_doc).into(holder.icon);
//                holder.icon.setImageResource(R.mipmap.small_ico_doc);
            } else if (filename.endsWith(".apk")) {
                Glide.with(mContext).load(R.mipmap.small_ico_apk).into(holder.icon);
//                holder.icon.setImageResource(R.mipmap.small_ico_apk);
            } else if (filename.endsWith(".avi")) {
            } else if (filename.endsWith(".xls")) {
                Glide.with(mContext).load(R.mipmap.small_ico_document).into(holder.icon);
//                holder.icon.setImageResource(R.mipmap.small_ico_document);
            } else if (filename.endsWith(".xml")) {
                Glide.with(mContext).load(R.mipmap.small_ico_c).into(holder.icon);
//                holder.icon.setImageResource(R.mipmap.small_ico_c);
            }else if(filename.endsWith(".zip")){
                Glide.with(mContext).load(R.mipmap.small_ico_ace).into(holder.icon);
//                holder.icon.setImageResource(R.mipmap.small_ico_ace);
            }
            else {
                Glide.with(mContext).load(R.mipmap.small_ico_chm).into(holder.icon);
//                holder.icon.setImageResource(R.mipmap.small_ico_chm);
            }
        }
        holder.name.setText(childs[position]);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(multi_selcet){
                    if(select_index.contains(position)){
                        select_index.remove((Integer)position);
                    }else {
                        select_index.add(position);
                    }
                    notifyDataSetChanged();
                }else {
                    mOnItemClick.onItemClick(v, position);
                }

            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mOnItemLongClickListener.onItemLongClick(v,position);
                return true;
            }
        });
        if(multi_selcet){
            holder.cb.setVisibility(View.VISIBLE);
            if(select_index.contains(position)){
                holder.cb.setChecked(true);
            }else {
                holder.cb.setChecked(false);
            }
        }else {
            holder.cb.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return childs.length;
    }

    public String getPath() {
        return path;
    }

    public String[] getChilds() {
        return childs;
    }

    class FileViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;
        CheckBox cb;

        public FileViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.iv_file_icon);
            name = (TextView) itemView.findViewById(R.id.tv_file_name);
            cb=(CheckBox)itemView.findViewById(R.id.cb_file_checked);
        }
    }
}