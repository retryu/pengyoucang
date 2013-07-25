package com.widget;

import com.activity.register.RegisterFragment;
import com.hengtiansoft.cloudcontact.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NotifyItem extends RelativeLayout {

	private LayoutInflater layoutInflater;
	private Context context;
	private View view;

	private TextView tvName;
	private TextView tvNumber;
	private String name;
	private RelativeLayout LayoutNotify;

	private int count;

	public NotifyItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context
				.obtainStyledAttributes(attrs, R.styleable.MyItem);
		name = a.getString(R.styleable.MyItem_item_name);
		init(context);

	}

	public void init(Context context) {

		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = (View) layoutInflater.inflate(R.layout.layout_notify_item, null);
		addView(view);
		initWidget();
	}
  
	public void initWidget() {
		tvName = (TextView) view.findViewById(R.id.Tv_Item_Name);
		tvNumber = (TextView) view.findViewById(R.id.Tv_Item_Number);
		LayoutNotify = (RelativeLayout) view.findViewById(R.id.Layout_Notify);
		tvName.setText(name);
		setCount(0);
	}
	 
	public TextView getTvName() {
		return tvName;
	}

	public void setTvName(TextView tvName) {
		this.tvName = tvName;
	}

	public TextView getTvNumber() {
		return tvNumber;
	}

	public void setTvNumber(TextView tvNumber) {
		this.tvNumber = tvNumber;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int c) {
		this.count = c;
		if (count <= 0) {
			LayoutNotify.setVisibility(View.INVISIBLE);
		} else {
			LayoutNotify.setVisibility(View.VISIBLE);
		}
		tvNumber.setText(c+"");

	}

}
