package com.syzible.dublinnotifier;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.syzible.dublinnotifier.networking.NetworkCallback;
import com.syzible.dublinnotifier.networking.ReqJSONObject;
import com.syzible.dublinnotifier.objects.Stop;
import com.syzible.dublinnotifier.tools.Constants;
import com.syzible.dublinnotifier.objects.Result;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NetworkCallback<JSONObject> {

    private Result result;
    private TextView textView;
    private static final String ROUTE = "27";
    private static final String DESTINATION = "Eden Quay";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        textView = (TextView) findViewById(R.id.information);

        String url = Constants.ENDPOINT + "?stopid=" + Constants.CENTRAL_BANK_STOP + "&format=json";
        new ReqJSONObject(this, url).execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSuccess(JSONObject object) {
        //this.result = new Result(object);
        //this.result = new Result(object, ROUTE);
        result = new Result(object, ROUTE, DESTINATION);
        String timeTo = "";

        if(result.getStops().size() > 0) {
            timeTo += result.getStops().size() + " buses for stop " + result.getStopId();

            for (Stop stop : result.getStops()) {
                System.out.println(stop.getDueTime() + " mins, " + stop.getRoute());
            }

            if (result.shouldFilterRoute())
                timeTo += " / bus " + result.getRouteFilter();

            if(result.shouldFilterTerminus())
                timeTo += " / constrained terminus " + result.getTerminusFilter();

            timeTo += " with the time to next being in " + result.getStops().get(0).getDueTime() +
                    " min(s) towards " + result.getStops().get(0).getDestination() + "\n\n";

            for (Stop stop : result.getStops())
                timeTo += stop.getDueTime() + " mins / route " + stop.getRoute() + " / " + stop.getDestination() + "\n";
        } else {
            timeTo = "No buses forecast for this search. Perhaps change your constraints?";
        }


        textView.setText(timeTo);
    }

    @Override
    public void onFailure() {
        Toast.makeText(getApplicationContext(), "No results", Toast.LENGTH_SHORT).show();
    }
}
