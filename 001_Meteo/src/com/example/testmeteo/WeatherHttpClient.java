package com.example.testmeteo;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class WeatherHttpClient {

	private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
	private static String IMG_URL = "http://openweathermap.org/img/w/";
	private static String BASE_FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?mode=json&q=";
	
	
	public String getWeatherData(String location) {
		HttpURLConnection con = null ;
		InputStream is = null;
		if (ActivityTwo.Per == 1) {
			BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
		}
		if (ActivityTwo.Per == 2) {
			BASE_URL = "http://api.openweathermap.org/data/2.5/weather?lat=";
		}
		if (ActivityTwo.Per == 3) {
			BASE_URL = "http://api.openweathermap.org/data/2.5/weather?id=";
		}

		try {
			con = (HttpURLConnection) (new URL(BASE_URL + location + "&lang=ru"))
					.openConnection();
			con.setRequestMethod("GET");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.connect();

			// Let's read the response
			StringBuffer buffer = new StringBuffer();
			is = con.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = br.readLine()) != null)
				buffer.append(line + "\r\n");

			is.close();
			con.disconnect();
			return buffer.toString();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (Throwable t) {
			}
			try {
				con.disconnect();
			} catch (Throwable t) {
			}
		}

		return null;

	}

	public String getForecastWeatherData(String location, String lang,
			String sForecastDayNum) {
		HttpURLConnection con = null;
		InputStream is = null;
		int forecastDayNum = Integer.parseInt(sForecastDayNum);

		try {

			// Forecast
			String url = BASE_FORECAST_URL + location;
			if (lang != null)
				url = url + "&lang=" + lang;

			url = url + "&cnt=" + forecastDayNum;
			con = (HttpURLConnection) (new URL(url)).openConnection();
			con.setRequestMethod("GET");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.connect();

			// Let's read the response
			StringBuffer buffer1 = new StringBuffer();
			is = con.getInputStream();
			BufferedReader br1 = new BufferedReader(new InputStreamReader(is));
			String line1 = null;
			while ((line1 = br1.readLine()) != null)
				buffer1.append(line1 + "\r\n");

			is.close();
			con.disconnect();

			System.out.println("Buffer [" + buffer1.toString() + "]");
			return buffer1.toString();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (Throwable t) {
			}
			try {
				con.disconnect();
			} catch (Throwable t) {
			}
		}

		return null;

	}

	public Bitmap getImage(String code) {
		HttpURLConnection con = null;
		InputStream is = null;
		try {
			con = (HttpURLConnection) (new URL(IMG_URL + code + ".png"))
					.openConnection();
			con.setRequestMethod("GET");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.connect();

			Bitmap image = BitmapFactory.decodeStream((InputStream) new URL(IMG_URL + code + ".png").getContent());
			return image;

		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (Throwable t) {
			}
			try {
				con.disconnect();
			} catch (Throwable t) {
			}
		}

		return null;

	}
}