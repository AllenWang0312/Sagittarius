package edu.tjrac.swant.todo.adapter;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import edu.tjrac.swant.todo.bean.WebInfo;
import edu.tjrac.swant.wjzx.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wpc on 2018/5/16.
 */

public class WebWorkGroupAdapter extends BaseQuickAdapter<WebWorkGroupAdapter.WebWorkGroupInfo,BaseViewHolder>{


    public WebWorkGroupAdapter(@Nullable List<WebWorkGroupInfo> data) {
        super(R.layout.item_web_work_group, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WebWorkGroupInfo item) {
       RecyclerView recycler=helper.getView(R.id.recycler);
        WebWorkSpaceAdapter adapter=new WebWorkSpaceAdapter(item.infos);
        recycler.setLayoutManager(new GridLayoutManager(mContext,2,GridLayoutManager.HORIZONTAL,false));
       recycler.setAdapter(adapter);

       helper.setText(R.id.tv_title,item.title)
       .addOnClickListener(R.id.iv_options);

    }

  public static class WebWorkGroupInfo implements Parcelable{
        String title;
        long id;
        long createAt;

        ArrayList<WebInfo> infos;

      public String getTitle() {
          return title;
      }

      public void setTitle(String title) {
          this.title = title;
      }

      public long getId() {
          return id;
      }

      public void setId(long id) {
          this.id = id;
      }

      public long getCreateAt() {
          return createAt;
      }

      public void setCreateAt(long createAt) {
          this.createAt = createAt;
      }

      public ArrayList<WebInfo> getInfos() {
          return infos;
      }

      public void setInfos(ArrayList<WebInfo> infos) {
          this.infos = infos;
      }

      public WebWorkGroupInfo(String title, ArrayList<WebInfo> infos) {
          this.title = title;
          this.infos = infos;
      }

      protected WebWorkGroupInfo(Parcel in) {
          title = in.readString();
          id = in.readLong();
          createAt = in.readLong();
          infos = in.createTypedArrayList(WebInfo.CREATOR);
      }

      public static final Creator<WebWorkGroupInfo> CREATOR = new Creator<WebWorkGroupInfo>() {
          @Override
          public WebWorkGroupInfo createFromParcel(Parcel in) {
              return new WebWorkGroupInfo(in);
          }

          @Override
          public WebWorkGroupInfo[] newArray(int size) {
              return new WebWorkGroupInfo[size];
          }
      };

      @Override
      public int describeContents() {
          return 0;
      }

      @Override
      public void writeToParcel(Parcel dest, int flags) {
          dest.writeString(title);
          dest.writeLong(id);
          dest.writeLong(createAt);
          dest.writeTypedList(infos);
      }
  }
}
