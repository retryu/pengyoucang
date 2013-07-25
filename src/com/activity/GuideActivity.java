package com.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.activity.guide.GuidePagerAdapter;
import com.hengtiansoft.cloudcontact.R;
import com.umeng.common.Log;

/**
 * @author retryu E-mail:ruanchenyugood@gmail.com
 * @version create Time：2013-7-23 下午01:06:44 file declare:
 */
public class GuideActivity extends CommonActivity {
	private ViewPager viewPager;

	@Override
	public void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.splash_activity);
		initWidget();
	}

	public void initWidget() {
		viewPager = (ViewPager) findViewById(R.id.ViewPaper);
		GuidePagerAdapter guidePagerAdapter = new GuidePagerAdapter(this);
		viewPager.setAdapter(guidePagerAdapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int index) {
				// TODO Auto-generated method stub
				Log.e("debug", ""+index);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}
}
