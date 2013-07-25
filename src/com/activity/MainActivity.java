package com.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.hengtiansoft.cloudcontact.R;
import com.widget.TaskButton;

/**
 * @author retryu E-mail:ruanchenyugood@gmail.com
 * @version create Time��2013-6-10 ����03:53:35 file declare:
 */
public class MainActivity extends CommonActivity implements OnClickListener {

	private TaskButton taskButton;

	@Override
	public void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_main);
		init();
	}

	public void init() {
		taskButton = (TaskButton) findViewById(R.id.TaskButton_Upload);
		taskButton.setOnclick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				taskButton.startAnimation();
			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
