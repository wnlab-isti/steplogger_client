package it.cnr.isti.steploggerclient;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class StepLoggerClientActivity extends AppCompatActivity {

    private Intent dummyIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steploggerclient);
        setButtons(isMyServiceRunning(StepLoggerClientService.class));
        dummyIntent = new Intent(this, StepLoggerClientService.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_steploggerclient, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startRemoteConnection(View view) {
        // Start the background service
        startService(dummyIntent);
        setButtons(true);
    }

    public void stopRemoteConnection(View view) {
        stopService(dummyIntent);
        setButtons(false);
        dummyIntent = null;
    }

    private void setButtons(Boolean intentRunning) {
        // Set button visibility
        Button startRemoteConnection = (Button) findViewById(R.id.buttonStart);
        Button stopRemoteConnection = (Button) findViewById(R.id.buttonStop);

        startRemoteConnection.setEnabled(!intentRunning);
        stopRemoteConnection.setEnabled(intentRunning);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service :
                manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if(dummyIntent != null){
            // If service is still running Put the activity in background
            moveTaskToBack(true);
            return;
        } else {
            // else close the app and free memory
            super.onBackPressed();
        }
    }
}