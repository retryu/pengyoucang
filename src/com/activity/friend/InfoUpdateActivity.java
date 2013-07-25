package com.activity.friend;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.CommonActivity;
import com.application.CommonApplication;
import com.db.OrmDateBaseHelper;
import com.db.dao.interfaze.MsgDao;
import com.db.model.Message;
import com.hengtiansoft.cloudcontact.R;
import com.http.MessageApi;
import com.widget.NumberNotify;

/**
 * @author retryu E-mail:ruanchenyugood@gmail.com
 * @version create Time：2013-6-28 下午02:42:14 file declare:
 */
public class InfoUpdateActivity extends CommonActivity implements
		android.view.View.OnClickListener {

	private static final int MSG_UPDATE = 1;

	private static final int MSG_INIT = 2;
	private OrmDateBaseHelper ormDateBaseHelper;
	private MsgDao msgDao;
	private UiHander hander;
	private ListView updateListView;
	private UpdateIngoAdapter updateIngoAdapter;
	private Button btnUpdate;
	private CheckBox checkAll;
	private NumberNotify numNotify;
	private Button btnBack;
	private TextView tvAll;
	private TextView tvAlert;

	private boolean needRefersh = false;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_update_info);
		initWidget();
	}

	public void initWidget() {
		hander = new UiHander();
		ormDateBaseHelper = ((CommonApplication) getApplication())
				.getOrmDateBaseHelper();
		updateListView = (ListView) findViewById(R.id.Lv_Update);
		numNotify = (NumberNotify) findViewById(R.id.NN_Msg);
		btnBack = (Button) findViewById(R.id.Btn_Back);
		checkAll = (CheckBox) findViewById(R.id.Check_All);
		tvAll = (TextView) findViewById(R.id.Tv_All);
		tvAlert = (TextView) findViewById(R.id.Tv_Alert);
		updateIngoAdapter = new UpdateIngoAdapter(this);
		updateListView.setAdapter(updateIngoAdapter);
		btnUpdate = (Button) findViewById(R.id.Btn_Update);
		btnUpdate.setOnClickListener(this);
		checkAll.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		msgDao = (MsgDao) ormDateBaseHelper.getMsgDao();
		checkMsgs(getIntent());

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		checkMsgs(intent);
	}

	public void checkMsgs(Intent intent) {
		if (intent.getExtras() != null) {
			Bundle bundle = intent.getExtras();
			String msg = bundle.getString("msg");
			if (msg != null) {
				Message MyMsg = MessageApi.getPushMessage(msg);
				android.os.Message msgOS = new android.os.Message();
				msgOS.what = MSG_UPDATE;
				msgOS.obj = MyMsg;
				hander.sendMessage(msgOS);
				needRefersh = true;
			}
		} else {
			android.os.Message msgOS = new android.os.Message();
			msgOS.what = MSG_INIT;
			hander.sendMessage(msgOS);
		}
	}

	class UiHander extends Handler {
		@Override
		public void dispatchMessage(android.os.Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			int what = msg.what;
			switch (what) {
			case MSG_UPDATE:
				Message m = (Message) msg.obj;
				try {
					msgDao.create(m);
				} catch (SQLException e) {
					e.printStackTrace();
				}

				List<UpdateInfo> infos = toUpdateInfo(msgDao
						.getNotifyMessages());
				numNotify.setNumber(infos.size());
				updateIngoAdapter.setInfos(infos);
				if (infos.size() == 0) {
					hide();
				} else {
					show();
				}
				break;

			case MSG_INIT:
				List<UpdateInfo> localInfos = toUpdateInfo(msgDao
						.getNotifyMessages());
				if (localInfos.size() == 0) {
					hide();
				} else {
					show();
				}
				updateIngoAdapter.setInfos(localInfos);
				numNotify.setNumber(localInfos.size());
				break;
			}
		}
	}

	public void show() {
		tvAlert.setVisibility(View.INVISIBLE);
		tvAll.setVisibility(View.VISIBLE);
		checkAll.setVisibility(View.VISIBLE);
		btnUpdate.setVisibility(View.VISIBLE);
	}

	public void hide() {
		tvAlert.setVisibility(View.VISIBLE);
		tvAll.setVisibility(View.INVISIBLE);
		checkAll.setVisibility(View.INVISIBLE);
		btnUpdate.setVisibility(View.INVISIBLE);

	}

	public List<UpdateInfo> toUpdateInfo(List<Message> msgs) {
		if (msgs == null)
			return null;
		List<UpdateInfo> infos = new ArrayList<UpdateInfo>();
		UpdateInfo info;
		for (int i = 0; i < msgs.size(); i++) {
			info = new UpdateInfo();
			info.setCheched(false);
			info.setMessage(msgs.get(i));
			infos.add(info);
		}
		return infos;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.Btn_Update:
			updateIngoAdapter.checkAll(true);
			List<UpdateInfo> infos = updateIngoAdapter.getInfos();
			update(infos);
		 
			List<UpdateInfo> localInfos = toUpdateInfo(msgDao
					.getNotifyMessages());
			updateIngoAdapter.setInfos(localInfos);
			numNotify.setNumber(localInfos.size());
			needRefersh = true;
			btnUpdate.setVisibility(View.INVISIBLE);
			Toast.makeText(this, "更新成功", Toast.LENGTH_LONG).show();
			break;

		case R.id.Check_All:
			if (checkAll.isChecked() == true) {
				updateIngoAdapter.checkAll(true);
			} else {
				updateIngoAdapter.checkAll(false);
			}
			break;

		case R.id.Btn_Back:
			back();
			break;
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		back();
	}

	private void back() {
		Intent intent = new Intent(this, FriendInfoActivity.class);
		Log.e("refersh", "MessageACtivity  refersh" + needRefersh);
		intent.putExtra("needRefersh", needRefersh);
		startActivity(intent);
		needRefersh = false;
		finish();
	}

	public void update(List<UpdateInfo> infos) {
		for (int i = 0; i < infos.size(); i++) {
			Message msg = infos.get(i).getMessage();
			try {

				msgDao.update(msg);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
