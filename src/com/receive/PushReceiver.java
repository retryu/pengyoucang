package com.receive;

import com.activity.MainActivity;
import com.activity.friend.InfoUpdateActivity;
import com.activity.message.MessageActivity;
import com.db.model.Message;
import com.http.MessageApi;
import com.util.PushUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

public class PushReceiver extends BroadcastReceiver {
	private static final String TAG = "MyReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "onReceive - " + intent.getAction() + ", extras: "
				+ printBundle(bundle));

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			// send the Registration Id to your server...
		} else if (JPushInterface.ACTION_UNREGISTER.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			// send the UnRegistration Id to your server...
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {

			String msgJson = bundle.getString(PushUtil.PUSH_DATA_EXTRA);
			int msgType = MessageApi.getMessageType(msgJson);
			Log.e("debug", "msgType:" + msgType + "  msgJson:" + msgJson);
			Intent i = null;
			if (msgType == 1) {
				i = new Intent(context, MessageActivity.class);
				bundle.putInt("type", MessageActivity.MSG_RESPONSE);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
			if (msgType == 0) {
				i = new Intent(context, MessageActivity.class);
				bundle.putInt("type", MessageActivity.MSG_UPDATE);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
			if (msgType == 2) {
				i = new Intent(context, InfoUpdateActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra("msg", msgJson);

			}

			i.putExtras(bundle);

			context.startActivity(i);

		} else {
			Log.d(TAG, "Unhandled intent - " + intent.getAction());
		}
	}

	// ��ӡ���е� intent extra ���
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

}
