package com.activity.login;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.activity.IndexActivity;
import com.db.OrmDateBaseHelper;
import com.db.dao.interfaze.UserDao;
import com.db.model.User;
import com.hengtiansoft.cloudcontact.LoginActivity;
import com.hengtiansoft.cloudcontact.R;
import com.http.CommonApi;
import com.http.HttpUtil;
import com.http.LoginApi;
import com.http.RegisterApi;
import com.http.response.CommonResponse;
import com.http.response.ErrorResponse;
import com.widget.HeadEditText;
import com.widget.LodingAlert;

public class LoginFragment extends Fragment implements OnClickListener {

	private View view;
	private Button btnLogging;
	private Button btnRegister;
	// private EditText etUserMail;
	// private EditText etUserPass;
	private HeadEditText etMail;
	private HeadEditText etPass;
	private LoginActivity loginActivity;
	private UiHandler uiHandler;
	private OrmDateBaseHelper ormDateBaseHelper;
	private Button btnLogOut;

	private static final int MSG_TO_MAIN = 1;
	private static final int MSG_SHOW_ERROR = 2;
	private static final int MSG_SHOW = 3;

	LodingAlert lodingAlert;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = (View) inflater.inflate(R.layout.fragment_loging, null);
		initWidget();
		return view;
	}

	public void initWidget() {
		uiHandler = new UiHandler();
		loginActivity = (LoginActivity) getActivity();
		btnLogging = (Button) view.findViewById(R.id.Btn_Logging);
		btnRegister = (Button) view.findViewById(R.id.Btn_Register);
		etMail = (HeadEditText) view.findViewById(R.id.Et_Email);
		etPass = (HeadEditText) view.findViewById(R.id.Et_PassWord);
		btnLogging.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		btnLogOut = (Button) view.findViewById(R.id.logout);

	}

	public boolean checkEmail(String mail) {
		Pattern p = Pattern
				.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher m = p.matcher(mail);
		return m.find();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Btn_Logging:
			login();
			break;

		case R.id.Btn_Register:
			// LoginActivity mainActivity = (LoginActivity) getActivity();
			// Intent intent=new Intent(mainActivity, MainActivity.class);
			// startActivity(intent);

			// register();
			loginActivity.toRegister();
			break;

		}
	}

	public void login() {
		String userName = etMail.getContent();
		String password = etPass.getContent();
		final User u = new User();
		u.setEmail(userName);
		u.setPassword(password);
		if (check(u) == false) {
			return;
		}

		showAlert();

		new Thread() {
			public void run() {
				CommonResponse commonResponse = LoginApi.login(u);
				if (commonResponse.getStateCode() == HttpUtil.CODE_SUCESS) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					JPushInterface.resumePush(loginActivity);

					User user = LoginApi.toUser(commonResponse.getResponse());
					JPushInterface.setAliasAndTags(
							loginActivity.getApplicationContext(),
							user.getUserId(), null);
					HttpUtil.setUser(user);

					// ContactApi.restoreContacts();

					Message msg = new Message();
					msg.what = MSG_TO_MAIN;
					msg.obj = user;
					uiHandler.sendMessage(msg);

				} else {
					Message msg = new Message();
					msg.what = MSG_SHOW_ERROR;

					if (commonResponse.getResponse() != null) {
						ErrorResponse errResponse = CommonApi
								.toErrorResponse(commonResponse.getResponse());
						Log.e("debug", "msg:" + errResponse.toString());
						msg.obj = errResponse.getMessage();
					} else {
						msg.obj = "网络连接错误";
					}
					uiHandler.sendMessage(msg);
				}
			};
		}.start();

	}

	private void showAlert() {
		lodingAlert = new LodingAlert(loginActivity);
		lodingAlert.setContent(getResources().getString(R.string.loging));
		lodingAlert.show();
	}

	public boolean check(User user) {
		if (user.getEmail().equals("")) {
			Toast.makeText(loginActivity,
					loginActivity.getString(R.string.alert_account_null),
					Toast.LENGTH_SHORT).show();
			return false;
		}

		if (user.getEmail().equals("") && user.getPassword().equals("")) {
			Toast.makeText(loginActivity,
					loginActivity.getString(R.string.all_null),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (user.getEmail().equals("")) {
			Toast.makeText(loginActivity,
					loginActivity.getString(R.string.alery_mail_err),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (user.getPassword().equals("")) {
			Toast.makeText(loginActivity,
					loginActivity.getString(R.string.alert_passwod_null),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (checkEmail(user.getEmail()) == false) {
			Toast.makeText(loginActivity,
					loginActivity.getString(R.string.alery_mail_err),
					Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	public void register() {
		final User user = getUserInfo();

		new Thread() {
			public void run() {
				CommonResponse response = RegisterApi.register(user);
				User user = LoginApi.toUser(response.getResponse());

				JPushInterface.setAliasAndTags(
						loginActivity.getApplicationContext(),
						user.getUserId(), null);

			};
		}.start();

	}

	public void loging() {
		User user = getUserInfo();

	}

	public User getUserInfo() {
		String userMail = etMail.getContent();
		String userPassWord = etPass.getContent();
		User user = new User();
		user.setEmail(userMail);
		user.setPassword(userPassWord);
		return user;
	}

	class UiHandler extends Handler {
		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			switch (msg.what) {
			case MSG_TO_MAIN:
				lodingAlert.dismiss();
				UserDao userDao = loginActivity.getCommoApp()
						.getOrmDateBaseHelper().getUserDao();
				User u = (User) msg.obj;
				try {
					userDao.create(u);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				HttpUtil.setUser(u);
				Intent intent = new Intent(getActivity(), IndexActivity.class);
				intent.putExtra("type", 0);
				startActivity(intent);
				loginActivity.finish();
				break;

			case MSG_SHOW_ERROR:
				if (lodingAlert != null) {
					lodingAlert.dismiss();
				}
				String msgStr = (String) msg.obj;
				Toast.makeText(loginActivity, msgStr, Toast.LENGTH_SHORT)
						.show();
				// loginActivity.showAlert(msgStr);
				break;
			case MSG_SHOW:
				String msgContent = (String) msg.obj;
				Toast.makeText(loginActivity, msgContent, Toast.LENGTH_SHORT)
						.show();
				// loginActivity.showAlert(msgContent);

				break;
			}

		}
	}

}
