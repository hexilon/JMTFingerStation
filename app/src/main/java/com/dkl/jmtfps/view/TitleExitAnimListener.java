package com.dkl.jmtfps.view;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.TextView;

public class TitleExitAnimListener implements AnimationListener {

	TextView mView;
	int mVisible;

	public TitleExitAnimListener(TextView view, int visible) {
		mView = view;
		mVisible = visible;
	}

	@Override
	public void onAnimationEnd(Animation arg0) {

		if (null != mView) {
			mView.setVisibility(mVisible);
		}
	}

	@Override
	public void onAnimationRepeat(Animation arg0) {
	}

	@Override
	public void onAnimationStart(Animation arg0) {
	}

}
