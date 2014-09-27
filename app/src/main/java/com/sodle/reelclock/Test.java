package com.sodle.reelclock;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;

public class Test extends Activity {

    ReelView hour;
    ReelView min;
    ReelView sec;

    double hours = 0;
    double mins = 0;
    double secs = 0;

    int timerDelay = 10;

    Handler timeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 101) {
                Calendar c = Calendar.getInstance();

                secs = c.get(Calendar.SECOND) + ((double) c.get(Calendar.MILLISECOND) / 1000);
                mins = c.get(Calendar.MINUTE) + ((double) c.get(Calendar.SECOND) / 60);
                hours = c.get(Calendar.HOUR_OF_DAY) + ((double) c.get(Calendar.MINUTE) / 60);

                hour.setValue(hours);
                min.setValue(mins);
                sec.setValue(secs);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        hour = (ReelView) findViewById(R.id.hour);
        min = (ReelView) findViewById(R.id.minute);
        sec = (ReelView) findViewById(R.id.second);

        hour.setMax(24);
        min.setMax(60);
        sec.setMax(60);

        min.setNumMinorTicks(1);
        sec.setNumMinorTicks(0);

        Thread updateTime = new Thread() {
            @Override
            public void run() {
                while (true) {
                    Message m = new Message();
                    m.what = 101;
                    timeHandler.sendMessage(m);

                    try {
                        sleep(timerDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        updateTime.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.test, menu);
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
}
