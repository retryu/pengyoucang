package com.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hengtiansoft.cloudcontact.R;

public class TaskButton extends RelativeLayout implements OnClickListener {

	private Context context;
	private LayoutInflater layoutInflater;
	private View view;

	private UploadIProgressBar uploadIProgressBar;
	private TextView tvState;
	private ImageButton imgButton;
	private UiHandler uiHandler;
	private final int MSG_UPDATE_PROCESS = 1;

	private final int MSG_ANIM_START = 2;
	private final int MSG_ANIM_END = 3;
	private int imgBg;
	private int imgBgSelected;
	private String btnName;
	private boolean runing;

	public TaskButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.MyButton);

		imgBg = a.getResourceId(R.styleable.MyButton_btn_bg, -1);
		imgBgSelected = a.getResourceId(R.styleable.MyButton_btn_selected_bg,
				-1);
		btnName = a.getString(R.styleable.MyButton_btn_name);
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		init();
	}

	public void init() {
		view = layoutInflater.inflate(R.layout.layout_task_button, null);
		uploadIProgressBar = (UploadIProgressBar) view
				.findViewById(R.id.upload);
		uploadIProgressBar.setVisibility(View.INVISIBLE);
		tvState = (TextView) view.findViewById(R.id.Tv_State);
		tvState.setText(btnName);
		imgButton = (ImageButton) view.findViewById(R.id.IMGBTN_Upload);
		if (imgBg != -1) {
			imgButton.setBackgroundResource(imgBg);
		}
		uiHandler = new UiHandler();
		addView(view);
	}

	public void startAnimation() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				startProcess();
				for (int i = 0; i < 100; i++) {
					if (runing == true) {
						setProcess(i);
					} else {
						endProcess();
						break;
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				// Message msg = new Message();
				// msg.what = MSG_ANIM_END;
				// uiHandler.sendMessage(msg);

			}

		}.start();
	}

	public void startProcess() {
		Message msgStart = new Message();
		msgStart.what = MSG_ANIM_START;
		uiHandler.sendMessage(msgStart);
		setRuning(true);
	}

	public void setProcess(int i) {
		Message msg = new Message();
		msg.what = MSG_UPDATE_PROCESS;
		msg.obj = 1 * i;
		uiHandler.sendMessage(msg);
	}

	public void endProcess() {
		Message msgEnd = new Message();
		msgEnd.what = MSG_ANIM_END;
		uiHandler.sendMessage(msgEnd);
	}

	class UiHandler extends Handler {
		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			int what = msg.what;
			switch (what) {
			case MSG_UPDATE_PROCESS:
				int process = (Integer) msg.obj;
				uploadIProgressBar.setPorcess(process);
				tvState.setVisibility(View.INVISIBLE);
				uploadIProgressBar.setVisibility(View.VISIBLE);
				break;

			case MSG_ANIM_START:
				tvState.setVisibility(View.INVISIBLE);
				uploadIProgressBar.setVisibility(View.VISIBLE);
				imgButton.setBackgroundResource(imgBgSelected);
				break;

			case MSG_ANIM_END:
				setRuning(false);
				imgButton.setBackgroundResource(imgBg);
				uploadIProgressBar.setVisibility(View.INVISIBLE);
				tvState.setVisibility(View.VISIBLE);

				break;
			}
		}
	}

	public void finish() {
		setRuning(false);
		imgButton.setBackgroundResource(imgBg);
		uploadIProgressBar.setVisibility(View.INVISIBLE);
		tvState.setVisibility(View.VISIBLE);

	}

	public boolean isRuning() {
		return runing;
	}

	public void setRuning(boolean runing) {
		this.runing = runing;
	}

	public void setOnclick(OnClickListener listener) {
		imgButton.setOnClickListener(listener);
//		this.setOnClickListener(listener);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.e("debug", "onclick");
	}

}
