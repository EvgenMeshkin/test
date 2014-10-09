package com.example.evgenmeshkin.task_manager;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by User on 07.10.2014.
 */
public class LoginActivity extends ActionBarActivity implements TextView.OnEditorActionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button cancelBtn = (Button) findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        EditText editGo = (EditText) findViewById(R.id.password);
        editGo.setOnEditorActionListener(this);
    }

    public void onLoginFinish(View view) {
        finish();
    }

    public void onLoginClick(View view) {
        Authorized.setLogged(true);
        setResult(RESULT_OK);
        finish();
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
     //   if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            // обрабатываем нажатие кнопки поиска
     //   }
        if (actionId == EditorInfo.IME_ACTION_GO) {
            // обрабатываем нажание кнопки GO
           Authorized.setLogged(true);
           setResult(RESULT_OK);
            finish();

            return true;
        }

        return false;

    }


}
