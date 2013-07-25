package com.activity;

import java.sql.SQLException;

import cn.jpush.android.api.JPushInterface;

import com.db.OrmDateBaseHelper;
import com.db.dao.interfaze.UserDao;
import com.db.model.User;
import com.hengtiansoft.cloudcontact.LoginActivity;
import com.hengtiansoft.cloudcontact.R;
import com.http.HttpUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

/**
 * @author retryu E-mail:ruanchenyugood@gmail.com
 * @version create Time：2013-7-24 下午01:42:38 file declare:
 */
public class SplashActivity extends CommonActivity {
	private UserDao userDao;
	private OrmDateBaseHelper ormDateBaseHelper;
	private UiHandler uiHandler;
	private final int MSG_TO_LOGIN = 1;
	private final int MSG_TO_INDEX = 2;
	private CommonActivity commonActivity;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_splash);
		uiHandler = new UiHandler();
		commonActivity = this;
		new Thread() {
			public void run() {
				try {
					Thread.sleep(1500);
				} catch (Exception e) {
					e.printStackTrace();
				}
				ormDateBaseHelper = getCommoApp().getOrmDateBaseHelper();
				userDao = ormDateBaseHelper.getUserDao();
				boolean isExits = userDao.isExits();
				if (isExits == true) {
					User user = null;
					try {
						user = userDao.queryForAll().get(0);
						JPushInterface.resumePush(commonActivity);
						JPushInterface.setAliasAndTags(getApplicationContext(),
								user.getUserId(), null);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					HttpUtil.setUser(user);
					Intent intent = new Intent(commonActivity,
							IndexActivity.class);
					startActivity(intent);
					finish();
				} else {
					Intent intent = new Intent(commonActivity,
							LoginActivity.class);
					startActivity(intent);
					finish();
				}
			};
		}.start();

	}

	public class UiHandler extends Handler {
		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			int what = msg.what;
			switch (what) {
			case MSG_TO_INDEX:

				break;

			default:
				break;
			}

		}
	}

}
