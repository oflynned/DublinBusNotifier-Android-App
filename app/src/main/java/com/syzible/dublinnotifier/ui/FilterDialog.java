package com.syzible.dublinnotifier.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.syzible.dublinnotifier.fragments.MapsFragment;
import com.syzible.dublinnotifier.R;
import com.syzible.dublinnotifier.tools.Manager;

import java.util.ArrayList;
import java.util.Collections;


/**
 * Created by ed on 18/02/2017.
 */

public class FilterDialog extends android.support.v4.app.DialogFragment {
    //private ArrayList<String> stopIdValues = new ArrayList<>();
    //private ArrayList<String> routeValues = new ArrayList<>();
    //private ArrayList<String> terminusValues = new ArrayList<>();

    private AlertDialog.Builder dialog;

    RelativeLayout stopIdLayout;
    TextView stopView;
    EditText stopEditText;
    ImageView mapsImageView;

    RelativeLayout routeLayout;
    TextView routeView;
    Spinner routeSpinner;

    RelativeLayout terminusLayout;
    TextView terminusView;
    Spinner terminusSpinner;

    RelativeLayout minTimeLayout;
    TextView minTimeView;
    Spinner minTimeSpinner;

    RelativeLayout notifyLayout;
    TextView notifyView;
    CheckBox notifyCheckbox;

    private FilterListener filterListener;

    public FilterDialog setFilterListener(FilterListener filterListener) {
        this.filterListener = filterListener;
        return this;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Filter")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (filterListener != null)
                            if (!getStopId().isEmpty())
                                filterListener.onFilter();
                            else
                                Toast.makeText(getContext(), "Please input a stop ID", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null);

        RelativeLayout outerRelativeLayout = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams outerParams = getMatchParentContent();
        outerRelativeLayout.setLayoutParams(outerParams);

        generateStopIdLayout();
        generateRouteLayout();
        generateTerminusLayout();
        generateMinTimeLayout();
        generateNotifyLayout();

        // adding views to individual containers
        stopIdLayout.addView(stopView);
        stopIdLayout.addView(stopEditText);
        stopIdLayout.addView(mapsImageView);

        /*
        routeLayout.addView(routeView);
        routeLayout.addView(routeSpinner);

        terminusLayout.addView(terminusView);
        terminusLayout.addView(terminusSpinner);

        minTimeLayout.addView(minTimeView);
        minTimeLayout.addView(minTimeSpinner);

        notifyLayout.addView(notifyView);
        notifyLayout.addView(notifyCheckbox);*/

        // adding containers to parent view
        outerRelativeLayout.addView(stopIdLayout);

        /*outerRelativeLayout.addView(routeLayout);
        outerRelativeLayout.addView(terminusLayout);
        outerRelativeLayout.addView(minTimeLayout);
        outerRelativeLayout.addView(notifyLayout);*/

        dialog.setView(outerRelativeLayout);

        return dialog.create();
    }

    private ArrayList<String> generateMinTimeValues() {
        ArrayList<String> minTimeValues = new ArrayList<>();
        minTimeValues.add(0, "-");
        for (int i = 0; i < 35; i += 5)
            minTimeValues.add(String.valueOf(i));
        return minTimeValues;
    }

    private ArrayList<String> generateRouteValues() {
        ArrayList<String> routeValues = new ArrayList<>();
        routeValues.add("46A");
        routeValues.add("145");
        routeValues.add("11");
        routeValues.add("40");
        routeValues.add("79A");
        routeValues.add("54A");

        Collections.sort(routeValues);

        routeValues.add(0, "-");

        return routeValues;
    }

    private ArrayList<String> generateTerminusValues() {
        ArrayList<String> terminusValues = new ArrayList<>();
        terminusValues.add("Ringsend");
        terminusValues.add("Phoenix Park");
        terminusValues.add("Wadelai Park");
        terminusValues.add("Pearse Street");

        Collections.sort(terminusValues);

        terminusValues.add(0, "-");

        return terminusValues;
    }

