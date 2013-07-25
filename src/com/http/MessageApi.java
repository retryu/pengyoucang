package com.http;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.util.JsonReader;

import cn.jpush.android.c.s;

import com.db.model.Message;
import com.http.response.CommonResponse;

/**
 * @author retryu E-mail:ruanchenyugood@gmail.com
 * @version create Time��2013-6-10 ����01:54:23 file declare:
 */
public class MessageApi extends CommonApi {
	private static final String MSGS_URL = "v1/message";

	private static final String MSG_URL = "v1/message/request";

	private static final String MSG_RESPONSE_URL = "/v1/message/response";

	public static CommonResponse getMessages() {
		CommonResponse commonResponse = new CommonResponse();
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("access_token", HttpUtil.Token);
			jsonParams.put("user_id", HttpUtil.UsreId);
			System.err.println("params:" + jsonParams.toString());
			commonResponse = HttpUtil.post(MAIN_URL + MSGS_URL, jsonParams);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return commonResponse;
	}

	public static Message getMessageById(String msgId) {
		CommonResponse response = null;
		JSONObject jsonParams = new JSONObject();
		Message msg = null;
		try {
			jsonParams.put("access_token", HttpUtil.Token);
			jsonParams.put("user_id", HttpUtil.UsreId);
			jsonParams.put("message_id", msgId);
			response = HttpUtil.post(MAIN_URL + MSG_URL, jsonParams);
			JSONObject jsonMsg = new JSONObject(response.getResponse())
					.getJSONObject("message");
			msg = toMessage(jsonMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return msg;
	}

	public static List<Message> toMessages(String jsonmsgs) {
		List<Message> msgs = new ArrayList<Message>();
		try {
			JSONObject json = new JSONObject(jsonmsgs);
			JSONArray msgArray = new JSONArray(
					json.getString("requestMessages"));
			for (int i = 0; i < msgArray.length(); i++) {
				JSONObject msgJson = msgArray.getJSONObject(i);
				Message msg = toMessage(msgJson);
				msgs.add(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return msgs;
	}

	public static Message toMessage(JSONObject msgJson) {
		Message msg = new Message();
		try {
			if (msgJson.isNull("message_id") == false) {
				msg.setMsgId(msgJson.getString("message_id"));
			}
			if (msgJson.isNull("message_type") == false) {
				msg.setMsg_type(msgJson.getString("message_type"));
			}
			if (msgJson.isNull("request_user_id") == false) {
				msg.setRequestId(msgJson.getString("request_user_id"));
			}
			if (msgJson.isNull("request_user_name") == false) {
				msg.setRequest_user_name(msgJson.getString("request_user_name"));
			}
			if (msgJson.isNull("response_user_id") == false) {
				msg.setResponse_user_id(msgJson.getString("response_user_id"));
			}
			if (msgJson.isNull("response_user_name") == false) {
				msg.setResponse_user_name(msgJson
						.getString("response_user_name"));
			}
			if (msgJson.isNull("message_state") == false) {
				msg.setMessage_state(msgJson.getString("message_state"));
			}
			if (msgJson.isNull("content") == false) {
				msg.setContent(msgJson.getString("content"));
			}
			if (msgJson.isNull("create_time") == false) {
				msg.setCreate_time(msgJson.getString("create_time"));
			}
			if (msgJson.isNull("request_user_email") == false) {
				msg.setRequest_email(msgJson.getString("request_user_email"));
			}
			if (msgJson.isNull("request_user_phone") == false) {
				msg.setRequest_phone(msgJson.getString("request_user_phone"));
			}
			System.out.println(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return msg;
	}

	public static int getID(String json) {
		int id = -1;
		try {
			JSONObject jsonObj = new JSONObject(json);
			id = jsonObj.getInt("messageID");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}

	public static CommonResponse responseFriend(String msgId, boolean isAgree) {
		CommonResponse response = null;
		JSONObject jsonParam = new JSONObject();
		try {
			jsonParam.put("access_token", HttpUtil.getToken());
			jsonParam.put("user_id", HttpUtil.getUsreId());
			jsonParam.put("message_id", msgId);
			if (isAgree == true) {
				jsonParam.put("is_delete", "0");
			} else {
				jsonParam.put("is_delete", "1");
			}
			response = HttpUtil.post(MAIN_URL + MSG_RESPONSE_URL, jsonParam);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public static Message getPushMessage(String jsonStr) {
		Message msg = new Message();
		try {
			JSONObject json = new JSONObject(jsonStr);
			if (!json.isNull("messageType")) {
				String msgType = json.getString("messageType");
				msg.setMsg_type(msgType);
			}
			if (!json.isNull("msgContent")) {
				String msgContent = json.getString("msgContent");
				msg.setContent(msgContent);
			}
			if (!json.isNull("msgId")) {
				String msgID = json.getString("msgId");
				msg.setMsgId(msgID);
			}
			if (!json.isNull("name")) {
				String name = json.getString("name");
				msg.setRequest_user_name(name);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msg;
	}

	public static int getMessageType(String json) {
		int type = -1;
		try {
			JSONObject typeJson = new JSONObject(json);
			type = typeJson.getInt("messageType");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return type;
	}
}
