package com.activity.guide;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hengtiansoft.cloudcontact.R;
import com.view.SplashView;

/**
 * @author retryu E-mail:ruanchenyugood@gmail.com
 * @version create Time：2013-7-23 下午01:25:51 file declare:
 */
public class GuidePagerAdapter extends PagerAdapter {
	private LayoutInflater layoutInflater;

	List<SplashView> views;
	int imgRs[] = { R.drawable.splash_01, R.drawable.splash_02,
			R.drawable.splash_03 };

	public GuidePagerAdapter(Context context) {
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		views = new ArrayList<SplashView>();
		for (int i = 0; i < 3; i++) {
			SplashView view = new SplashView(context);
			view.setContent(imgRs[i]);
			views.add(view);
		}
	}

	@Override
	public int getCount() {
		if (views != null) {
			return views.size();
		}
		return 0;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	// @Override
	// public Object instantiateItem(ViewGroup container, int position) {
	// // TODO Auto-generated method stub
	// // return super.instantiateItem(container, position);
	//
	// View view = (View) layoutInflater.inflate(R.layout.layout_guide_item,
	// null);
	// ((ViewPager) container).addView(views.get(position), 0);
	// return view;
	// }

	@Override
	public Object instantiateItem(View container, int position) {

		((ViewPager) container).addView(views.get(position), 0);
		return views.get(position);
	}

	public List<SplashView> getViews() {
		return views;
	}

	public void setViews(List<SplashView> views) {
		this.views = views;
	}

	@Override
	public void destroyItem(View collection, int position, Object view) {
		((ViewPager) collection).removeView(views.get(position));
	}

	@Override
	public Parcelable saveState() {
		return super.saveState();
	}

	@Override
	public void finishUpdate(View container) {
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
		super.restoreState(state, loader);
	}

}
