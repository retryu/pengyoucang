package com.db.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.db.dao.interfaze.MsgDao;
import com.db.model.Message;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class MsgDaoImpl extends BaseDaoImpl<Message, String> implements MsgDao {

	public MsgDaoImpl(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, Message.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void readed(Message msg) {
		msg.setReaded(true);
		try {
			update(msg);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<Message> getUnRead(Message msg) {
		List<Message> msgs = null;
		try {
			msgs = queryForEq("readed", false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msgs;
	}

	public List<Message> getMessages() {
		List<Message> msgs = null;
		try {
			QueryBuilder<Message, String> qb = queryBuilder().orderBy("readed",
					true);
			PreparedQuery<Message> pq = qb.prepare();
			msgs = query(pq);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msgs;
	}

	@Override
	public List<Message> getNotifyMessages() {
		List<Message> msgs = null;
		try {
			QueryBuilder<Message, String> qb = queryBuilder();
			qb.where().eq("msg_type", "2").and().eq("readed", false);
			PreparedQuery<Message> pq = qb.prepare();
			msgs = query(pq);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msgs;
	}

	@Override
	public int getNotifyCount() {
		int count = getNotifyMessages().size();
		return count;
	}

	@Override
	public List<Message> getRequestMessages(Boolean readed) {
		List<Message> msgs = null;
		try {
			QueryBuilder<Message, String> qb = queryBuilder();
			qb.where().ne("msg_type", "2").and().eq("readed", readed);
			PreparedQuery<Message> pq = qb.prepare();
			msgs = query(pq);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msgs;
	}

	@Override
	public int getRequetsCount() {
		List<Message>  msgs= getRequestMessages(false);
		if(msgs==null){
			return  0;
		}
		int count =msgs.size();
		return count;
	}

	@Override
	public List<Message> getRequestMessages() {
		List<Message> msgs = null;
		try {
			QueryBuilder<Message, String> qb = queryBuilder();
			qb.where().ne("msg_type", "2");
			PreparedQuery<Message> pq = qb.prepare();
			msgs = query(pq);
		} catch (SQLException e) {  
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msgs;
	}

}
