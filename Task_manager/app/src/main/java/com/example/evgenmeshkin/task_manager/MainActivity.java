package com.example.evgenmeshkin.task_manager;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    public static TextView rez = null;
    public static final int requestF = 0;
    String Text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rez = (TextView) findViewById(R.id.rez);
       // if (("").equals((getIntent().getExtras().getString("textF")))){
       //     rez.setText(""); }
      //     else
      try {
        rez.setText(String.valueOf(getIntent().getExtras().getString("textF")));}
      catch (NullPointerException  e) {rez.setText("");}
      }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onFinderClick(View view) {
        startActivity(new Intent(this, FindActivity.class));
    }
    public void onListVClick(View view) {
        startActivity(new Intent(this, ListviewActivity.class));
    }
    public void onGoogleClick(View view) {
        startActivity(new Intent(this, GoogleActivity.class));
    }
    public void onVkClick(View view) {
        startActivity(new Intent(this, VkActivity.class));
    }
}
