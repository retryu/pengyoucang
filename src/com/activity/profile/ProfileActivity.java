package com.activity.profile;

import java.sql.SQLException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.activity.CommonActivity;
import com.activity.IndexActivity;
import com.activity.contacts.OldContactAdapter;
import com.db.OrmDateBaseHelper;
import com.db.dao.interfaze.MsgDao;
import com.db.dao.interfaze.UserDao;
import com.db.model.User;
import com.hengtiansoft.cloudcontact.LoginActivity;
import com.hengtiansoft.cloudcontact.R;
import com.http.UserInfoApi;
import com.umeng.fb.FeedbackAgent;
import com.widget.HeadEditText;
import com.widget.LodingAlert;
import com.widget.OpratAlert;

/**
 * @author retryu E-mail:ruanchenyugood@gmail.com
 * @version create Time��2013-6-21 ����01:29:33 file declare:
 */
public class ProfileActivity extends CommonActivity implements
		android.view.View.OnClickListener {
	private UserDao userDao;
	private OrmDateBaseHelper ormDateBaseHelper;
	private HeadEditText HeMobile;
	private HeadEditText HeHome;
	// private HeadEditText HeQQ;
	private HeadEditText HeEmail;
	private HeadEditText HeName;
	private Button btnUpdate;
	private Button btnBack;
	private static final int MSG_ALERT = 1;
	private UiHandler uiHandler;
	public final static int MSG_LODING = 5;
	public final static int MSG_DISMIS = 6;
	LodingAlert lodingAlert;
	private Activity activity;
	private Button btnLogout;
	private TextView teHead;
	private Button btnFeedBack;
	public static IndexActivity indexActivity;
	private User userOld;
	OpratAlert opratAlert;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_profile);
		ormDateBaseHelper = getCommoApp().getOrmDateBaseHelper();
		userDao = ormDateBaseHelper.getUserDao();
		initWidget();
		try {
			userOld = userDao.queryForAll().get(0);
			bindData(userOld);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void initWidget() {
		activity = this;
		HeName = (HeadEditText) findViewById(R.id.He_Name);
		HeHome = (HeadEditText) findViewById(R.id.He_Home);
		HeMobile = (HeadEditText) findViewById(R.id.He_Mobile);
		HeEmail = (HeadEditText) findViewById(R.id.He_Email);
		teHead = (TextView) findViewById(R.id.Tv_Head);
		btnUpdate = (Button) findViewById(R.id.Btn_Update);
		btnBack = (Button) findViewById(R.id.Btn_Back);
		btnLogout = (Button) findViewById(R.id.Btn_Logout);
		btnFeedBack = (Button) findViewById(R.id.Btn_FeedBack);
		btnLogout.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		btnUpdate.setOnClickListener(this);
		btnFeedBack.setOnClickListener(this);
		uiHandler = new UiHandler();
	}

	public void bindData(User user) {
		if (user.getCellPhon1() != null) {
			HeMobile.setContent(user.getCellPhon1());
		}
		if (user.getCellPhone2() != null) {
			HeHome.setContent(user.getCellPhone2());
		}
		if (user.getName() != null) {
			HeName.setContent(user.getName());
		}
		// if (user.getTodo_one() != null) {
		// HeQQ.setContent(user.getTodo_one());
		// }
		if (user.getEmail() != null) {
			// HeEmail.setContent(user.getEmail());
			teHead.setText(teHead.getText().toString() + "(" + user.getEmail()
					+ ")");
		}
	}

	public User getUser() {
		User user = null;
		try {
			user = userDao.queryForAll().get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String mobile = HeMobile.getContent();
		String home = HeHome.getContent();
		String name = HeName.getContent();
		// String qq = HeQQ.getContent();
		user.setCellPhon1(mobile);
		user.setCellPhone2(home);
		user.setName(name);

		// user.setTodo_one(qq);

		return user;
	}

	public boolean isChange(User user) {
		Log.e("debug",
				"phone1:" + user.getCellPhon1() + "  phone2:"
						+ userOld.getCellPhon1());
		if (!user.getCellPhon1().equals(userOld.getCellPhon1())) {
			return true;
		}
		if (!user.getCellPhone2().equals(userOld.getCellPhone2())) {
			return true;
		}
		if (!user.getName().equals(userOld.getName())) {
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.Btn_Update:
			final User user = getUser();
			if (HeName.getContent().equals("")) {
				Toast.makeText(activity,
						getResources().getString(R.string.name_cant_be_null),
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (HeMobile.getContent().equals("")) {
				Toast.makeText(activity,
						getResources().getString(R.string.phone_cant_be_null),
						Toast.LENGTH_SHORT).show();
				return;
			}
			// if (HeHome.getContent().length() != 0) {
			// Toast.makeText(activity,
			// getResources().getString(R.string.home_cant_be_null),
			// Toast.LENGTH_SHORT).show();
			// return;
			// }

			if (HeMobile.getContent().length() != 11) {
				Toast.makeText(activity,
						getResources().getString(R.string.alert_mobile_length),
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (HeHome.getContent().length() > 20) {
				Toast.makeText(
						activity,
						getResources()
								.getString(R.string.phone_number_too_long),
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (isChange(user) == false) {
				Toast.makeText(this,
						getResources().getString(R.string.alert_change),
						Toast.LENGTH_LONG).show();
				return;
			}

			lodingAlert = new LodingAlert(activity);
			lodingAlert.setContent(getResources().getString(
					R.string.please_wait));
			lodingAlert.show();

			new Thread() {

				public void run() {
					UserInfoApi.updateInfo(user);
					try {
						userDao.update(user);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Message msg = new Message();
					msg.what = MSG_ALERT;
					msg.obj = getResources().getString(R.string.please_wait);
					uiHandler.sendMessage(msg);
				};
			}.start();
			break;
		case R.id.Btn_Back:
			Intent intent = new Intent(this, IndexActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.Btn_Logout:
			// Toast.makeText(getApplicationContext(), "正在注销",
			// Toast.LENGTH_SHORT)
			// .show();
			opratAlert = new OpratAlert(activity);
			opratAlert.setTitle(getResources().getString(R.string.alert));
			opratAlert.setAlert(getResources().getString(R.string.sure_logout));
			opratAlert.setBtnCancelListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					opratAlert.dismiss();
				}
			});

			opratAlert.setBtnOkListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					opratAlert.dismiss();
					logOut();
				}
			});
			opratAlert.show();
			break;

		case R.id.Btn_FeedBack:
			FeedbackAgent agent = new FeedbackAgent(this);
			agent.startFeedbackActivity();
			break;
		}
	}

	private void logOut() {
		if (indexActivity != null) {
			indexActivity.finish();
		}
		lodingAlert = new LodingAlert(activity);
		lodingAlert.setContent(getResources().getString(R.string.please_wait));
		lodingAlert.show();
		JPushInterface.stopPush(this);

		try {
			List<User> users = userDao.queryForAll();
			for (int i = 0; i < users.size(); i++) {
				User u = users.get(i);
				userDao.delete(u);
			}
			MsgDao msgDao = ormDateBaseHelper.getMsgDao();
			msgDao.delete(msgDao.getMessages());
			lodingAlert.dismiss();
			Intent intentLogin = new Intent(this, LoginActivity.class);
			startActivity(intentLogin);
			finish();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class UiHandler extends Handler {
		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			int what = msg.what;
			switch (what) {
			case MSG_ALERT:

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				lodingAlert.dismiss();
				Toast.makeText(activity, getString(R.string.finish_update),
						Toast.LENGTH_LONG).show();
				break;

			default:
				break;
			}

		}

	}

}
