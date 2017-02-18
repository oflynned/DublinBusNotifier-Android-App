package com.syzible.dublinnotifier;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.syzible.dublinnotifier.networking.NetworkCallback;
import com.syzible.dublinnotifier.networking.ReqJSONObject;
import com.syzible.dublinnotifier.objects.Result;
import com.syzible.dublinnotifier.objects.Stop;
import com.syzible.dublinnotifier.tools.Constants;
import com.syzible.dublinnotifier.ui.CardAdapter;
import com.syzible.dublinnotifier.ui.FilterDialog;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NetworkCallback<JSONObject> {

    private static final String ROUTE = "79A";
    private static final String DESTINATION = "Aston Quay";

    private RecyclerView recyclerView;
    private String stopId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TODO implement dialogue box here for notifications
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FilterDialog filterDialog = new FilterDialog();
                filterDialog.setFilterListener(new FilterDialog.FilterListener() {
                            @Override
                            public void onFilter() {
                                stopId = filterDialog.getStopId();
                                loadData(stopId);
                            }
                        })
                        .show(getSupportFragmentManager(), "Filter");
            }
        });

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(stopId);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.result_holder);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        RecyclerView.Adapter adapter = new CardAdapter(new Result("Loading ..."));
        recyclerView.setAdapter(adapter);

        stopId = Constants.CENTRAL_BANK_STOP;
        loadData(stopId);
    }

    public void loadData(String stopid) {
        String url = Constants.ENDPOINT + "?stopid=" + stopid + "&format=json";
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

    @Override
    public void onSuccess(JSONObject object) {
        Result result = new Result(object);
        RecyclerView.Adapter adapter = new CardAdapter(result);
        recyclerView.setAdapter(adapter);
    }

    private static Result filterResults(Result result, String route, String terminus) {
        ArrayList<Stop> filteredStops = new ArrayList<>();

        if (result.getStops().size() > 0) {
            for (Stop stop : result.getStops()) {
                if (result.shouldFilterRoute() && stop.getRoute().equals(route))
                        filteredStops.add(stop);

                if (result.shouldFilterTerminus() && stop.getDestination().equals(terminus))
                        filteredStops.add(stop);
            }
        }

        return new Result(filteredStops);
    }

    @Override
    public void onFailure() {
        Toast.makeText(getApplicationContext(), "No results", Toast.LENGTH_SHORT).show();
    }
}
