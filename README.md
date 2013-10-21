	private void showChannelIds() {
		String appId = null;
		String channelId = null;
		String clientId = null;

		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		appId = sp.getString("appid", "");
		channelId = sp.getString("channel_id", "");
		clientId = sp.getString("user_id", "");
		
		Resources resource = this.getResources();
		String pkgName = this.getPackageName();
		infoText = (TextView) findViewById(resource.getIdentifier("text", "id", pkgName));

		String content = "\tApp ID: " + appId + "\n\tChannel ID: " + channelId
				+ "\n\tUser ID: " + clientId + "\n\t";
		if (infoText != null) {
			infoText.setText(content);
			infoText.invalidate();
		}
	}
