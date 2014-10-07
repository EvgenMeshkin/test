package com.example.evgenmeshkin.task_manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by User on 07.10.2014.
 */
public class FindActivity extends ActionBarActivity {
    private TextView rez;
    public static EditText Text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        rez = (TextView) findViewById(R.id.rez);
//        rez.setText("");
        try {
            rez.setText(String.valueOf(getIntent().getExtras().getString("textF")));}
        catch (NullPointerException  e) {rez.setText("");}

    }


    public void onHomeClick(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }


    public void onFindClick(View view) {

        Text = (EditText) findViewById(R.id.edit_text);
        if (Text.getText().toString().equals("")) {
        //startActivity(new Intent(this, FindActivity.class));
        //rez.setText("Input error");
            Intent intent = new Intent(this, FindActivity.class);
            intent.putExtra("textF", "Input error"); // в ключ username пихаем текст из первого текстового поля
            startActivity(intent);

        }
        else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("textF", Text.getText().toString()); // в ключ username пихаем текст из первого текстового поля
            startActivity(intent);
                 }
    }


}
