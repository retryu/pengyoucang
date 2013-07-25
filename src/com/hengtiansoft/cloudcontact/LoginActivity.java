package com.hengtiansoft.cloudcontact;

import java.sql.SQLException;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import cn.jpush.android.api.JPushInterface;

import com.activity.CommonActivity;
import com.activity.IndexActivity;
import com.activity.login.LoginFragment;
import com.activity.register.RegisterActivity;
import com.activity.register.RegisterFragment;
import com.db.OrmDateBaseHelper;
import com.db.dao.interfaze.UserDao;
import com.db.model.Contact;
import com.db.model.User;
import com.http.HttpUtil;
import com.util.ContactUtil;
import com.widget.HoloAlert;

public class LoginActivity extends CommonActivity {

	private FrameLayout contentLayout;
	private FragmentManager fm;
	private FragmentTransaction ft;
	private HoloAlert holoAlert;
	private UserDao userDao;
	private RegisterFragment registerFragment;
	private OrmDateBaseHelper ormDateBaseHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// testQuery();
		ormDateBaseHelper = getCommoApp().getOrmDateBaseHelper();
		userDao = ormDateBaseHelper.getUserDao();
		boolean isExits = userDao.isExits();
		if (isExits == true) {
			User user = null;
			try {
				user = userDao.queryForAll().get(0);
				JPushInterface.resumePush(this);
				JPushInterface.setAliasAndTags(getApplicationContext(),
						user.getUserId(), null);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HttpUtil.setUser(user);
			Intent intent = new Intent(this, IndexActivity.class);
			startActivity(intent);
			finish();
		}

		init();
	}

	public void init() {
		holoAlert = (HoloAlert) findViewById(R.id.HoloAlert);
		contentLayout = (FrameLayout) findViewById(R.id.Layout_Content);
		fm = getSupportFragmentManager();
		ft = fm.beginTransaction();
		Fragment loginFragment = getLoginFragment();
		ft.add(R.id.Layout_Content, loginFragment);
		ft.commit();

	}

	public Fragment getLoginFragment() {
		Fragment fragment = new LoginFragment();
		return fragment;
	}

	/**
	 * unit test query
	 */
	public void testQuery() {
		ContactUtil contactUtil = new ContactUtil(this);
		List<Contact> contacts = contactUtil.query();

	}

	public void showAlert(String alerStr) {
		if (holoAlert.isShow == true) {
			holoAlert.hide();
		} else {
			holoAlert.show(alerStr);
		}
	}

	public void toRegister() {
		// ft = fm.beginTransaction();
		// if (registerFragment == null) {
		// registerFragment = new RegisterFragment();
		// }
		// registerFragment.setLoginActivity(this);
		// registerFragment.setOrmDateBaseHelper(ormDateBaseHelper);
		// ft.setCustomAnimations(R.anim.slide_in_from_bottom,
		// R.anim.animation_null, R.anim.animation_null,
		// R.anim.slide_out_from_bottom);
		// ft.replace(R.id.Layout_Content, registerFragment);
		// // ft.addToBacksStack("1");
		// ft.commit();

		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.animation_null);
	}  

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		// ft = fm.beginTransaction();
		// ft.remove(registerFragment);
		// ft.setCustomAnimations(R.anim.slide_in_from_bottom,
		// R.anim.slide_out_from_bottom);
		// ft.commit();
	}
}
