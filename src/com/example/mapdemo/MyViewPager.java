package com.example.mapdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/*
 * ViewPager代替クラス
 * スワイプによるページ切り替えを抑制できる
 * android.support.v4.view.ViewPagerの代わりに使ってください
 */
public class MyViewPager extends android.support.v4.view.ViewPager {
	boolean isSwipeHold_ = true; // スワイプによるページ切り替えを抑制する

	/*
	 * スワイプによるページ切り替え有効/無効設定
	 */
	public void setSwipeHold(boolean enable) {
		isSwipeHold_ = enable;
	}

	public MyViewPager(Context context) {
		super(context);
	}

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isSwipeHold_)
			return false;
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (isSwipeHold_)
			return false;
		return super.onInterceptTouchEvent(event);
	}
}