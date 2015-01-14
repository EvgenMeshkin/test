package by.evgen.android.apiclient;

import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.example.evgen.apiclient.processing.FileProcessor;
import com.example.evgen.apiclient.source.FileDataSource;

//import org.junit.Test;

import java.io.File;
import java.io.InputStream;

/**
 * Created by evgen on 20.10.2014.
 */
public class FileProcessorTest extends ActionBarActivity {
    final String DIR_SD = "MyFiles";
 //   @Test
 //private testCala();
    public void testCalA() throws Exception {
        //getBaseContext();
        //setContentView(R.layout.activity_main);
        InputStream input = getAssets().open("Proba.txt");
        int size = input.available();
        byte[] buffer = new byte[size];
        input.read(buffer);
        input.close();
        String text = new String(buffer);

        File sdPath = Environment.getExternalStorageDirectory();
        FileDataSource get = new FileDataSource();
        File result = get.getResult(sdPath.getAbsolutePath() + "/" + DIR_SD);
        FileProcessor proc = new FileProcessor();
        String processingResult = proc.process(result);





        if (text.equals(processingResult)) {
        Log.d("myLog", "True"); }
        else Log.d("myLog", "False");


    }


}
