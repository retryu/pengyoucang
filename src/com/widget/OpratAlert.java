package com.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hengtiansoft.cloudcontact.R;

/**
 * @author retryu E-mail:ruanchenyugood@gmail.com
 * @version create Time：2013-7-8 上午10:45:58 file declare:
 */
public class OpratAlert extends Dialog {

	private Button btnOk;
	private Button btnCancel;
	private TextView tvAlert;
	private OpratAlert opratAlert;

	public OpratAlert(Context context) {
		super(context);
		setContentView(R.layout.layout_oprat_dialog);
		setCancelable(false);
		initWidget();
	}

	public void initWidget() {
		opratAlert = this;
		btnCancel = (Button) findViewById(R.id.Btn_Cancel);
		btnOk = (Button) findViewById(R.id.Btn_OK);
		tvAlert = (TextView) findViewById(R.id.Tv_Alert);
		btnOk.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				opratAlert.dismiss();
			}
		});
		btnCancel.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				opratAlert.dismiss();
			}
		});
	}

	public void setBtnOkListener(android.view.View.OnClickListener clickListener) {
		btnOk.setOnClickListener(clickListener);
	}

	public void setBtnCancelListener(
			android.view.View.OnClickListener clickListener) {
		btnCancel.setOnClickListener(clickListener);
	}

	public void setAlert(String alert) {
		if (alert != null) {
			tvAlert.setText(alert);
		}
	}

	public void setBtnCancalVisiable(boolean isVisiable) {
		if (isVisiable == true) {
			btnCancel.setVisibility(View.VISIBLE);
		} else {
			btnCancel.setVisibility(View.INVISIBLE);
		}
	}

	public void setBtnOkVisisable(boolean isVisiable) {
		if (isVisiable == true) {
			btnOk.setVisibility(View.VISIBLE);
		} else {
			btnOk.setVisibility(View.INVISIBLE);
		}
	}

}
