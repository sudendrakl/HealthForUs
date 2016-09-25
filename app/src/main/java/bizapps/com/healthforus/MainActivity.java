package bizapps.com.healthforus;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    TextView tv = (TextView) findViewById(R.id.textview);
  }

  @Override protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    //TODO: intent.getStringExtra("KEY");...do something with it
  }
}
