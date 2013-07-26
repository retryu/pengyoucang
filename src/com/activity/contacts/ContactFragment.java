package com.activity.contacts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.db.model.Contact;
import com.db.model.Friend;
import com.hengtiansoft.cloudcontact.R;
import com.http.ContactApi;
import com.http.FriendApi;
import com.model.FriendItem;
import com.util.ContactUtil;
import com.util.PinyinComparator;
import com.view.DropBottomView;
import com.widget.DropBottomListView;
import com.widget.OprationDialog;
import com.widget.SideBar;

/**
 * @author retryu E-mail:ruanchenyugood@gmail.com
 * @version create Time��2013-6-10 ����02:24:05 file declare:
 */
public class ContactFragment extends Fragment {

	private DropBottomListView contactListView;
	private OldContactAdapter cAdapter;
	private ContactAdapter contactAdapter;
	private View view;
	private ContactUtil contactUtil;
	private static final int MSG_SHOW_CONTACT = 1;
	private static final int MSG_CLEAR = 2;
	private static final int MSG_MERGE = 3;
	public static final int MSG_UPDATE = 4;
	private UiHandler uiHandler;
	private DropBottomView dropBottomView;
	private List<FriendItem> contacts = null;
	private LayoutInflater layoutInflater;
	private Button btnUoload;
	private Button btnRestore;
	private ContactUtil conUtil;
	private SideBar sideBar;
	private TextView mDialogText;
	private RelativeLayout layoutProgress;
	private Activity activity;

	private static final int MSG_ALERT = 5;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		layoutInflater = inflater;
		view = inflater.inflate(R.layout.fragment_contacts, null);
		initWidget();
		return view;
	}

	public void initData() {
		Log.e("debug`", "initData");
		contactUtil = new ContactUtil(getActivity());
	}

	public void initWidget() {
		Log.e("debug", "initWidget");
		ContactUtil.setContext(getActivity());
		activity = getActivity();
		sideBar = (SideBar) view.findViewById(R.id.sideBar);
		dropBottomView = (DropBottomView) view
				.findViewById(R.id.DropBottomView_Contact);
		contactListView = (DropBottomListView) view
				.findViewById(R.id.ListView_Contacts);
		contactAdapter = new ContactAdapter(getActivity());
		mDialogText = (TextView) view.findViewById(R.id.alphaText);
		sideBar.setListView(contactListView);
		sideBar.setTextView(mDialogText);
		contactListView.setAdapter(contactAdapter);
		layoutProgress = (RelativeLayout) view
				.findViewById(R.id.Layout_Progress);
		// addBottomVidw();
		uiHandler = new UiHandler();
		contactAdapter.setUiHandler(uiHandler);
		contactListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				FriendItem friendItem = contactAdapter.getFriends().get(
						position);

				Log.d("debug", "" + friendItem);
				OprationDialog oprationDialog = new OprationDialog(activity);
				oprationDialog.showDialog(friendItem);
			}

		});

		init();

	}

	public void clear() {
		Message msg = new Message();
		msg.what = MSG_CLEAR;
		uiHandler.sendMessage(msg);
	}

	public void updeData() {

		new Thread() {
			public void run() {
				showMessage("正在更新数据");
				// List<FriendItem> friendItems = getContacts();
				List<FriendItem> friendItems = new ArrayList<FriendItem>();
				List<Friend> f = FriendApi.getFriends();
				List<FriendItem> frinds = FriendApi.ToFriendItems(f);
				friendItems.addAll(frinds);

				Message msg = new Message();
				msg.what = MSG_SHOW_CONTACT;
				msg.obj = friendItems;
				uiHandler.sendMessage(msg);

			};
		}.start();
	}

	public void init() {
	 

		new Thread() {
			public void run() {
				List<FriendItem> friendItems = new ArrayList<FriendItem>();
				List<Friend> f = FriendApi.getFriends();
				List<FriendItem> frinds = FriendApi.ToFriendItems(f);
				friendItems.addAll(frinds);
				Message msg = new Message();
				msg.what = MSG_MERGE;
				msg.obj = friendItems;
				uiHandler.sendMessage(msg);

			};
		}.start();

	}

	public void showMessage(String msgStr) {
		if (uiHandler != null) {
			Message msg = new Message();
			msg.what = MSG_ALERT;
			msg.obj = msgStr;
			uiHandler.sendMessage(msg);
		}
	}

	public void getFriend() {

	}

	public void addTextView() {

	}

	public void addBottomVidw() {
		Context context = getActivity();
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater
				.inflate(R.layout.layout_bottom_contact, null);
		btnUoload = (Button) view.findViewById(R.id.Btn_Upload);
		btnRestore = (Button) view.findViewById(R.id.Btn_Restore);
		dropBottomView.setView(view);
		contactListView.setDropBottom(dropBottomView);

		btnUoload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new Thread() {
					@Override
					public void run() {
						super.run();

						// ContactApi.backUpCpntacts(contacts);

					}
				}.start();

			}
		});

		btnRestore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ContactApi.restoreContacts();

			}
		});
	}

	public List<FriendItem> getContacts() {
		Log.e("debug", "getContacts" + activity);
		List<FriendItem> friendItems;
		contactUtil = new ContactUtil(activity);
		List<Contact> contacts = contactUtil.query();
		friendItems = contactUtil.getFriendItem(contacts);
		return friendItems;
	}

	class UiHandler extends Handler {
		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			switch (msg.what) {
			case MSG_SHOW_CONTACT:
				contacts = (List<FriendItem>) msg.obj;
				contactAdapter.setContacts(contacts);
				layoutProgress.setVisibility(View.GONE);
				sideBar.setVisibility(View.VISIBLE);
				Toast.makeText(activity, "更新完成", Toast.LENGTH_LONG).show();
				break;

			case MSG_CLEAR:
				contactAdapter.setContacts(null);
				layoutProgress.setVisibility(View.VISIBLE);
				sideBar.setVisibility(View.GONE);
				break;
			case MSG_MERGE:
				contacts = (List<FriendItem>) msg.obj;
				contactAdapter.mergeContact(contacts);
				if (contactAdapter.getFriends() != null) {
					layoutProgress.setVisibility(View.GONE);
					sideBar.setVisibility(View.VISIBLE);
				}
				break;
			case MSG_ALERT:
				String alertStr = (String) msg.obj;
				Toast.makeText(activity, alertStr, Toast.LENGTH_LONG).show();
				break;
			case MSG_UPDATE:
				List<FriendItem> friendItems = (List<FriendItem>) msg.obj;
				if (friendItems != null) {
					Collections.sort(friendItems, new PinyinComparator());
					contactAdapter.notifyDataSetChanged();
				}
				break;
			}
		}
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

}
