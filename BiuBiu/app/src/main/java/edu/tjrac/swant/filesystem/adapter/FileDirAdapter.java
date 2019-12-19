package edu.tjrac.swant.filesystem.adapter;//package color.measurement.com.from_cp20.manager.file;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import edu.tjrac.swant.baselib.common.interfaze.OnItemClickListener;
import edu.tjrac.swant.wjzx.R;

import java.util.ArrayList;


/**
 * Created by wpc on 2017/6/15.
 */

 public class FileDirAdapter extends RecyclerView.Adapter<FileDirAdapter.FileDirViewHolder> {
    ArrayList<String> rote;
    Context mContext;

    public FileDirAdapter(ArrayList<String> rote, Context context) {
        this.rote = rote;
        mContext = context;
    }

    OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
    @Override
    public FileDirViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FileDirViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_dir_file_explore, null));
    }

    @Override
    public void onBindViewHolder(FileDirViewHolder holder, final int position) {
        holder.dir.setText(rote.get(position));
//        UiUtils.setDrawableRight(mContext, holder.dir, R.drawable.ic_keyboard_arrow_right_black_24dp);
        holder.icon.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
        holder.dir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rote.size();
    }

    class FileDirViewHolder extends RecyclerView.ViewHolder {
        TextView dir;
        ImageView icon;

        public FileDirViewHolder(View itemView) {
            super(itemView);
            dir =itemView.findViewById(R.id.text);
            icon= itemView.findViewById(R.id.icon);
        }
    }
}