package com.ht.cloudcontact;

import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.SQLException;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.activity.CommonActivity;
import com.activity.login.LoginFragment;
import com.db.OrmDateBaseHelper;
import com.db.dao.interfaze.UserDao;
import com.db.model.Contact;
import com.db.model.User;
import com.hengtiansoft.cloudcontact.R;
import com.util.ContactUtil;
import com.widget.HoloAlert; 

public class MainActivity extends CommonActivity {

	private FrameLayout contentLayout;
	private FragmentManager fm;
	private FragmentTransaction ft;
	private HoloAlert holoAlert;  

//	@Override 
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//		// testQuery();
//		init();
//	}
//
//	public void init() {
//		holoAlert = (HoloAlert) findViewById(R.id.HoloAlert);
//		contentLayout = (FrameLayout) findViewById(R.id.Layout_Content);
//		fm = getSupportFragmentManager();
//		ft = fm.beginTransaction();
//		Fragment loginFragment = getLoginFragment();
//		ft.add(R.id.Layout_Content, loginFragment);
//		ft.commit();
//		
//		OrmDateBaseHelper ormDateBaseHelper = new OrmDateBaseHelper(this,
//				"cloud_contact", null, 1);
//		UserDao userDao = ormDateBaseHelper.getUserDao();
//		User user = new User();
//		user.setName("123");
//		user.setCellPhon1("13567124034");
//		user.setEmail("xxx@gmail.com");
//		try {
//			userDao.create(user);
//			
//			List<User> users=userDao.queryForAll();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//
//	public Fragment getLoginFragment() {
//		Fragment fragment = new LoginFragment();
//		return fragment;
//	}
//
//	/**
//	 * unit test query
//	 */
//	public void testQuery() {
//		ContactUtil contactUtil = new ContactUtil(this);
//		List<Contact> contacts = contactUtil.query();
//
//	}

	 

}
