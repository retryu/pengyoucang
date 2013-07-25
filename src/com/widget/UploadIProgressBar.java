package com.widget;

import com.hengtiansoft.cloudcontact.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class UploadIProgressBar extends RelativeLayout {

	private Context c;

	private ImageView imgProgess;
	private ImageView imgRight;
	private ImageView imgLeft;
	private int width;

	public UploadIProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		c = context;
		initWidget();
	}

	public void initWidget() {

		setBg();
		initProgress();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = getMeasuredWidth();
	}

	public void setBg() {
		ImageView img = new ImageView(c);
		img.setBackgroundResource(R.drawable.progressbar_bg);
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		addView(img, lp);

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
	}

	public void initProgress() {
		LayoutParams leftLp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		imgLeft = new ImageView(c);
		imgLeft.setBackgroundResource(R.drawable.progressbar_left);
		leftLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		addView(imgLeft, leftLp);
		
		
		
		LayoutParams midelp = new LayoutParams(70, LayoutParams.WRAP_CONTENT);
		midelp.setMargins(10, 0, 0, 0);
		imgProgess = new ImageView(c);

		imgProgess.setBackgroundResource(R.drawable.progressbar_mid);
		midelp.addRule(RelativeLayout.RIGHT_OF, imgLeft.getId());
		addView(imgProgess, midelp);

		LayoutParams rightLp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		imgRight = new ImageView(c);
		imgRight.setBackgroundResource(R.drawable.progressba_right);
		rightLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		addView(imgRight, rightLp);

		imgLeft.setVisibility(View.INVISIBLE);
		imgProgess.setVisibility(View.INVISIBLE);
		imgRight.setVisibility(View.INVISIBLE);

	}

	public void setPorcess(int process) {
		if (process < 6) {
			imgLeft.setVisibility(View.VISIBLE);
			imgRight.setVisibility(View.INVISIBLE);
			imgProgess.setVisibility(View.INVISIBLE);
		} else if (process < 94) {
			imgLeft.setVisibility(View.VISIBLE);
			float pre = (float) (process / 100f) * (width - 20);
			android.view.ViewGroup.LayoutParams lp = imgProgess
					.getLayoutParams();
			lp.width = (int) pre;
			imgProgess.setLayoutParams(lp);
			if (imgProgess.getVisibility() == View.INVISIBLE) {
				imgProgess.setVisibility(View.VISIBLE);
			}
			imgRight.setVisibility(View.INVISIBLE);
		} else {
			android.view.ViewGroup.LayoutParams lp = imgProgess
					.getLayoutParams();
			lp.width = width - 15;
			imgProgess.setLayoutParams(lp);
			imgRight.setVisibility(View.VISIBLE);
			imgProgess.setVisibility(View.VISIBLE);
		}
	}
}
