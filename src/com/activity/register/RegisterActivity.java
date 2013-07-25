package com.activity.register;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.activity.CommonActivity;
import com.hengtiansoft.cloudcontact.R;

/**
 * @author retryu E-mail:ruanchenyugood@gmail.com
 * @version create Time：2013-7-24 下午12:27:47 file declare:
 */
public class RegisterActivity extends CommonActivity {
	private FragmentManager fm;
	private FragmentTransaction ft;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_register);
		initWidget();
	}

	public void initWidget() {
		FrameLayout contentLayout = (FrameLayout) findViewById(R.id.Layout_Content);
		fm = getSupportFragmentManager();
		ft = fm.beginTransaction();
		RegisterFragment registerFragemnt = new RegisterFragment();
		registerFragemnt.setLoginActivity(this);
		ft.add(R.id.Layout_Content, registerFragemnt);
		ft.commit();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(0, R.anim.slide_out_from_bottom);
		finish();

	}
}
