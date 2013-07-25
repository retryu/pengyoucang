package com.application;

import com.db.OrmDateBaseHelper;
import com.db.dao.interfaze.MsgDao;

import android.app.Application;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

public class CommonApplication extends Application {
	private  OrmDateBaseHelper ormDateBaseHelper ; 
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.e("debug", "commonApplication  is  oncreate");
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		ormDateBaseHelper = new OrmDateBaseHelper(this, "cloud_contact", null,
				1);
		 
 
	}
	
	public OrmDateBaseHelper getOrmDateBaseHelper() {
		return ormDateBaseHelper;
	}

	public void setOrmDateBaseHelper(OrmDateBaseHelper ormDateBaseHelper) {
		this.ormDateBaseHelper = ormDateBaseHelper;
	}
	

}
