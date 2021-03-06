package com.db.dao.interfaze;

import java.util.List;

import com.db.model.Message;
import com.j256.ormlite.dao.Dao;

public interface MsgDao extends Dao<Message, String> {

	public void readed(Message msg);

	public List<Message> getUnRead(Message msg);

	public List<Message> getMessages();

	
	
	public List<Message> getRequestMessages(Boolean  readed);
	public List<Message> getRequestMessages();
	 

	public List<Message> getNotifyMessages();

	public int getNotifyCount();

	
	
	public  int  getRequetsCount();

}
