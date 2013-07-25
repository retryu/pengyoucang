package com.widget;

import com.hengtiansoft.cloudcontact.R;
import com.util.UnitsUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author retryu E-mail:ruanchenyugood@gmail.com
 * @version create Time：2013-6-29 下午03:21:19
 */
public class NumberNotify extends RelativeLayout {

	private int number;
	private Context context;
	ImageView imgBg;
	TextView tvNumber;

	public NumberNotify(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public NumberNotify(Context context) {
		super(context);
		init(context);
	}

	public void init(Context con) {
		this.context = con;
		imgBg = new ImageView(context);
		imgBg.setBackgroundResource(R.drawable.notify_bg);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		addView(imgBg, params);

		tvNumber = new TextView(context);
		tvNumber.setText(number + "");
		LayoutParams tvParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		tvParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		tvNumber.setTextColor(getResources().getColor(R.color.white));
		addView(tvNumber, tvParams);
		setNumber(number);
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
		if (number == 0) {
			imgBg.setVisibility(View.GONE);
			tvNumber.setVisibility(View.GONE);
		} else {
			imgBg.setVisibility(View.VISIBLE);
			tvNumber.setVisibility(View.VISIBLE);
			tvNumber.setText("" + number);
		}
	}

}
