package com.syzible.dublinnotifier.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.syzible.dublinnotifier.R;
import com.syzible.dublinnotifier.networking.NetworkCallback;
import com.syzible.dublinnotifier.objects.Result;
import com.syzible.dublinnotifier.tools.Manager;
import com.syzible.dublinnotifier.ui.CardAdapter;

import org.json.JSONObject;

/**
 * Created by ed on 20/02/2017.
 */

public class CardsFragment extends Fragment implements NetworkCallback<JSONObject> {
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Manager.getInstance().loadData(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cards, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Manager.getInstance().createFilterDialog(getFragmentManager(), CardsFragment.this);
            }
        });

        final SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) view.findViewById(R.id.card_container_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Manager.getInstance().loadData(CardsFragment.this);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.result_holder);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        RecyclerView.Adapter adapter = new CardAdapter(new Result("Loading ..."));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onSuccess(JSONObject object) {
        Result result = new Result(object);
        RecyclerView.Adapter adapter = new CardAdapter(result);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onFailure() {
        Toast.makeText(getContext(), "No results", Toast.LENGTH_SHORT).show();
    }
}
