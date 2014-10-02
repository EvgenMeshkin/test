package com.example.testmeteo.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.testmeteo.R;
import com.example.testmeteo.WeatherHttpClient;
import com.example.testmeteo.model.DayForecast;

public class DayForecastFragment  extends Fragment{
	private DayForecast dayForecast;
	private ImageView iconWeather;
	
	public DayForecastFragment() {}
	
	public void setForecast(DayForecast dayForecast) {
		
		this.dayForecast = dayForecast;
		
	}

	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dayforecast_fragment, container, false);
		
		TextView tempView = (TextView) v.findViewById(R.id.tempForecast);
		TextView descView = (TextView) v.findViewById(R.id.skydescForecast);
		tempView.setText( (int) (dayForecast.forecastTemp.min - 275.15) + "-" + (int) (dayForecast.forecastTemp.max - 275.15) );
		descView.setText(dayForecast.weather.currentCondition.getDescr());
		iconWeather = (ImageView) v.findViewById(R.id.forCondIcon);
		// Now we retrieve the weather icon
		JSONIconWeatherTask task = new JSONIconWeatherTask();
		task.execute(new String[]{dayForecast.weather.currentCondition.getIcon()});
		
		return v;
	}

	
	
	private class JSONIconWeatherTask extends AsyncTask<String, Void, Bitmap> {
		
		@Override
		protected Bitmap doInBackground(String... params) {
			
			Bitmap data = null;
			
			try {
				
				// Let's retrieve the icon
				data = ( (new WeatherHttpClient()).getImage(params[0]));
				
			} catch (Exception e) {				
				e.printStackTrace();
			}
			
			return data;
	}
		
		
		
		
	@Override
		protected void onPostExecute(Bitmap data) {			
			super.onPostExecute(data);
			
			if (data != null) {
			iconWeather.setImageBitmap(data);
			}
		}



  }
}
