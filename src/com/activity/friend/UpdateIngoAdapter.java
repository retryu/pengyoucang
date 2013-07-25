package com.activity.friend;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.db.model.Message;
import com.hengtiansoft.cloudcontact.R;

/**
 * @author retryu E-mail:ruanchenyugood@gmail.com
 * @version create Time：2013-6-28 下午03:10:23 file declare:
 */
public class UpdateIngoAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	List<UpdateInfo> infos;

	public UpdateIngoAdapter(Context context) {
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if (infos == null) {
			return 0;
		} else {
			infos.size();
			return infos.size();
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		UpdateInfo info = infos.get(position);
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.layout_update_info,
					null);

		}
		bindView(convertView, info);

		return convertView;
	}

	public void bindView(View view, final UpdateInfo info) {
		TextView tvName = (TextView) view.findViewById(R.id.Tv_Name);
		final CheckBox ckBox = (CheckBox) view.findViewById(R.id.Ck_Update);
		if (info.getMessage() != null) {
			String name = info.getMessage().getRequest_user_name();
			if (name != null) {
				tvName.setText(name);
			}
		}
		if (info.isCheched() == true) {
			ckBox.setChecked(true);
		} else {
			ckBox.setChecked(false);
		}
		ckBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				check(info, ckBox.isChecked());
			}
		});

	}

	public void check(UpdateInfo info, boolean isChecked) {
		Message msg = info.getMessage();
		info.setCheched(isChecked);
		msg.setReaded(isChecked);
	}

	public void setInfos(List<UpdateInfo> i) {
		this.infos = i;
		notifyDataSetChanged();
	}

	public List<UpdateInfo> getInfos() {
		return infos;
	}

	public void checkAll(boolean  checked) {
		UpdateInfo  info;
		for (int i = 0; i < infos.size(); i++) {
			info=infos.get(i);
			check(info, checked);
		}
		notifyDataSetChanged();

	}
	// public List<UpdateInfo> getCheckInfos(){
	// List<UpdateInfo> checkedInfos=new ArrayList<UpdateInfo>();
	// UpdateInfo checkedInfo;
	// for(int i=0;i<infos.size();i++){
	// checkedInfo=infos.get(i);
	// if(checkedInfo.isCheched()==true){
	// checkedInfos.add(checkedInfo);
	// }
	// }
	// return checkedInfos;
	//
	// }

}
