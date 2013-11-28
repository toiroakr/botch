package kom.botch.rasaki;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/*
 * ViewPager代替クラス
 * スワイプによるページ切り替えを抑制できる
 * android.support.v4.view.ViewPagerの代わりに使ってください
 */
/*
 * 左右分けて設定可能
 * 画面の向き変更はどうなってるか不明
 */
public class MyViewPager extends android.support.v4.view.ViewPager {
	private boolean isRSwipeHold = false; // スワイプによる右へのページ切り替えを抑制する
	private boolean isLSwipeHold = false; // スワイプによる左へのページ切り替えを抑制する
	private boolean onHoldingSwipe = false;

	// スワイプによるページ切り替え有効/無効設定
	public void setSwipeHold(boolean enable) {
		isRSwipeHold = enable;
		isLSwipeHold = enable;
	}

	public void setRSwipeHold(boolean enable) {
		isRSwipeHold = enable;
	}

	public void setLSwipeHold(boolean enable) {
		isLSwipeHold = enable;
	}

	public MyViewPager(Context context) {
		super(context);
	}

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// if (isSwipeHold_)
		// return false;
		if (isSwipeHold(event))
			return false;
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (isSwipeHold(event))
			return false;
		return super.onInterceptTouchEvent(event);
	}

	private boolean isSwipeHold(MotionEvent event) {
		boolean _isSwipeHold;
		double swipeArea = getWidth() * 0.25;
		Log.d("TouchEvent", "" + swipeArea);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (event.getX() > this.getWidth() - swipeArea
					|| event.getX() < swipeArea) {
				_isSwipeHold = false;
				if (isRSwipeHold && event.getX() > this.getWidth() - swipeArea)
					_isSwipeHold = true;
				else if (isLSwipeHold && event.getX() < swipeArea)
					_isSwipeHold = true;
			} else {
				_isSwipeHold = true;
			}
			onHoldingSwipe = _isSwipeHold;
			Log.d("TouchEvent", "getAction()" + "ACTION_DOWN | "
					+ onHoldingSwipe);
			break;
		case MotionEvent.ACTION_UP:
			onHoldingSwipe = false;
			return false;
		case MotionEvent.ACTION_MOVE:
			Log.d("TouchEvent", "getAction()" + "MOVE | " + onHoldingSwipe);
			return onHoldingSwipe;
		case MotionEvent.ACTION_CANCEL:
			onHoldingSwipe = false;
			Log.d("TouchEvent", "getAction()" + "ACTION_CANCEL | "
					+ onHoldingSwipe);
			break;
		}
		return onHoldingSwipe;
	}
}