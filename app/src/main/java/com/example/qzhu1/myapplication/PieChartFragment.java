package com.example.qzhu1.myapplication;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class PieChartFragment extends SimpleFragment {
    final Firebase ref = new Firebase("https://knotedb.firebaseio.com");
    float[] values = new float[2];
    public static Fragment newInstance() {
        return new PieChartFragment();
    }

    private PieChart mChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_simple_pie, container, false);

        mChart = (PieChart) v.findViewById(R.id.pieChart1);
        mChart.setDescription("");

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Bold.ttf");

        mChart.setCenterTextTypeface(tf);
        mChart.setCenterText(generateCenterText());
        mChart.setCenterTextSize(10f);
        mChart.setCenterTextTypeface(tf);

        // radius of the center hole in percent of maximum radius
        mChart.setHoleRadius(55f);
        mChart.setTransparentCircleRadius(60f);

        Legend l = mChart.getLegend();
        l.setPosition(LegendPosition.RIGHT_OF_CHART);

        // Total owe
        Query qOwe = ref.child("bill").orderByChild("owe").equalTo(KnotsApplication.email);
        qOwe.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                values[0] = 0;
                HashMap<?, ?> hashMap = (HashMap) snapshot.getValue();
                if(hashMap!=null) {
                    Set set = hashMap.entrySet();
                    Iterator i = set.iterator();
                    while (i.hasNext()) {
                        Map.Entry me = (Map.Entry) i.next();
                        HashMap<?, ?> e = (HashMap) me.getValue();
                        String n = (String) e.get("amount");
                        values[0] += Float.parseFloat(n);
                    }
                    mChart.setData(getPieData());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        // Total pay
        Query qPay = ref.child("bill").orderByChild("pay").equalTo(KnotsApplication.email);
        qPay.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                values[1] = 0;
                HashMap<?, ?> hashMap = (HashMap) snapshot.getValue();
                if(hashMap!=null) {
                    Set set = hashMap.entrySet();
                    Iterator i = set.iterator();
                    while (i.hasNext()) {
                        Map.Entry me = (Map.Entry) i.next();
                        HashMap<?, ?> e = (HashMap) me.getValue();
                        String n = (String) e.get("amount");
                        values[1] += Float.parseFloat(n);
                    }
                    mChart.setData(getPieData());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

//        mChart.setData(getPieData());

        return v;
    }

    private SpannableString generateCenterText() {
        SpannableString s;
        try {
            s = new SpannableString("Balance\n" + KnotsApplication.email.substring(0, KnotsApplication.email.indexOf("@")));
        }catch (Exception e){
            System.out.println("==================== email null ====================");
            e.printStackTrace();
            s = new SpannableString("Balance\nUser");
        }finally {
            //continue
        }
        s.setSpan(new RelativeSizeSpan(2f), 0, 8, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 8, s.length(), 0);
        return s;
    }

    public static final int[] MY_COLORS = {
            Color.rgb(217, 80, 138), Color.rgb(106, 167, 134), Color.rgb(254, 247, 120),
            Color.rgb(106, 167, 134), Color.rgb(53, 194, 209)
    };

    private PieData getPieData() {

        ArrayList<Entry> entries1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add("Owe");
        xVals.add("Pay");

        entries1.add(new Entry(values[0], 0));
        entries1.add(new Entry(values[1], 1));

        PieDataSet ds1 = new PieDataSet(entries1, "Total Balance");
        ds1.setColors(MY_COLORS);
        ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.WHITE);
        ds1.setValueTextSize(12f);

        PieData d = new PieData(xVals, ds1);
        d.setValueTypeface(Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf"));

        return d;
    }
}
