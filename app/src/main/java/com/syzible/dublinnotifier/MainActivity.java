package com.syzible.dublinnotifier;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.syzible.dublinnotifier.fragments.CardsFragment;
import com.syzible.dublinnotifier.tools.Manager;

public class MainActivity extends AppCompatActivity {

    // map
    // TODO poll curated list of data for spinners?

    // TODO notifications
    // TODO shared prefs keys for storing the last queried stop
    // TODO update service
    // TODO style colours and animations
    // TODO edge cases


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Manager.getInstance().changeFragment(new CardsFragment(), getSupportFragmentManager());
    }

    @Override
    public void onBackPressed() {
        if(Manager.getInstance().getStage() == 1) {
            this.finish();
        } else {
            Manager.getInstance().removeLastFragment(getSupportFragmentManager());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
