package io.github.tiagofrbarbosa.fleekard.viewpager;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by tfbarbosa on 21/10/17.
 */

public class CustomViewPager extends ViewPager {

    private boolean enabled;

    public CustomViewPager(Context context) {
        super(context);
        this.enabled = true;
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        return enabled ? super.onTouchEvent(motionEvent) : false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent){
        return enabled ? super.onInterceptTouchEvent(motionEvent) : false;
    }

    public void setSwipe(boolean enabled){
        this.enabled = enabled;
    }
}
