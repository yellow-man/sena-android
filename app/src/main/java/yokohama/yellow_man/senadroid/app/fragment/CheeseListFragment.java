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

package yokohama.yellow_man.senadroid.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import yokohama.yellow_man.senadroid.app.entity.Cheeses;
import yokohama.yellow_man.senadroid.R;
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
                LineChart chart = (LineChart) view.findViewById(R.id.chart);

                // apply styling
                // holder.chart.setValueTypeface(mTf);
                chart.getDescription().setEnabled(false);
                chart.setDrawGridBackground(false);

                XAxis xAxis = chart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(false);
                xAxis.setDrawAxisLine(true);

                YAxis leftAxis = chart.getAxisLeft();
                leftAxis.setLabelCount(5, false);
                leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

                YAxis rightAxis = chart.getAxisRight();
                rightAxis.setLabelCount(5, false);
                rightAxis.setDrawGridLines(false);
                rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

                // set data
                chart.setData(createBarChartData());

                // do not forget to refresh the chart
                // holder.chart.invalidate();
                chart.animateX(750);
            }

            // LineDataの設定
            private LineData createBarChartData() {
                ArrayList<Entry> e1 = new ArrayList<Entry>();

                for (int i = 0; i < 12; i++) {
                    e1.add(new Entry(i, (int) (Math.random() * 65) + 40));
                }

                LineDataSet d1 = new LineDataSet(e1, "New DataSet " + 1 + ", (1)");
                d1.setLineWidth(2.5f);
                d1.setCircleRadius(4.5f);
                d1.setHighLightColor(Color.rgb(244, 117, 117));
                d1.setDrawValues(false);

                ArrayList<Entry> e2 = new ArrayList<Entry>();

                for (int i = 0; i < 12; i++) {
                    e2.add(new Entry(i, e1.get(i).getY() - 30));
                }

                LineDataSet d2 = new LineDataSet(e2, "New DataSet " + 1 + ", (2)");
                d2.setLineWidth(2.5f);
                d2.setCircleRadius(4.5f);
                d2.setHighLightColor(Color.rgb(244, 117, 117));
                d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
                d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
                d2.setDrawValues(false);

                ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
                sets.add(d1);
                sets.add(d2);

                LineData cd = new LineData(sets);
                return cd;
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
                // グラフ

            } else if (position == 1) {
                // ヘッダー

            } else {
                // ボディ
                ViewHolder3 holder3 = (ViewHolder3)holder;

                holder3.mBoundString = mValues.get(position);
                holder3.mTextView.setText(mValues.get(position));
                holder3.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, CheeseDetailActivity.class);
                        intent.putExtra(CheeseDetailActivity.EXTRA_NAME, holder.mBoundString);

                        context.startActivity(intent);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }
}
