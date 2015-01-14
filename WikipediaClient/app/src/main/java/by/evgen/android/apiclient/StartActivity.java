package by.evgen.android.apiclient;

        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v7.app.ActionBarActivity;

/**
 * Created by User on 07.10.2014.
 */
//TODO refactoring
public class StartActivity extends ActionBarActivity {
    public static final int requestL = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Authorized.isLogged()) {
            startMainActivity();
        } else {
            startActivityForResult(new Intent(this, VkLoginActivity.class), requestL);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestL && resultCode == RESULT_OK) {
            startMainActivity();
        } else {
            finish();
        }
    }

    private void startMainActivity() {
        startActivity(new Intent(this, WikiActivity.class));
        finish();
    }

}