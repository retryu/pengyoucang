package com.activity.friend;

import java.sql.SQLException;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.activity.IndexActivity;
import com.activity.about.AboutActivity;
import com.activity.message.MessageActivity;
import com.application.CommonApplication;
import com.db.OrmDateBaseHelper;
import com.db.dao.interfaze.MsgDao;
import com.db.dao.interfaze.UserDao;
import com.db.model.User;
import com.hengtiansoft.cloudcontact.R;
import com.http.HttpUtil;
import com.http.MessageApi;
import com.widget.NotifyItem;
import com.widget.NumberNotify;

/**
 * @author retryu E-mail:ruanchenyugood@gmail.com
 * @version create Time：2013-6-26 上午11:17:48 file declare:
 */
public class FriendInfoActivity extends SherlockFragmentActivity implements
		OnClickListener {

	private NotifyItem itemInfoChange;
	private NotifyItem itemAaddConact;
	private NotifyItem itemRecomend;
	private NotifyItem itemAbout;
	private boolean needRefersh = false;
	private OrmDateBaseHelper ormDateBaseHelper;
	private NumberNotify numberNotify;
	private Button btnBack;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_friendinfo);
		CommonApplication commonApplication = (CommonApplication) getApplication();
		ormDateBaseHelper = commonApplication.getOrmDateBaseHelper();
		UserDao userDao = ormDateBaseHelper.getUserDao();
		User user;
		try {
			user = userDao.queryForAll().get(0);
			HttpUtil.setUser(user);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		initWidget();
		Intent intent = getIntent();
		checkIntent(intent);

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		checkIntent(intent);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		updateNotify();
		Log.e("debug", "resume");

	}

	public void checkIntent(Intent intent) {
		if (intent == null)
			return;
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			needRefersh = bundle.getBoolean("needRefersh");

		}
	}

	public void updateNotify() {
		MsgDao msgDao = ormDateBaseHelper.getMsgDao();
		int requestCount = msgDao.getRequetsCount();
		int notifyCount = msgDao.getNotifyCount();
		itemRecomend.setCount(requestCount);
		itemInfoChange.setCount(notifyCount);
		int total = requestCount + notifyCount;
		numberNotify.setNumber(total);

	}

	private void initWidget() {
		itemAbout = (NotifyItem) findViewById(R.id.Item_About);
		itemInfoChange = (NotifyItem) findViewById(R.id.Item_Info);
		itemRecomend = (NotifyItem) findViewById(R.id.Item_Recommend);
		itemAaddConact = (NotifyItem) findViewById(R.id.Item_Add_Contact);
		numberNotify = (NumberNotify) findViewById(R.id.NN_Msg);
		itemInfoChange.setOnClickListener(this);
		itemAaddConact.setOnClickListener(this);
		itemRecomend.setOnClickListener(this);
		btnBack = (Button) findViewById(R.id.Btn_Back);
		btnBack.setOnClickListener(this);
		itemAbout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		Intent intent;
		switch (id) {
		case R.id.Item_Info:
			intent = new Intent(this, InfoUpdateActivity.class);
			startActivity(intent);
			break;

		case R.id.Item_Add_Contact:
			intent = new Intent(this, SearchFriendActivity.class);
			startActivity(intent);
			break;

		case R.id.Item_Recommend:
			intent = new Intent(this, MessageActivity.class);
			startActivity(intent);
			break;
		case R.id.Btn_Back:
			back();
			break;
		case R.id.Item_About:
			Intent intentAbout = new Intent(this, AboutActivity.class);
			startActivity(intentAbout);
			break;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		back();
	}

	private void back() {
		Intent intent = new Intent(this, IndexActivity.class);
		Log.e("refersh", "FriendInfo  refersh:" + needRefersh);
		intent.putExtra("needRefersh", needRefersh);
		startActivity(intent);
		finish();
	}

}
