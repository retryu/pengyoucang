package com.activity.friend;

import com.db.model.Message;

/**
 * @author retryu E-mail:ruanchenyugood@gmail.com
 * @version create Time：2013-6-28 下午03:11:20 file declare:
 */
public class UpdateInfo {
	private boolean isCheched;
	private Message message;

	public boolean isCheched() {
		return isCheched;
	}

	public void setCheched(boolean isCheched) {
		this.isCheched = isCheched;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	
	
}
