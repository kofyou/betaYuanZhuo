package com.fht.yuanzhuo.Utils;

import android.content.Context;

/**
 * Created by asus on 2018/10/14.
 */

public class DensityUtils {
    /**
     * 根据手机分辨率从dip的单位转成px（像素）
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }

    /**
     * 根据手机分辨率从px转成dp
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue/scale + 0.5f);
    }
}
