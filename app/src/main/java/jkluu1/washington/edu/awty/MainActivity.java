package jkluu1.washington.edu.awty;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;


public class MainActivity extends ActionBarActivity {
    private boolean start;
    private String message;
    private String phoneNum;
    private int minutes;

    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = false;  // Assumes messages are not being sent
    }

    // OnClick()
    public void submit(View v) {

        if (!start) {
            EditText messageText = (EditText) findViewById(R.id.messageView);
            EditText phoneNumText = (EditText) findViewById(R.id.phoneNumView);
            EditText minutesText = (EditText) findViewById(R.id.minutesView);

            message = messageText.getText().toString();
            phoneNum = phoneNumText.getText().toString();
            minutes = Integer.parseInt(minutesText.getText().toString());

            // Checks that user inputs are valid and sends broadcast
            if (!message.equals("") && !phoneNum.equals("") && minutes > 0) {
                // Send user inputs to the receiver
                Intent intent = new Intent(MainActivity.this, Receiver.class);
                intent.putExtra("message", message);
                intent.putExtra("phoneNum", phoneNum);
                // intent.putExtra("minutes", minutes);

                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                Button btn = (Button) findViewById(R.id.button);
                btn.setText("Stop");
                start = true;
                Log.d("test", "before method call");
                start();

            }
        } else {
            Button btn = (Button) findViewById(R.id.button);
            btn.setText("Start");
            start = false;

            stop();
        }
    }

    // Sets an alarm for the given amount of minutes
    public void start() {
        Log.d("test", "start begin");
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        int interval = 1000 * 60 * minutes;

        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                interval, pendingIntent);
    }

    // Stops the alarm
    public void stop() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
