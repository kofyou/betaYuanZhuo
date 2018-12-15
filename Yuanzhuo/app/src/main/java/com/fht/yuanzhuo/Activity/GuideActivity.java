package com.fht.yuanzhuo.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.fht.yuanzhuo.R;
import com.fht.yuanzhuo.Utils.CacheUtils;
import com.fht.yuanzhuo.Utils.DensityUtils;

import java.util.ArrayList;

public class GuideActivity extends Activity{

    private static final String TAG = GuideActivity.class.getSimpleName();
    private ViewPager viewPager;
    private Button btn_start_main;
    private LinearLayout ll_point_group;
    private ImageView iv_red_point;
    private int leftmax;
    private int widthdpi;

    private ArrayList<ImageView> imageViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        viewPager= findViewById(R.id.viewpager);
        btn_start_main = findViewById(R.id.btn_start_main);
        ll_point_group = findViewById(R.id.ll_point_group);
        iv_red_point = findViewById(R.id.iv_red_point);

        int[] ids = new int[]{
                R.drawable.guide1,
                R.drawable.guide2,
                R.drawable.guide3,
        };

        widthdpi = DensityUtils.dip2px(this,10);

        for (int i= 0;i<ids.length;i++){
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(ids[i]);
            imageViews.add(imageView);

            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.point_normal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthdpi,widthdpi);
            if(i != 0){
                params.leftMargin = widthdpi;
            }
            point.setLayoutParams(params);
            ll_point_group.addView(point);
        }

        viewPager.setAdapter(new MyAdapter());

        iv_red_point.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());
        //得到屏幕滑动百分比
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());

        btn_start_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CacheUtils.putBoolean(GuideActivity.this,SplashActivity.START_MAIN,true);

                Intent intent = new Intent(GuideActivity.this,LoginActivity.class);
                startActivity(intent);

                finish();
            }
        });


    }



    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            Log.e(TAG,"position==" + position + ",positionOffset==" + positionOffset + ",positionOffsetPixels=="+positionOffsetPixels);

            int leftmargin = (int)(position * leftmax +( positionOffset * leftmax));

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_red_point.getLayoutParams();
            params.leftMargin = leftmargin;
            iv_red_point.setLayoutParams(params);
        }

        @Override
        public void onPageSelected(int i) {
            if(i == imageViews.size() -1 ){
                btn_start_main.setVisibility(View.VISIBLE);
            }else {
                btn_start_main.setVisibility(View.GONE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

    class  MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener{

        @Override
        public void onGlobalLayout() {
            iv_red_point.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            leftmax = ll_point_group.getChildAt(1).getLeft() - ll_point_group.getChildAt(0).getLeft();
        }
    }

    class MyAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViews.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position,Object object) {
            container.removeView((View)object);
        }
    }
}
