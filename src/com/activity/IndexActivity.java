package com.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.contacts.ContactFragment;
import com.activity.friend.FriendInfoActivity;
import com.activity.profile.ProfileActivity;
import com.db.OrmDateBaseHelper;
import com.db.dao.interfaze.MsgDao;
import com.db.model.Contact;
import com.hengtiansoft.cloudcontact.R;
import com.http.ContactApi;
import com.http.HttpUtil;
import com.http.response.CommonResponse;
import com.umeng.fb.FeedbackAgent;
import com.util.ContactUtil;
import com.widget.NumberNotify;
import com.widget.OpratAlert;
import com.widget.TaskButton;

/**
 * @author retryu E-mail:ruanchenyugood@gmail.com
 * @version create Time��2013-6-21 ����03:51:08 file declare:
 */
public class IndexActivity extends CommonActivity implements
		android.view.View.OnClickListener {
	private Button imgProfile;
	private RelativeLayout layoutContent;
	private FragmentManager fm;
	private FragmentTransaction ft;
	private IndexActivity activity;
	private Button imgFriend;
	private TextView tvUpload;
	private TextView tvFriend;
	private NumberNotify notify;
	ContactUtil contactUtil;

	private TaskButton taskButtonUpload;
	private TaskButton taskButtonDownload;
	private OrmDateBaseHelper ormDateBaseHelper;
	public static final int MSG_UPDATE = 1;
	private static final int MSG_UPDATE_DOWNLOAD = 3;
	private static final int MSG_FINISHDOWNLOAD = 4;
	private static final int MSG_UPLOAD = 1;
	private static final int MSG_UPLOAD_FINISH = 2;
	private static final int MSG_ALERT = 5;
	OpratAlert opratAlert;
	ContactFragment contactFragment;

	private UiHandler uiHandler;

	@Override
	public void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		activity = this;
		contactUtil = new ContactUtil(this);
		setContentView(R.layout.activity_index);
		initWidget();
		ormDateBaseHelper = this.getCommoApp().getOrmDateBaseHelper();
		Intent intent = getIntent();
		checkMessage(intent);
		updateNotify();
		FeedbackAgent agent = new FeedbackAgent(this);
		agent.sync();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		checkMessage(intent);
		updateNotify();
	}

	private void checkMessage(Intent intent) {
		if (intent == null)
			return;
		if (intent.getExtras() != null) {
			Bundle bundle = intent.getExtras();
			int type = bundle.getInt("type");
			if (type == MSG_UPDATE) {
				contactFragment.setActivity(this);
				contactFragment.clear();
				contactFragment.updeData();
			}

			boolean needRefersh = bundle.getBoolean("needRefersh");
			Log.e("refersh", "IndexActivity  refersh" + needRefersh);
			if (needRefersh == true) {
				contactFragment.setActivity(this);
				contactFragment.updeData();
			}
		}
	}

	public void initWidget() {
		ContactUtil.setContext(this);
		opratAlert = new OpratAlert(this);
		uiHandler = new UiHandler();
		imgProfile = (Button) findViewById(R.id.Img_Profile);
		// imgUpload = (ImageView) findViewById(R.id.Img_Upload);
		taskButtonUpload = (TaskButton) findViewById(R.id.TaskButton_Upload);
		taskButtonDownload = (TaskButton) findViewById(R.id.TaskButton_Download);
		imgFriend = (Button) findViewById(R.id.Img_Friend);
		tvFriend = (TextView) findViewById(R.id.Tv_Friend);
		notify = (NumberNotify) findViewById(R.id.NN_Msg);
		// layoutContent = (RelativeLayout) findViewById(R.id.Layout_Menu);
		fm = getSupportFragmentManager();
		ft = fm.beginTransaction();
		contactFragment = new ContactFragment();
		ft.replace(R.id.Layout_Content, contactFragment);
		ft.commit();
		imgProfile.setOnClickListener(this);
		taskButtonUpload.setOnclick(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				opratAlert = new OpratAlert(activity);
				opratAlert.setTitle(getResources().getString(R.string.alert));
				opratAlert.setAlert(getResources().getString(
						R.string.sure_upload));
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
						upload();
					}
				});

				opratAlert.show();

			}
		});
		taskButtonDownload.setOnclick(new OnClickListener() {

			@Override
			public void onClick(View v) {
				opratAlert = new OpratAlert(activity);
				opratAlert.setTitle(getResources().getString(R.string.alert));
				opratAlert.setAlert(getResources().getString(
						R.string.sure_download));
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
						download();
					}
				});

				opratAlert.show();
			}
		});

		imgFriend.setOnClickListener(this);

	}

	public void updateNotify() {
		MsgDao msgDao = ormDateBaseHelper.getMsgDao();
		int requestCount = msgDao.getRequetsCount();
		int notifyCount = msgDao.getNotifyCount();
		int total = requestCount + notifyCount;
		notify.setNumber(total);

	}

	@Override
	public void onClick(View v) {
		Log.e("debug", "onclick");
		int id = v.getId();
		switch (id) {
		case R.id.Img_Profile:

			Intent intentToProfile = new Intent(getApplicationContext(),
					ProfileActivity.class);
			startActivity(intentToProfile);
			ProfileActivity.indexActivity = activity;

			break;
		case R.id.TaskButton_Upload:

			break;

		case R.id.Img_Friend:
			Intent intent = new Intent(this, FriendInfoActivity.class);
			startActivity(intent);
			break;

		}
	}

	public void upload() {

		if (checkTask() == false) {
			return;
		}
		taskButtonUpload.startAnimation();
		new Thread() {
			@Override
			public void run() {
				super.run();

				List<Contact> contacts = contactUtil.query();
				CommonResponse commonResponse = ContactApi
						.backUpCpntacts(contacts);
				if (commonResponse.getStateCode() != HttpUtil.CODE_SUCESS) {
					Message msgAlert = new Message();
					msgAlert.what = MSG_ALERT;
					msgAlert.obj = getResources().getString(R.string.error);
					uiHandler.sendMessage(msgAlert);

				} else {
					Message msgFinish = new Message();
					msgFinish.what = MSG_UPLOAD_FINISH;
					uiHandler.sendMessage(msgFinish);
				}
			}
		}.start();

	}

	private boolean checkTask() {
		if (taskButtonUpload.isRuning() == true) {
			Toast.makeText(activity, "请等待上传完成", Toast.LENGTH_SHORT).show();
			return false;

		}
		if (taskButtonDownload.isRuning() == true) {
			Toast.makeText(activity, "正等待下载完成", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	public void download() {
		if (checkTask() == false) {
			return;
		}
		Log.e("debug", "chackTask:" + checkTask());
		new Thread() {
			@Override
			public void run() {
				super.run();
				taskButtonDownload.startProcess();
				CommonResponse response = ContactApi.restoreContacts();
				if (response.getStateCode() == com.http.HttpUtil.CODE_SUCESS) {

					List<Contact> contacts = ContactApi.toContacts(response
							.getResponse());
					ContactUtil contactUtil = new ContactUtil(activity);
					List<Contact> backContacts = contactUtil
							.getBackUpContact(contacts);
					int count = backContacts.size();
					String finishStr = getResources()
							.getString(R.string.finish);
					String processStr = finishStr + "0/" + count;
					for (int i = 0; i < backContacts.size(); i++) {
						Contact contact = backContacts.get(i);
						processStr = finishStr + i + "/" + count;
						Message msg = new Message();
						msg.what = MSG_UPDATE_DOWNLOAD;
						float precent = 100 * ((float) i / (float) count);
						msg.obj = (int) precent;
						uiHandler.sendMessage(msg);
						// try {
						// } catch (InterruptedException e) {
						// // TODO Auto-generated catch block
						// e.printStackTrace();
						// }
						contactUtil.insert(contact);
					}
					Message msg = new Message();
					msg.what = MSG_FINISHDOWNLOAD;
					uiHandler.sendMessage(msg);
				} else if (response.getStateCode() == com.http.HttpUtil.CODE_NULL) {
					Message msgAlert = new Message();
					msgAlert.what = MSG_ALERT;
					msgAlert.obj = getResources().getString(
							R.string.cannot_down_load);
					uiHandler.sendMessage(msgAlert);
				} else {
					Message msgAlert = new Message();
					msgAlert.what = MSG_ALERT;
					msgAlert.obj = getResources().getString(R.string.error);
					uiHandler.sendMessage(msgAlert);
				}

			}
		}.start();
	}

	class UiHandler extends Handler {
		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			int msgId = msg.what;
			switch (msgId) {
			case MSG_UPDATE_DOWNLOAD:
				int process = (Integer) msg.obj;
				taskButtonDownload.setProcess(process);
				break;

			case MSG_FINISHDOWNLOAD:
				taskButtonDownload.finish();
				opratAlert = new OpratAlert(activity);
				opratAlert.setTitle(getResources().getString(R.string.alert));
				// Toast.makeText(activity, "上传完成", Toast.LENGTH_SHORT).show();
				opratAlert.setAlert(getResources().getString(
						R.string.finishDownload));
				opratAlert.show();
				opratAlert.setBtnCancalVisiable(false);

				break;
			case MSG_UPLOAD:
				taskButtonUpload.finish();
				break;
			case MSG_UPLOAD_FINISH:
				taskButtonUpload.finish();
				opratAlert = new OpratAlert(activity);
				opratAlert.setTitle(getResources().getString(R.string.alert));
				// Toast.makeText(activity, "上传完成", Toast.LENGTH_SHORT).show();
				opratAlert.setAlert(getResources().getString(
						R.string.upload_finish));
				opratAlert.show();
				opratAlert.setBtnCancalVisiable(false);

				break;
			case MSG_ALERT:
				taskButtonUpload.finish();
				taskButtonDownload.finish();
				String alert = (String) msg.obj;
				opratAlert = new OpratAlert(activity);
				opratAlert.setTitle(getResources().getString(R.string.alert));
				// Toast.makeText(activity, "上传完成", Toast.LENGTH_SHORT).show();
				if (alert != null) {
					opratAlert.setAlert(alert);
				} else {
					opratAlert.setAlert(getResources()
							.getString(R.string.error));
				}
				opratAlert.show();
				opratAlert.setBtnCancalVisiable(false);
				opratAlert.setBtnOkListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						opratAlert.dismiss();
					}
				});
				break;

			}

		}
	}
}
