package com.activity.message;

import java.sql.SQLException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.activity.CommonActivity;
import com.activity.MainActivity;
import com.activity.friend.FriendInfoActivity;
import com.application.CommonApplication;
import com.db.OrmDateBaseHelper;
import com.db.dao.interfaze.MsgDao;
import com.db.dao.interfaze.UserDao;
import com.db.model.Message;
import com.db.model.User;
import com.hengtiansoft.cloudcontact.R;
import com.http.HttpUtil;
import com.http.MessageApi;
import com.util.PushUtil;
import com.widget.LodingAlert;

public class MessageActivity extends CommonActivity implements OnClickListener {

	public static final int MSG_DATA = 1;
	public static final int MSG_UPDATE = 2;
	public static final int MSG_CHANGE_STATE = 3;
	public static final int MSG_RESPONSE = 4;
	private ListView listViewMessage;
	MessageAdapter msgAdapter;
	private UIHandler uiHandler;
	private Intent msgIntent;
	boolean needRefersh = false;
	public final String MSG_UPDATA = "MSG_UPDATE";
	private OrmDateBaseHelper ormDateBaseHelper;
	private MsgDao msgDao;
	private Button btnBack;
	private UserDao userDao;
	public final static int MSG_LODING = 5;
	public final static int MSG_DISMIS = 6;
	LodingAlert lodingAlert;
	private Activity activity;
	private TextView tvAlert;

	@Override
	public void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_message_list);
		ormDateBaseHelper = getCommoApp().getOrmDateBaseHelper();
		msgDao = ormDateBaseHelper.getMsgDao();
		userDao = ormDateBaseHelper.getUserDao();
		User user;
		try {
			user = userDao.queryForAll().get(0);
			HttpUtil.setUser(user);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initWidget();
		initData();
		Intent intent = getIntent();
		checkMessgae(intent);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		checkMessgae(intent);
	}

	private void checkMessgae(Intent intent) {
		if (intent.getExtras() != null) {
			Bundle bundle = intent.getExtras();
			int type = bundle.getInt("type");
			if (type == MSG_UPDATE) {
				getMsg(intent);
			}
			if (type == MSG_RESPONSE) {
				String msgJson = bundle.getString(PushUtil.PUSH_DATA_EXTRA);
				Message msg = MessageApi.getPushMessage(msgJson);
				android.os.Message msgOS = new android.os.Message();
				msgOS.what = MSG_UPDATE;
				msgOS.obj = msg;
				uiHandler.sendMessage(msgOS);
			}
		}

	}

	private void getMsg(Intent intent) {
		msgIntent = intent;
		if (msgIntent != null) {
			Bundle b = msgIntent.getExtras();
			if (b != null) {
				String msgJson = b.getString(PushUtil.PUSH_DATA_EXTRA);
				System.err.println("msgId" + msgJson);
				final int id = MessageApi.getID(msgJson);
				new Thread() {
					public void run() {

						Message msg = MessageApi.getMessageById(id + "");
						android.os.Message msgOS = new android.os.Message();
						msgOS.what = MSG_UPDATE;
						msgOS.obj = msg;
						uiHandler.sendMessage(msgOS);
					};
				}.start();
			}
		}
	}

	public void initWidget() {
		activity = this;
		btnBack = (Button) findViewById(R.id.Btn_Back);
		tvAlert = (TextView) findViewById(R.id.Tv_Alert);
		listViewMessage = (ListView) findViewById(R.id.ListView_Message);
		msgAdapter = new MessageAdapter(this);
		msgAdapter.msgActivity = this;
		listViewMessage.setAdapter(msgAdapter);
		uiHandler = new UIHandler();
		msgAdapter.setUiHandler(uiHandler);
		msgAdapter.setMsgDao(msgDao);
		btnBack.setOnClickListener(this);

	}

	public void initData() {
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				android.os.Message msg = new android.os.Message();
				msg.what = MSG_DATA;
				uiHandler.sendMessage(msg);
			}
		}.start();
	}

	class UIHandler extends Handler {
		@Override
		public void dispatchMessage(android.os.Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			int msgCode = msg.what;
			switch (msgCode) {
			case MSG_DATA:
				try {
					List<Message> msgs = msgDao.getRequestMessages();
					msgAdapter.setMsgs(msgs);
					if (msgs.size() > 0) {
						tvAlert.setVisibility(View.INVISIBLE);
					} else {
						tvAlert.setVisibility(View.VISIBLE);
					}
					msgAdapter.notifyDataSetChanged();
				} catch (Exception e) {

				}
				break;

			case MSG_UPDATE:
				int msgId = msg.what;
				Message myMsg = (Message) msg.obj;
				// 如果消息为统一为好友 默认设置为已读
				needRefersh = true;
				if (myMsg.getMsg_type().equals("1")) {
					myMsg.setReaded(true);
				}
				try {
					msgDao.create(myMsg);
					List<Message> localMsgs = msgDao.getRequestMessages();
					if (localMsgs.size() > 0) {
						tvAlert.setVisibility(View.INVISIBLE);
					} else {
						tvAlert.setVisibility(View.VISIBLE);
					}
					msgAdapter.setMsgs(localMsgs);
					msgAdapter.notifyDataSetChanged();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			case MSG_CHANGE_STATE:
				if (lodingAlert != null) {
					lodingAlert.dismiss();
				}
				Message myMessage = (Message) msg.obj;
				msgAdapter.updateMessage(myMessage);
				break;

			case MSG_LODING:
				lodingAlert = new LodingAlert(activity);
				String content = (String) msg.obj;
				if (content != null) {
					lodingAlert.setContent(content);
				} else {
					lodingAlert.setContent(getResources().getString(
							R.string.please_wait));
				}
				lodingAlert.show();
				break;
			case MSG_DISMIS:
				lodingAlert.dismiss();
				break;
			}
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		back();
	}

	private void back() {
		Intent intent = new Intent(this, FriendInfoActivity.class);
		Log.e("refersh", "MessageACtivity  refersh" + needRefersh);
		intent.putExtra("needRefersh", needRefersh);
		startActivity(intent);
		finish();
	}

	@Override
	public void onClick(View v) {

		int id = v.getId();
		switch (id) {
		case R.id.Btn_Back:
			back();

			break;

		default:
			break;
		}
	}

}
