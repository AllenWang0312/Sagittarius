package edu.tjrac.swant.common.image;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.PagerAdapter;

public class CheckPhotoPagerAdapter extends PagerAdapter {

    Context context;
    private List<View> views = new ArrayList<>();
    List<String> urls = new ArrayList<>();

    public CheckPhotoPagerAdapter(Context context, List<String> urls) {
        this.context = context;
        this.urls = urls;
        for (int i = 0; i <urls.size() ; i++) {
            PhotoView image = new PhotoView(context);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image.setTransitionName(urls.get(i));
            }
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

    public View getView(int position){
        return views.get(position);
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
