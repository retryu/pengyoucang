package com.activity.friend;

import java.sql.SQLException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.widget.SearchView;
import com.activity.CommonActivity;
import com.db.OrmDateBaseHelper;
import com.db.dao.interfaze.UserDao;
import com.db.model.User;
import com.hengtiansoft.cloudcontact.R;
import com.widget.ListViewSearch;

/**
 * @author retryu E-mail:ruanchenyugood@gmail.com
 * @version create Time��2013-6-17 ����04:13:48 file declare:
 */
public class SearchFriendActivity extends CommonActivity implements
		OnClickListener {

	private SearchView searchView;
	ListViewSearch lvFriend;
	private FriendSearchAdapter searchAdapter;
	private OrmDateBaseHelper ormDateBaseHelper;
	private UserDao userDao;
	private com.widget.SearchView mySearchView;

	private Button btnBack;
	private  User  user;
	@Override
	public void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.layout_search_friend);
		initWidget();
	}

	private void initWidget() {
		ormDateBaseHelper = getCommoApp().getOrmDateBaseHelper();
		userDao = ormDateBaseHelper.getUserDao();
		try {
			user = userDao.queryForAll().get(0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		btnBack = (Button) findViewById(R.id.Btn_Back);
		btnBack.setOnClickListener(this);
		lvFriend = (ListViewSearch) findViewById(R.id.ListView_Friends);
		lvFriend.setUser(user);
		searchAdapter = new FriendSearchAdapter(this);
		lvFriend.setSearchAdapter(searchAdapter);
		mySearchView = (com.widget.SearchView) findViewById(R.id.SearchView_My);
		lvFriend.setMySearchView(mySearchView);
		lvFriend.setActivity(this);
		searchAdapter.setUiHander(lvFriend.getUiHandler());
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getSupportMenuInflater().inflate(R.menu.search_friend_menu, menu);
		// searchView = (SearchView) menu.findItem(R.id.action_search)
		// .getActionView();
		// lvFriend.setSearchView(searchView);
		// lvFriend.init();
		return true;
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

	public void back() {
		Intent intent = new Intent(this, FriendInfoActivity.class);
		startActivity(intent);
		finish();
	}

}
