package by.evgen.android.apiclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import by.evgen.android.apiclient.auth.secure.EncrManager;

/**
 * Created by User on 07.10.2014.
 */
//TODO refactoring
public class LoginActivity extends ActionBarActivity {

    public static final int REQUEST_CODE_VK = 0;

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
    }

    public void onLoginClick(View view) {
        Toast.makeText(this, "implement me", Toast.LENGTH_SHORT).show();
    }

    public void onFragmentsClick(View view) {
        startActivity(new Intent(this, WikiActivity.class));
    }

    private String mSomeSecureString;

    public void onEncrDecrClick(View view) {
        if (mSomeSecureString == null) {
            EditText editText = (EditText) findViewById(R.id.password);
            try {
                mSomeSecureString = EncrManager.encrypt(this, editText.getText().toString());
            } catch (Exception e) {
                //TODO
            }
            Toast.makeText(this, mSomeSecureString, Toast.LENGTH_SHORT).show();
        } else {
            try {
                mSomeSecureString = EncrManager.decrypt(this, mSomeSecureString);
            } catch (Exception e) {
                //TODO
            }
            Toast.makeText(this, mSomeSecureString, Toast.LENGTH_SHORT).show();
            mSomeSecureString = null;
        }

    }

    public void onVkAuthClick(View view) {
        Intent intent = new Intent(this, VkLoginActivity.class);
        startActivityForResult(intent, REQUEST_CODE_VK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_VK && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}