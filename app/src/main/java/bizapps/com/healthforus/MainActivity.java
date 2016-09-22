package bizapps.com.healthforus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    //TODO: intent.getStringExtra("KEY");...do something with it
  }
}
