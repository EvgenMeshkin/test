package com.example.testmeteo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ActivityTwo extends Activity implements OnClickListener {
	Button btnActTwoo;	
	Button btnkor;	
	Button btnid;	
	EditText text;
	EditText text2;
	EditText text3;
	EditText text4;
	public static int  Per = 0;
	public static String s;
	
@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.two);
		Per = 0;
		s = MainActivity.city;
		text = (EditText) findViewById(R.id.editText1);
		text2 = (EditText) findViewById(R.id.editText2);
		text3 = (EditText) findViewById(R.id.editText3);
		text4 = (EditText) findViewById(R.id.editText4);
		btnActTwoo = (Button) findViewById(R.id.btnActTwoo);
		btnActTwoo.setOnClickListener(this);
		btnkor = (Button) findViewById(R.id.btnkor);
		btnkor.setOnClickListener(this);
		btnid = (Button) findViewById(R.id.btnid);
		btnid.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnActTwoo:

			s = text.getText().toString();
			if (s == "") {
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
				break;
			}

			Per = 1;
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			break;

		case R.id.btnkor:
			s = text2.getText() + "&Lon=" + text4.getText();
			if (s == "&Lon=") {
				Intent intentt = new Intent(this, MainActivity.class);
				startActivity(intentt);
				break;
			}
			Per = 2;
			Intent intentt = new Intent(this, MainActivity.class);
			startActivity(intentt);
			break;
		case R.id.btnid:

			s = text3.getText() + "";
			if (s == "") {
				Intent intentid = new Intent(this, MainActivity.class);
				startActivity(intentid);
				break;
			}
			Per = 3;
			Intent intentid = new Intent(this, MainActivity.class);
			startActivity(intentid);
			break;

		default:
			break;
		}
	}

}
