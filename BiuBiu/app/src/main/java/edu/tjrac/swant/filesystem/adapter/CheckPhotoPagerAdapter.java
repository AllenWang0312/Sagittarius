package edu.tjrac.swant.filesystem.adapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 22320 on 2017/12/14.
 */

public class CheckPhotoPagerAdapter extends PagerAdapter {

    Context context;
    private List<View> views = new ArrayList<>();
    List<String> urls = new ArrayList<>();

    public CheckPhotoPagerAdapter(Context context, List<String> urls) {
        this.context = context;
        this.urls = urls;
        for (int i = 0; i <urls.size() ; i++) {
            ImageView image = new ImageView(context);
            image.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            Glide.with(context).load(urls.get(i)).into(image);
            views.add(image);
        }
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = views.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        object=null;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void addAll(List<View> dd) {
        views.addAll(dd);
        notifyDataSetChanged();
    }

    public void clear() {
        views.clear();
        notifyDataSetChanged();
    }

    public void remove(int poi) {
        views.remove(poi);
        notifyDataSetChanged();
    }


}
