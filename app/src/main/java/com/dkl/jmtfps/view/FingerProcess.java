package com.dkl.jmtfps.view;


import com.dkl.jmtfps.R;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android .util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * 闪烁的指纹，用以表示录取进度
 * @author dkl
 * @since 2015-2-25
 */
public class FingerProcess extends FrameLayout {

	public static final int fp_imgs[] = new int[] { R.drawable.fingerprint_00 ,
			R.drawable.fingerprint_00, R.drawable.fingerprint_01,
			R.drawable.fingerprint_02, R.drawable.fingerprint_03,
			R.drawable.fingerprint_04, R.drawable.fingerprint_05,
			R.drawable.fingerprint_06, R.drawable.fingerprint_07,
			R.drawable.fingerprint_08, R.drawable.fingerprint_09,
			R.drawable.fingerprint_10};

	public int mCurrentStep = 0;

	private View view;

	public FingerProcess(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public FingerProcess(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public FingerProcess(Context context) {
		super(context);
		init(context);
	}

	public void init(Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.fingerprocess, this);
		twikcleImg(context,0);
	}

	public void twikcleImg(Context context,int step) {
		ImageView imageViewAbove = (ImageView) view.findViewById(R.id.id_img_above);
		ImageView imageViewBelow = (ImageView) view.findViewById(R.id.id_img_below);
		int imgAboveId;
		int imgBelowId;
		
		if (step < 10) {
			
			imgAboveId = fp_imgs[step+1];
		    imgBelowId = fp_imgs[step];
			imageViewAbove.setImageResource(imgAboveId);
			imageViewBelow.setImageResource(imgBelowId);

			ObjectAnimator oa = ObjectAnimator.ofFloat(imageViewAbove, "alpha",0.0f, 1.0f);
			oa.setInterpolator(new LinearInterpolator());
			oa.setDuration(2500);
			oa.setRepeatCount(0);

			oa.start();
		} else {
			imgAboveId = fp_imgs[11];
		    imgBelowId = fp_imgs[10];
			imageViewAbove.setImageResource(imgAboveId);
			imageViewBelow.setImageResource(imgBelowId);
		}
		

	}
}
