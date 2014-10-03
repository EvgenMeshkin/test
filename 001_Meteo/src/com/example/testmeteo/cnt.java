package com.example.testmeteo;

import org.json.JSONException;

import com.example.testmeteo.adapter.DailyForecastPageAdapter;
import com.example.testmeteo.model.WeatherForecast;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class cnt extends FragmentActivity implements OnClickListener {

	Button butshow;
	Button butexit;
	EditText editdn;
	public static int Per = 0;
	public static int Pe = 0;
	public String s;
	String city;
	String lang;
	private static String forecastDaysNum = "5";
	private ViewPager pager;
	private TextView cityT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cnt);

		city = MainActivity.city;
		lang = "ru";
		cityT = (TextView) findViewById(R.id.gor);
		editdn = (EditText) findViewById(R.id.editdn);
		pager = (ViewPager) findViewById(R.id.pager);
		butexit = (Button) findViewById(R.id.butexit);
		butshow = (Button) findViewById(R.id.butshow);
		butexit.setOnClickListener(this);
		butshow.setOnClickListener(this);
		JSONForecastWeatherTask task1 = new JSONForecastWeatherTask();
		task1.execute(new String[] { city, lang, forecastDaysNum });

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.butexit:
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			break;
		case R.id.butshow:
			forecastDaysNum = editdn.getText().toString();
			Pe = 1;
			Intent intentcn = new Intent(this, cnt.class);
			startActivity(intentcn);
			break;

		default:
			break;
		}

	}

	private class JSONForecastWeatherTask extends
			AsyncTask<String, Void, WeatherForecast> {

		@Override
		protected WeatherForecast doInBackground(String... params) {

			String data = ((new WeatherHttpClient()).getForecastWeatherData(
					params[0], params[1], params[2]));
			WeatherForecast forecast = new WeatherForecast();
			try {
				forecast = JSONWeatherParser.getForecastWeather(data);
				System.out.println("Weather [" + forecast + "]");

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return forecast;

		}

		@Override
		protected void onPostExecute(WeatherForecast forecastWeather) {
			super.onPostExecute(forecastWeather);

			DailyForecastPageAdapter adapter = new DailyForecastPageAdapter(
					Integer.parseInt(forecastDaysNum),
					getSupportFragmentManager(), forecastWeather);

			pager.setAdapter(adapter);
			cityT.setText("" + city);
		}
	}

}
