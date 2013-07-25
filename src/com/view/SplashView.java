package com.view;

import java.net.ContentHandler;

import com.hengtiansoft.cloudcontact.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * @author retryu E-mail:ruanchenyugood@gmail.com
 * @version create Time：2013-7-23 下午02:16:43 file declare:
 */
public class SplashView extends RelativeLayout {

	private LayoutInflater layoutInflater;
	private ImageView imageView;
	private View view;

	public SplashView(Context context) {
		super(context);
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = (View) layoutInflater.inflate(R.layout.layout_guide_item, null);
		addView(view);
		initWidget();
	}

	public void initWidget() {
		imageView = (ImageView) view.findViewById(R.id.imageView);
	}

	public void setContent(int resId) {
		imageView.setImageResource(resId);
	}

}
