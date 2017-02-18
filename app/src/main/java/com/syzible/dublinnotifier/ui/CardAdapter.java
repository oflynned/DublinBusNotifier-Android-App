package com.syzible.dublinnotifier.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.syzible.dublinnotifier.R;
import com.syzible.dublinnotifier.objects.Result;
import com.syzible.dublinnotifier.objects.Stop;

/**
 * Created by ed on 30/01/2017.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.Holder> {
    private Result result;

    public CardAdapter(Result result) {
        this.result = result;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        Stop stop = result.getStops().get(position);
        holder.tv_route.setText("Route " + stop.getRoute());

        String dueTime;
        if(stop.getDueTime() == 0)
            dueTime = "Due";
        else if(stop.getDueTime() == 1)
            dueTime = stop.getDueTime() + " min";
        else dueTime = stop.getDueTime() + " mins";

        holder.tv_timeDue.setText(dueTime);
        holder.tv_terminus.setText(stop.getDestination());
        holder.tv_stopId.setText(result.getStopId());
    }

    @Override
    public int getItemCount() {
        return result.getStops().size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView tv_route, tv_timeDue, tv_terminus, tv_stopId;

        Holder(View view) {
            super(view);

            tv_route = (TextView) view.findViewById(R.id.route);
            tv_timeDue = (TextView) view.findViewById(R.id.due);
            tv_terminus = (TextView) view.findViewById(R.id.terminus);
            tv_stopId = (TextView) view.findViewById(R.id.stop_id);
        }
    }
}
