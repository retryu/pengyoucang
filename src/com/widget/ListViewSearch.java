package com.widget;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.widget.SearchView;
import com.activity.friend.FriendSearchAdapter;
import com.activity.friend.SearchResult;
import com.db.model.Friend;
import com.db.model.User;
import com.hengtiansoft.cloudcontact.R;
import com.http.CommonApi;
import com.http.FriendApi;
import com.http.HttpUtil;
import com.http.response.CommonResponse;
import com.http.response.ErrorResponse;
import com.model.FriendState;

/**
 * @author retryu E-mail:ruanchenyugood@gmail.com
 * @version create Time��2013-6-17 ����04:49:19 file declare:
 */
public class ListViewSearch extends ListView {

	private FriendSearchAdapter searchAdapter;
	private SearchView searchView;
	private final static int MSG_NOT_FOUND = 1;
	private final static int MSG_SHOW_RESULT = 2;
	private final static int MSG_ERROR = 3;
	private final static int MSG_ALER = 4;

	public final static int MSG_LODING = 5;
	public final static int MSG_DISMIS = 6;
	private UiHandler uiHandler;
	private com.widget.SearchView mySearchView;
	private Activity activity;
	LodingAlert lodingAlert;
	private User user;

	public ListViewSearch(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();

	}

	public ListViewSearch(Context context) {
		super(context);

		init();
	}

	public void init() {
		uiHandler = new UiHandler();
	}

	public void queryUser(String user) {
		final String u = user;
		new Thread() {
			@Override
			public void run() {
				super.run();
				CommonResponse response = FriendApi.getFriend(u);
				if (response == null) {
					Message msg = new Message();
					msg.what = MSG_ALER;
					msg.obj = getResources().getString(R.string.error);
					uiHandler.sendMessage(msg);
					return ;
				}
				if (response.getStateCode() == HttpUtil.CODE_NULL) {
					Message msg = new Message();
					msg.what = MSG_NOT_FOUND;
					uiHandler.sendMessage(msg);
				} else if (response.getStateCode() == HttpUtil.CODE_SUCESS) {
					Message msg = new Message();
					msg.what = MSG_SHOW_RESULT;
					FriendState friendState = FriendApi.checkFriend(response
							.getResponse());
					msg.obj = friendState;
					uiHandler.sendMessage(msg);
				} else if (response.getStateCode() == HttpUtil.CODE_FAIL) {
					ErrorResponse errorResponse = CommonApi
							.toErrorResponse(response.getResponse());
					String msgAlert = errorResponse.getMessage();
					Message msg = new Message();
					msg.what = MSG_ALER;
					msg.obj = msgAlert;
					uiHandler.sendMessage(msg);
				}

				Message msg = new Message();
				msg.what = ListViewSearch.MSG_DISMIS;
				msg.obj = getResources().getString(R.string.searcging);
				uiHandler.sendMessage(msg);
			}
		}.start();

	}

	public SearchView getSearchView() {
		return searchView;
	}

	public void setSearchView(SearchView searchView) {
		this.searchView = searchView;
	}

	public class UiHandler extends Handler {
		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			int what = msg.what;
			switch (what) {
			case MSG_NOT_FOUND:
				List<SearchResult> r = new ArrayList<SearchResult>();
				SearchResult searchResult = new SearchResult();
				searchResult.setType(0);
				r.add(searchResult);
				searchAdapter.setResults(r);
				break;

			case MSG_SHOW_RESULT:

				List<SearchResult> results = new ArrayList<SearchResult>();
				FriendState friendState = (FriendState) msg.obj;
				Friend friend = new Friend();
				friend.setName(friendState.getName());
				friend.setId(Integer.parseInt(friendState.getUserId()));
				friend.setCellPhone1(friendState.getPhoneNumber());
				if (friendState.getIsAlreadyFriend() == 0) {
					SearchResult result = new SearchResult();
					result.setType(2);
					result.setFriend(friend);
					results.add(result);
					searchAdapter.setResults(results);
				} else {
					SearchResult result = new SearchResult();
					result.setType(1);
					result.setFriend(friend);
					results.add(result);
					searchAdapter.setResults(results);
				}
				break;
			case MSG_ALER:
				String alert = (String) msg.obj;
				Toast.makeText(activity, alert, Toast.LENGTH_LONG).show();
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

	public FriendSearchAdapter getSearchAdapter() {
		return searchAdapter;
	}

	public void setSearchAdapter(FriendSearchAdapter searchAdapter) {
		this.searchAdapter = searchAdapter;
		this.setAdapter(searchAdapter);
	}

	public com.widget.SearchView getMySearchView() {
		return mySearchView;
	}

	public void setMySearchView(com.widget.SearchView s) {
		this.mySearchView = s;
		mySearchView.getSearchBtn().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String query = mySearchView.getContent();
				Log.e("debug", "name:" + user.getEmail());
				if (query.equals(user.getEmail())) {
					Toast.makeText(
							activity,
							activity.getString(R.string.cannot_add_self_friend),
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (query.equals("")) {
					Toast.makeText(activity,
							activity.getString(R.string.must_input_email),
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (checkEmail(query) == false) {
					Toast.makeText(activity,
							activity.getString(R.string.alery_mail_err),
							Toast.LENGTH_SHORT).show();
					return;
				}
				queryUser(query);
				Message msg = new Message();
				msg.what = ListViewSearch.MSG_LODING;
				uiHandler.sendMessage(msg);

			}
		});
	}

	public boolean checkEmail(String mail) {
		Pattern p = Pattern
				.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher m = p.matcher(mail);
		return m.find();
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public UiHandler getUiHandler() {
		return uiHandler;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
