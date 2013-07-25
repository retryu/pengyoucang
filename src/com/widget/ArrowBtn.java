package com.widget;

import com.hengtiansoft.cloudcontact.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author retryu E-mail:ruanchenyugood@gmail.com
 * @version create Time：2013-6-29 下午03:59:20 file declare:
 */
public class ArrowBtn extends RelativeLayout {

	private Context context;
	private  TextView  tvTitle;
	private  NumberNotify  numberNotify;
	

	public ArrowBtn(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ArrowBtn(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void init(Context con) {
		context = con;
		setBackgroundResource(R.color.edit_bg);
	}

}