    private void generateStopIdLayout() {
        // stop id filter - Required! Cannot poll otherwise
        stopIdLayout = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams stopIdParams = getWrapContent();
        stopIdParams.setMargins(getDp(24), 0, getDp(8), 0);
        stopIdLayout.setLayoutParams(stopIdParams);
        stopIdLayout.setId(View.generateViewId());

        stopView = new TextView(getContext());
        RelativeLayout.LayoutParams stopParams = getWrapContent();
        stopParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        stopParams.addRule(RelativeLayout.CENTER_VERTICAL);
        stopView.setLayoutParams(stopParams);
        stopView.setText("Stop ID");
        stopView.setId(View.generateViewId());

        Drawable dr = getResources().getDrawable(R.drawable.ic_place_black_48dp);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        Drawable drawable = new BitmapDrawable(getResources(),
                Bitmap.createScaledBitmap(bitmap, getDp(32), getDp(32), true));

        mapsImageView = new ImageView(getContext());
        RelativeLayout.LayoutParams mapsImageViewParams = getWrapContent();
        mapsImageViewParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        mapsImageViewParams.addRule(RelativeLayout.CENTER_VERTICAL);
        mapsImageView.setLayoutParams(mapsImageViewParams);
        mapsImageView.setImageDrawable(drawable);
        mapsImageView.setId(View.generateViewId());
        mapsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // remove the dialog while the map is being shown
                // replace later with data stored in prefs or manager instance
                Fragment dialogFrag = getFragmentManager().findFragmentByTag("Filter");
                getFragmentManager().beginTransaction().remove(dialogFrag).commit();

                MapsFragment mapsFragment = new MapsFragment();
                Manager.getInstance().changeFragment(mapsFragment, getFragmentManager());
            }
        });

        stopEditText = new EditText(getContext());
        RelativeLayout.LayoutParams stopEditTextParams = getWrapContent();
        stopEditTextParams.addRule(RelativeLayout.LEFT_OF, mapsImageView.getId());
        stopEditText.setLayoutParams(stopEditTextParams);
        stopEditText.setMinEms(4);
        stopEditText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        stopEditText.setText(Manager.getInstance().getStopId());
        stopEditText.setId(View.generateViewId());
    }

    private void generateRouteLayout() {
        // route filter -- optional
        routeLayout = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams routeLayoutParams = getWrapContent();
        routeLayoutParams.setMargins(getDp(24), 0, getDp(8), 0);
        routeLayoutParams.addRule(RelativeLayout.BELOW, stopIdLayout.getId());
        routeLayout.setLayoutParams(routeLayoutParams);
        routeLayout.setId(View.generateViewId());

        routeView = new TextView(getContext());
        RelativeLayout.LayoutParams routeParams = getWrapContent();
        routeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        routeParams.addRule(RelativeLayout.CENTER_VERTICAL);
        routeView.setLayoutParams(routeParams);
        routeView.setText("Route");
        routeView.setId(View.generateViewId());

        routeSpinner = new Spinner(getContext());
        RelativeLayout.LayoutParams routeSpinnerParams = getWrapContent();
        routeSpinnerParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        routeSpinnerParams.addRule(RelativeLayout.CENTER_VERTICAL);
        ArrayAdapter<String> routeAdapter = new ArrayAdapter<>(getContext(),
                R.layout.filter_spinner, generateRouteValues());
        routeSpinner.setAdapter(routeAdapter);
        routeSpinner.setLayoutParams(routeSpinnerParams);
        routeSpinner.setId(View.generateViewId());
    }

    private void generateTerminusLayout() {
        //terminus filter -- optional
        terminusLayout = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams terminusLayoutParams = getWrapContent();
        terminusLayoutParams.setMargins(getDp(24), 0, getDp(8), 0);
        terminusLayoutParams.addRule(RelativeLayout.BELOW, routeLayout.getId());
        terminusLayout.setLayoutParams(terminusLayoutParams);
        terminusLayout.setId(View.generateViewId());

        terminusView = new TextView(getContext());
        RelativeLayout.LayoutParams terminusParams = getWrapContent();
        terminusParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        terminusParams.addRule(RelativeLayout.CENTER_VERTICAL);
        terminusView.setLayoutParams(terminusParams);
        terminusView.setText("Terminus");
        terminusView.setId(View.generateViewId());

        terminusSpinner = new Spinner(getContext());
        RelativeLayout.LayoutParams terminusSpinnerParams = getWrapContent();
        terminusSpinnerParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        terminusSpinnerParams.addRule(RelativeLayout.CENTER_VERTICAL);
        ArrayAdapter<String> terminusAdapter = new ArrayAdapter<>(getContext(),
                R.layout.filter_spinner, generateTerminusValues());
        terminusSpinner.setAdapter(terminusAdapter);
        terminusSpinner.setLayoutParams(terminusSpinnerParams);
        terminusSpinner.setId(View.generateViewId());
    }

    private void generateMinTimeLayout() {
        // min time filter -- optional
        minTimeLayout = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams minTimeLayoutParams = getWrapContent();
        minTimeLayoutParams.setMargins(getDp(24), 0, getDp(8), 0);
        minTimeLayoutParams.addRule(RelativeLayout.BELOW, terminusLayout.getId());
        minTimeLayout.setLayoutParams(minTimeLayoutParams);
        minTimeLayout.setId(View.generateViewId());

        minTimeView = new TextView(getContext());
        RelativeLayout.LayoutParams minTimeParams = getWrapContent();
        minTimeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        minTimeParams.addRule(RelativeLayout.CENTER_VERTICAL);
        minTimeView.setLayoutParams(minTimeParams);
        minTimeView.setText("Min Time to Next Bus");
        minTimeView.setId(View.generateViewId());

        minTimeSpinner = new Spinner(getContext());
        RelativeLayout.LayoutParams minTimeSpinnerParams = getWrapContent();
        minTimeSpinnerParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        minTimeSpinnerParams.addRule(RelativeLayout.CENTER_VERTICAL);
        ArrayAdapter<String> minTimeAdapter = new ArrayAdapter<>(getContext(),
                R.layout.filter_spinner, generateMinTimeValues());
        minTimeSpinner.setAdapter(minTimeAdapter);
        minTimeSpinner.setLayoutParams(minTimeSpinnerParams);
        minTimeSpinner.setId(View.generateViewId());
    }

    private void generateNotifyLayout() {
        // notification checkbox -- optional
        notifyLayout = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams notifyLayoutParams = getWrapContent();
        notifyLayoutParams.setMargins(getDp(24), 0, getDp(8), 0);
        notifyLayoutParams.addRule(RelativeLayout.BELOW, minTimeLayout.getId());
        notifyLayout.setLayoutParams(notifyLayoutParams);
        notifyLayout.setId(View.generateViewId());

        notifyView = new TextView(getContext());
        RelativeLayout.LayoutParams notifyParams = getWrapContent();
        notifyParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        notifyParams.addRule(RelativeLayout.CENTER_VERTICAL);
        notifyView.setLayoutParams(notifyParams);
        notifyView.setText("Notification Update");
        notifyView.setId(View.generateViewId());

        notifyCheckbox = new CheckBox(getContext());
        RelativeLayout.LayoutParams checkboxParams = getWrapContent();
        checkboxParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        checkboxParams.addRule(RelativeLayout.CENTER_VERTICAL);
        checkboxParams.setMargins(0, 0, getDp(8), 0);
        notifyCheckbox.setLayoutParams(checkboxParams);
        notifyCheckbox.setId(View.generateViewId());
    }

    public RelativeLayout.LayoutParams getWrapContent() {
        return new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public RelativeLayout.LayoutParams getMatchParentContent() {
        return new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public int getDp(float pixels) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels,
                getContext().getResources().getDisplayMetrics());
    }

    public String getStopId() {
        return stopEditText.getText().toString().trim();
    }

    public String getRoute() {
        return routeSpinner.getSelectedItem().toString().trim();
    }

    public String getTerminus() {
        return terminusSpinner.getSelectedItem().toString().trim();
    }

    public int getMinTime() {
        return minTimeSpinner.getSelectedItem().toString().equals("-") ? -1 :
                Integer.parseInt(minTimeSpinner.getSelectedItem().toString());
    }

    public boolean shouldNotify() {
        return notifyCheckbox.isChecked();
    }
}
