/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package yokohama.yellow_man.senadroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import yokohama.yellow_man.senadroid.app.CheeseDetailActivity;

public class CheeseListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cheese_list, container, false);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.recyclerview);
        setupRecyclerView(rv);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(
                new SimpleStringRecyclerViewAdapter(getActivity(), getRandomSublist(Cheeses.sCheeseStrings, 30)));
    }

    private List<String> getRandomSublist(String[] array, int amount) {
        ArrayList<String> list = new ArrayList<>(amount);
        Random random = new Random();
        while (list.size() < amount) {
            list.add(array[random.nextInt(array.length)]);
        }
        return list;
    }

    public static class SimpleStringRecyclerViewAdapter extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<String> mValues;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public String mBoundString;

            public final View mView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
            }
        }

        public static class ViewHolder2 extends ViewHolder {
            public final View mView;

            public ViewHolder2(View view) {
                super(view);
                mView = view;
                createBarChart(view);
            }

            public void createBarChart(View view) {
                BarChart barChart = (BarChart) view.findViewById(R.id.bar_chart);

                barChart.getAxisRight().setEnabled(false);
                barChart.getAxisLeft().setEnabled(true);
                barChart.setDrawGridBackground(true);
                barChart.setDrawBarShadow(false);
                barChart.setEnabled(true);

                barChart.setTouchEnabled(true);
                barChart.setPinchZoom(true);
                barChart.setDoubleTapToZoomEnabled(true);

                barChart.setScaleEnabled(true);

                barChart.getLegend().setEnabled(true);

                //X軸周り
                XAxis xAxis = barChart.getXAxis();
                xAxis.setDrawLabels(true);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(true);

                barChart.setData(createBarChartData());

                barChart.invalidate();
                // アニメーション
                barChart.animateY(2000, Easing.EasingOption.EaseInBack);
            }

            // BarChartの設定
            private BarData createBarChartData() {
                ArrayList<BarDataSet> barDataSets = new ArrayList<>();

                // X軸
                ArrayList<String> xValues = new ArrayList<>();
                xValues.add("1月");
                xValues.add("2月");
                xValues.add("3月");
                xValues.add("4月");
                xValues.add("5月");
                xValues.add("6月");

                // valueA
                ArrayList<BarEntry> valuesA = new ArrayList<>();
                valuesA.add(new BarEntry(100, 0));
                valuesA.add(new BarEntry(200, 1));
                valuesA.add(new BarEntry(300, 2));
                valuesA.add(new BarEntry(400, 3));
                valuesA.add(new BarEntry(500, 4));
                valuesA.add(new BarEntry(600, 5));

                BarDataSet valuesADataSet = new BarDataSet(valuesA, "A");
                valuesADataSet.setColor(ColorTemplate.COLORFUL_COLORS[3]);

                barDataSets.add(valuesADataSet);

                // valueB
                ArrayList<BarEntry> valuesB = new ArrayList<>();
                valuesB.add(new BarEntry(200, 0));
                valuesB.add(new BarEntry(300, 1));
                valuesB.add(new BarEntry(400, 2));
                valuesB.add(new BarEntry(500, 3));
                valuesB.add(new BarEntry(600, 4));
                valuesB.add(new BarEntry(700, 5));

                BarDataSet valuesBDataSet = new BarDataSet(valuesB, "B");
                valuesBDataSet.setColor(ColorTemplate.COLORFUL_COLORS[4]);

                barDataSets.add(valuesBDataSet);

                BarData barData = new BarData(xValues, barDataSets);
                return barData;
            }
        }

        public static class ViewHolder3 extends ViewHolder {
            public String mBoundString;

            public final View mView;
            public final TextView mTextView;

            public ViewHolder3(View view) {
                super(view);
                mView = view;
                mTextView = (TextView) view.findViewById(android.R.id.text1);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }

        public SimpleStringRecyclerViewAdapter(Context context, List<String> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view;
            switch (viewType) {
                case 1:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.graph_item, parent, false);
                    return new ViewHolder2(view);
                case 2:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_header, parent, false);
                    return new ViewHolder(view);
                default:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
                    return new ViewHolder3(view);
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position < 1) {
                return 1;
            } else if (position == 1) {
                return 2;
            }
            return 3;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Log.d("hogehoge", String.valueOf(position));
            if (position < 1) {

            } else {
//                holder.mBoundString = mValues.get(position);
//                holder.mTextView.setText(mValues.get(position));
//                holder.mView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Context context = v.getContext();
//                        Intent intent = new Intent(context, CheeseDetailActivity.class);
//                        intent.putExtra(CheeseDetailActivity.EXTRA_NAME, holder.mBoundString);
//
//                        context.startActivity(intent);
//                    }
//                });
            }
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }
}
