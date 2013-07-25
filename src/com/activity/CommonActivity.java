package com.activity;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.application.CommonApplication;
import com.db.OrmDateBaseHelper;
import com.umeng.analytics.MobclickAgent;

public class CommonActivity extends SherlockFragmentActivity {

	private OrmDateBaseHelper ormDateBaseHelper;
	private CommonApplication application;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		// ormDateBaseHelper = new OrmDateBaseHelper(this, "cloud_contact",
		// null,
		// 1);
		application = (CommonApplication) getApplication();
		MobclickAgent.setDebugMode(true);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	// public OrmDateBaseHelper getOrmDateBaseHelper() {
	// return ormDateBaseHelper;
	// }
	//
	// public void setOrmDateBaseHelper(OrmDateBaseHelper ormDateBaseHelper) {
	// this.ormDateBaseHelper = ormDateBaseHelper;
	// }
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public CommonApplication getCommoApp() {
		return application;
	}

	public String getResourceString(int id) {
		String str = getResources().getString(id);
		return str;

	}

	public OrmDateBaseHelper getOrmDateBaseHelper() {
		return application.getOrmDateBaseHelper();
	}

}
