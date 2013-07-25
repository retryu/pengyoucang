package com.widget;

import cn.jpush.android.c.r;

import com.hengtiansoft.cloudcontact.R;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

/**
 * @author retryu E-mail:ruanchenyugood@gmail.com
 * @version create Time：2013-7-8 下午01:30:19 file declare:
 */
public class LodingAlert extends Dialog {
	private TextView tvLoading;

	public LodingAlert(Context context) {
		super(context);
		setContentView(R.layout.layout_alert_loding);
		setCancelable(false);
		setTitle(context.getResources().getString(R.string.please_wait));
		initWidget();
	}

	public void initWidget() {
		tvLoading = (TextView) findViewById(R.id.Tv_Loding);
	}

	public void setContent(String content) {
		if (content != null) {
			tvLoading.setText(content);
		}
	}
}
