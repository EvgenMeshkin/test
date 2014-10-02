package com.example.testmeteo;

import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testmeteo.model.Weather;


public class MainActivity extends Activity implements OnClickListener{

	public static final String TEXT = "Text";

	private TextView cityText;
	private TextView condDescr;
	private TextView temp;
	private TextView press;
	private TextView windSpeed;
	private TextView windDeg;
	private TextView date;
	private TextView hum;
	private TextView par;
	private TextView obl;
	private ImageView imgView;
	Button btnActTwo;
	Button btncnt;
	String data;
	public static String city;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		city = "Nowe Ostrowy";

		cityText = (TextView) findViewById(R.id.cityText);
		condDescr = (TextView) findViewById(R.id.condDescr);
		temp = (TextView) findViewById(R.id.temp);
		par = (TextView) findViewById(R.id.par);
		hum = (TextView) findViewById(R.id.hum);
		press = (TextView) findViewById(R.id.press);
		windSpeed = (TextView) findViewById(R.id.windSpeed);
		windDeg = (TextView) findViewById(R.id.windDeg);
		date = (TextView) findViewById(R.id.date);
		imgView = (ImageView) findViewById(R.id.condIcon);

		obl = (TextView) findViewById(R.id.obl);
		btnActTwo = (Button) findViewById(R.id.btnActTwo);
		btncnt = (Button) findViewById(R.id.btncnt);
		btnActTwo.setOnClickListener(this);
		btncnt.setOnClickListener(this);
		if (ActivityTwo.Per != 0) {

			city = ActivityTwo.s;
		}

		JSONWeatherTask task = new JSONWeatherTask();
		task.execute(new String[] { city });
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnActTwo:
			ActivityTwo.s = city;
			Intent intent = new Intent(this, ActivityTwo.class);
			startActivity(intent);
			break;
		case R.id.btncnt:
			Intent intentcnt = new Intent(this, cnt.class);
			startActivity(intentcnt);
			break;
		default:
			break;
		}
	}

	private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

		@Override
		protected Weather doInBackground(String... params) {
			Weather weather = new Weather();
			data = ((new WeatherHttpClient()).getWeatherData(params[0]));
			// data = "";

			if (data != null) {
				try {
					weather = JSONWeatherParser.getWeather(data);

					// Let's retrieve the icon

					weather.iconData = ((new WeatherHttpClient())
							.getImage(weather.currentCondition.getIcon()
									.toString()));

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			return weather;

		}

		@Override
		protected void onPostExecute(Weather weather) {
			super.onPostExecute(weather);

			if (weather.iconData != null) {
				Bitmap img = weather.iconData;
				imgView.setImageBitmap(img);
			}

			if (data != null) {
				cityText.setText("" + weather.location.getCity() + ","+ weather.location.getCountry());
				city = "" + weather.location.getCity();
				condDescr.setText("Координаты:  "+ weather.location.getLatitude() + "  "
						+ weather.location.getLongitude());
				temp.setText(""	+ Math.round((weather.temperature.getTemp() - 273.15))+ "' C");
				hum.setText("Влажность:  "+ weather.currentCondition.getHumidity() + "%");
				press.setText("Давление:  "	+ weather.currentCondition.getPressure() + " hPa");
				obl.setText("Облачность:  " + weather.clouds.getPerc() + "%");
				windSpeed.setText("Скорость ветра:  " + weather.wind.getSpeed()	+ " mps");
				windDeg.setText("Направление ветра:  " + weather.wind.getDeg()+ "");
				par.setText("   " + weather.currentCondition.getCondition()	+ "(" + weather.currentCondition.getDescr() + ")");

				String s_unixtime = (String) weather.location.getDate();
				Integer unixtime = 0;
				try {
					unixtime = Integer.parseInt(s_unixtime);
				} catch (NumberFormatException e) {
				}
				SimpleDateFormat formatter = new SimpleDateFormat();
				String datee = formatter.format(new Date(unixtime * 1000L));

				date.setText("Дата:  " + datee + "");

			}
		}

	}
}