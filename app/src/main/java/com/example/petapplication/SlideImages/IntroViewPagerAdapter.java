package com.example.petapplication.SlideImages;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.petapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class IntroViewPagerAdapter extends PagerAdapter {

    private Context mContext ;
    private List<ScreenItem> mListScreen;

    public IntroViewPagerAdapter(Context mContext, List<ScreenItem> mListScreen) {
        this.mContext = mContext;
        this.mListScreen = mListScreen;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.layout_screen,null);
        ImageView imgSlide = layoutScreen.findViewById(R.id.intro_img);
        ScreenItem screenItem = mListScreen.get(position);
        Picasso.get()
                .load(screenItem.getScreenImg())
                .resize(800,500)
                .centerCrop()
                .into(imgSlide);
        container.addView(layoutScreen);
        return layoutScreen;
    }
    @Override
    public int getCount() {
        return mListScreen.size();
    }

    @Override
    public boolean isViewFromObject( View view,  Object o) {
        return view == o;
    }

    @Override
    public void destroyItem( ViewGroup container, int position,  Object object) {
        container.removeView((View)object);
    }
}