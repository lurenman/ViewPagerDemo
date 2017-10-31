package com.example.lurenman.viewpagerdemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lurenman.viewpagerdemo.R;
import com.example.lurenman.viewpagerdemo.animate.DepthPageTransformer;
import com.example.lurenman.viewpagerdemo.animate.ZoomOutPageTransformer;
import com.example.lurenman.viewpagerdemo.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: baiyang.
 * Created on 2017/10/31.
 * 参考
 * http://blog.csdn.net/javazejian/article/details/52160092
 */

public class StartpageActivity extends Activity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private static final String TAG = "StartpageActivity";
    // / ViewPager的数据
    private List<ImageView> imageViewList;
    private ViewPager mViewPager;
    private TextView tv_start;//开始体验
    // 点的组
    private LinearLayout llPointGroup;
    // 选中的点view对象
    private View mSelectPointView;//要移动的那个小红色view
    // 点之间的宽度
    private int pWidth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);
        initViews();
    }

    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.vp_guide);
        tv_start = (TextView) findViewById(R.id.tv_start);
        llPointGroup = (LinearLayout) findViewById(R.id.ll_guide_point_group);
        mSelectPointView = findViewById(R.id.select_point);
        initDatas();
        GuideAdapter adapter = new GuideAdapter();
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(this);
        // 第一个参数表示是否反序画出图片，true child 倒序，false child 顺序；实际并没有发现什么特别的效果。
       // mViewPager.setPageTransformer(true,new ZoomOutPageTransformer());
        mViewPager.setPageTransformer(true,new DepthPageTransformer());
        tv_start.setOnClickListener(this);
    }

    private void initDatas() {
        int[] imageResIDs = {R.drawable.pager1, R.drawable.pager2,
                R.drawable.pager3};
        imageViewList = new ArrayList<>();

        ImageView iv;// 图片  View view;// 点
        View view;// 点
        LinearLayout.LayoutParams params; // 参数类
        for (int i = 0; i < imageResIDs.length; i++) {
            iv = new ImageView(this);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setBackgroundResource(imageResIDs[i]);
            imageViewList.add(iv);
            // 根据图片的个数, 每循环一次向LinearLayout中添加一个点
            view = new View(this);
            view.setBackgroundResource(R.drawable.point_normal);
            // 设置参数
            params = new LinearLayout.LayoutParams(DensityUtil.dip2px(getApplicationContext(),10), DensityUtil.dip2px(getApplicationContext(),10));
            if (i != 0) {
                params.leftMargin = DensityUtil.dip2px(getApplicationContext(),10);
            }
            view.setLayoutParams(params);// 添加参数
            llPointGroup.addView(view);//这个添加的是底部那个不动的小灰色的圆view
        }
    }
   /* position 表示当前选中的是第几个页面；
      positionOffset 是当前页面滑动比例，其值得范围是[0,1)，如果页面向右翻动，
      这个值不断变大，最后在趋近1的情况后突变为0。如果页面向左翻动，这个值不断变小，最后变为0。
      positionOffsetPixels 是当前页面滑动像素的偏移距离
    */

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.w(TAG, "onPageScrolled position: "+position);
        Log.w(TAG, "onPageScrolled positionOffset: "+positionOffset);
        Log.w(TAG, "onPageScrolled positionOffsetPixels: "+positionOffsetPixels);
        if(pWidth==0) {
            pWidth = llPointGroup.getChildAt(1).getLeft()
                    - llPointGroup.getChildAt(0).getLeft();
        }

        // 获取点要移动的距离
        int leftMargin = (int) (pWidth * (position + positionOffset));
        // 给红点设置参数
        RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) mSelectPointView
                .getLayoutParams();
        params.leftMargin = leftMargin;
        mSelectPointView.setLayoutParams(params);
    }

    @Override
    public void onPageSelected(int position) {
        // 显示体验按钮
        if (position == imageViewList.size() - 1) {
            tv_start.setVisibility(View.VISIBLE);// 显示
        } else {
            tv_start.setVisibility(View.GONE);// 隐藏
        }

    }
   /* 当页面滚动状态改变时被回调。有三个值：0（END）,1(PRESS) , 2(UP) 。
      当用手指滑动翻页时，手指按下去的时候会触发这个方法，state值为1，手指抬起时，如果发生了滑动（即使很小）
      其值会变为2，最后变为0 。总共执行这个方法三次。当当前页面翻页时，会执行这个方法两次，state值分别为2 , 0
    */

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        if (view == tv_start) {
            Toast.makeText(getApplicationContext(), "相应事件", Toast.LENGTH_SHORT).show();
        }
    }

    private class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /*
       * 删除元素
       */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = imageViewList.get(position);
            container.addView(iv);// 1. 向ViewPager中添加一个view对象
            return iv; // 2. 返回当前添加的view对象
        }
    }
}
