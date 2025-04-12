package com.example.pricestracker.ui.chart;

import com.example.pricestracker.models.Price;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class PriceChartHelper {
    public static void setupChart(LineChart chart) {
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    }

    public static void updateChart(LineChart chart, List<Price> prices) {
        ArrayList<Entry> entries = new ArrayList<>();

        for (int i = 0; i < prices.size(); i++) {
            Price price = prices.get(i);
            entries.add(new Entry(i, (float) price.getPrice()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Price Trend");
        dataSet.setColor(0xFF3F51B5);
        dataSet.setValueTextColor(0xFF212121);
        dataSet.setLineWidth(2f);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();
    }
}