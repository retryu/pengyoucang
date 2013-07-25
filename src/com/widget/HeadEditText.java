package com.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hengtiansoft.cloudcontact.R;

/**
 * @author retryu E-mail:ruanchenyugood@gmail.com
 * @version create Time��2013-6-21 ����02:11:59 file declare:
 */
public class HeadEditText extends RelativeLayout {

	private Context context;
	private TextView tvHead;
	private EditText etCOntent;
	private ImageView imgTag;
	LayoutInflater layoutInflater;
	private boolean needPass;
	private String name;
	int tagImgRes;
	int contextColor;
	boolean isNumber;

	public HeadEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		name = "";
		TypedArray a = context
				.obtainStyledAttributes(attrs, R.styleable.MyEdit);
		name = a.getString(R.styleable.MyEdit_name);
		tagImgRes = a.getResourceId(R.styleable.MyEdit_tag_image, -1);
		needPass = a.getBoolean(R.styleable.MyEdit_password, false);
		contextColor = a.getColor(R.styleable.MyEdit_context_color, -1);
		isNumber = a.getBoolean(R.styleable.MyEdit_isNumber, false);
		init(context);
	}

	public HeadEditText(Context context) {
		super(context);
		name = "";
		init(context);
	}

	public void init(Context c) {
		context = c;
		layoutInflater = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.layout_head_edittext, null);
		tvHead = (TextView) view.findViewById(R.id.Tv_Head);
		etCOntent = (EditText) view.findViewById(R.id.ET_Content);
		if (isNumber == true) {
			etCOntent.setInputType(InputType.TYPE_CLASS_NUMBER);
		}
		etCOntent.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
				30) });
		imgTag = (ImageView) view.findViewById(R.id.Img_Tag);
		if (tagImgRes != -1) {
			imgTag.setBackgroundResource(tagImgRes);
		}
		if (needPass) {
			etCOntent
					.setTransformationMethod(new PasswordTransformationMethod());
		}
		if (contextColor != -1) {
			etCOntent.setTextColor(contextColor);
		}
		tvHead.setText(name);
		addView(view);

	}

	public void setContent(String content) {
		etCOntent.setText(content);
	}

	public String getContent() {
		String content = etCOntent.getText().toString();
		return content;
	}

	public void setOnFouceChange(OnFocusChangeListener onFocusChangeListener) {
		etCOntent.setOnFocusChangeListener(onFocusChangeListener);
	}

	public void addTextWatche(TextWatcher tw) {
		etCOntent.addTextChangedListener(tw);
	}

}
