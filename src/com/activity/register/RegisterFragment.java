package com.activity.register;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.activity.CommonActivity;
import com.activity.IndexActivity;
import com.db.OrmDateBaseHelper;
import com.db.dao.interfaze.UserDao;
import com.db.model.User;
import com.hengtiansoft.cloudcontact.R;
import com.http.CommonApi;
import com.http.HttpUtil;
import com.http.LoginApi;
import com.http.RegisterApi;
import com.http.response.CommonResponse;
import com.umeng.analytics.c;
import com.widget.HeadEditText;
import com.widget.LodingAlert;

public class RegisterFragment extends android.support.v4.app.Fragment implements
		OnClickListener {

	private HeadEditText heEmail;
	private HeadEditText heUsername;
	private HeadEditText hePassword;
	private HeadEditText heMobile;
	private HeadEditText heSurePassWord;
	private View view;
	private static final int MSG_ALERT = 1;
	private static final int MSG_TOMAIN = 2;
	private CommonActivity commonActivity;
	private Button btnContinue;
	private UiHander uiHander;
	private OrmDateBaseHelper ormDateBaseHelper;
	private LodingAlert lodingAlert;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_register, null);
		initWidget();
		return view;
	}

	public void initWidget() {
		ormDateBaseHelper = commonActivity.getOrmDateBaseHelper();
		heSurePassWord = (HeadEditText) view
				.findViewById(R.id.He_Sure_PassWord);
		heEmail = (HeadEditText) view.findViewById(R.id.He_Email);
		heMobile = (HeadEditText) view.findViewById(R.id.He_Mobile);
		hePassword = (HeadEditText) view.findViewById(R.id.He_PassWord);
		heUsername = (HeadEditText) view.findViewById(R.id.He_UserName);
		btnContinue = (Button) view.findViewById(R.id.Btn_Continue);
		btnContinue.setOnClickListener(this);
		uiHander = new UiHander();

		TextWatcher tw = new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				Log.e("debuf", "textChange");
				if (checkAllFilled() == true) {
					btnContinue.setTextColor(Color.parseColor("#7dd4ff"));
					btnContinue
							.setBackgroundResource(R.drawable.registerbutton);
				} else {
					btnContinue
							.setBackgroundResource(R.drawable.register_button_unpress);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		};
		heEmail.addTextWatche(tw);
		heMobile.addTextWatche(tw);
		hePassword.addTextWatche(tw);
		heUsername.addTextWatche(tw);
		heEmail.addTextWatche(tw);
		heSurePassWord.addTextWatche(tw);
	}

	public boolean checkAllFilled() {

		if (heEmail.getContent().length() > 0
				&& heMobile.getContent().length() > 0
				&& hePassword.getContent().length() > 0
				&& heUsername.getContent().length() > 0
				&& heSurePassWord.getContent().length() > 0) {
			return true;
		}
		return false;
	}

	public User getUser() {
		User user = new User();
		user.setEmail(heEmail.getContent());
		user.setPassword(hePassword.getContent());
		user.setName(heUsername.getContent());
		user.setCellPhon1(heMobile.getContent());
		return user;
	}

	class UiHander extends Handler {
		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			int what = msg.what;
			switch (what) {
			case MSG_ALERT:
				if (lodingAlert != null) {
					lodingAlert.dismiss();
				}
				String errResponse = (String) msg.obj;
				Toast.makeText(getActivity(), errResponse, Toast.LENGTH_LONG)
						.show();
				break;

			case MSG_TOMAIN:
				Intent intent = new Intent(commonActivity, IndexActivity.class);
				startActivity(intent);
				break;
			}

		}
	}

	public boolean checkInfo() {
		if (hePassword.getContent().length() < 8) {
			Toast.makeText(commonActivity,
					commonActivity.getString(R.string.alert_password_leng),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (hePassword.getContent().length() > 30) {
			Toast.makeText(commonActivity,
					commonActivity.getString(R.string.alert_password_too_long),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	public boolean checkPhone() {
		if (heMobile.getContent().length() != 11) {
			Toast.makeText(commonActivity,
					commonActivity.getString(R.string.alert_mobile_length),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	public boolean checkPassWord() {
		if (!hePassword.getContent().equals(heSurePassWord.getContent())) {
			Log.e("debug", "p1:" + hePassword.getContent() + "  p2:"
					+ heSurePassWord.getContent());
			return false;
		}
		return true;
	}

	public boolean checkEmail(String mail) {
		Pattern p = Pattern
				.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher m = p.matcher(mail);
		return m.find();
	}

	public void check() {
	}

	@Override
	public void onClick(View v) {

		int id = v.getId();
		switch (id) {
		case R.id.Btn_Continue:
			if (checkAllFilled() == false) {
				return;
			}
			if (checkEmail(heEmail.getContent()) == false) {
				Toast.makeText(commonActivity,
						commonActivity.getString(R.string.alery_mail_err),
						Toast.LENGTH_SHORT).show();
				return;
			} 
			if (checkInfo() == false) {
				Toast.makeText(commonActivity,
						commonActivity.getString(R.string.alert_password_leng),
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (checkPassWord() == false) {
				Toast.makeText(commonActivity,
						commonActivity.getString(R.string.alert_password_same),
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (checkPhone() == false) {
				return;
			}
			showAlert();
			new Thread() {
				public void run() {
					User user = getUser();
					CommonResponse response = RegisterApi.register(user);
					if (response.getStateCode() == HttpUtil.CODE_SUCESS) {
						Intent intent = new Intent(commonActivity,
								IndexActivity.class);
						String userJson = response.getResponse();
						User u = LoginApi.toUser(userJson);
						HttpUtil.setUser(u);
						JPushInterface.resumePush(commonActivity);
						JPushInterface.setAliasAndTags(
								commonActivity.getApplicationContext(),
								u.getUserId(), null);
						UserDao userDao = ormDateBaseHelper.getUserDao();
						try {
							user.setAccess_token(u.getAccess_token());
							user.setUserId(u.getUserId());
							userDao.create(user);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						startActivity(intent);
						commonActivity.finish();
					} else {
						String errResponse = response.getResponse();
						Message msg = new Message();
						msg.what = MSG_ALERT;
						if (errResponse != null) {
							String err = CommonApi.toErrorResponse(errResponse)
									.getMessage();
							msg.obj = err;
						} else {
							msg.obj = "网络错误";
						}
						uiHander.sendMessage(msg);
					}
				};
			}.start();
			break;

		default:
			break;
		}
	}

	private void showAlert() {
		lodingAlert = new LodingAlert(commonActivity);
		lodingAlert.setContent(getResources().getString(R.string.registering));
		lodingAlert.show();
	}

	public CommonActivity getLoginActivity() {
		return commonActivity;
	}

	public void setLoginActivity(CommonActivity ca) {
		this.commonActivity = ca;
	}

	public void setOrmDateBaseHelper(OrmDateBaseHelper ormDateBaseHelper) {
		this.ormDateBaseHelper = ormDateBaseHelper;
	}

}
