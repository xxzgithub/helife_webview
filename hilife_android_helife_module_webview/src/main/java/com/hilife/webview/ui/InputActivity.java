package com.hilife.webview.ui;

import android.app.Activity;
import android.content.Intent;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.dajia.android.base.util.StringUtil;
import com.hilife.view.main.base.DajiaBaseActivity;
import com.hilife.view.R;

public class InputActivity extends DajiaBaseActivity {
	private EditText webview_input;
	private RelativeLayout input_l;
	private Button inputButton;
	@Override
	protected void loadLayout() {
		setContentView(R.layout.view_input_2);
	}

	@Override
	protected void findView() {
		webview_input = (EditText)findViewById(R.id.input);
		input_l = (RelativeLayout)findViewById(R.id.input_l);
		inputButton = (Button)findViewById(R.id.inputButton);
	}

	@Override
	protected void setListener() {
		input_l.setOnClickListener(this);
		inputButton.setOnClickListener(this);
	}

	@Override
	protected void onClickEvent(View view) {
		switch (view.getId()) {
			case R.id.input_l:
				_return();
				break;
			case R.id.inputButton:
				_return();
				break;
		}
	}

	@Override
	protected void processLogic() {
		Intent intent = getIntent();
		String type = intent.getStringExtra("type");
		if("password".equalsIgnoreCase(type)){
			webview_input.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);
		}else if("number".equalsIgnoreCase(type)){
			webview_input.setInputType(InputType.TYPE_CLASS_NUMBER);
		}else if("tel".equalsIgnoreCase(type)){
			webview_input.setInputType(InputType.TYPE_CLASS_PHONE);
		}
		if("textarea".equalsIgnoreCase(type)){
			webview_input.setMinLines(3);
		}else{
			webview_input.setMaxLines(1);
		}
		String inputValue = intent.getStringExtra("inputValue");
		if(StringUtil.isNotEmpty(inputValue)){
			webview_input.setText(inputValue);
			webview_input.setSelection(inputValue.length());
		}

	}

	@Override
	public String getPageID() {
		return null;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				finish();
				return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void _return(){
		Intent data = getIntent();
		data.putExtra("text", webview_input.getText().toString());
		setResult(Activity.RESULT_OK, data);
		finish();
	}

}
