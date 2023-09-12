package com.example.expensestracker.budgetanalysis;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.expensestracker.R;
import com.example.expensestracker.budget_categories.BudgetCategoryManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BudgetAnalysisFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_budget_analysis, container, false);
        final Context context = getActivity().getApplicationContext();
        try {
            AnyChartView anyChartView = root.findViewById(R.id.any_chart_view);
            anyChartView.setProgressBar(root.findViewById(R.id.progress_bar));

            HashMap<String, Double> dataMap = BudgetCategoryManager.getCategoryExpensesMap();
            List<DataEntry> data = new ArrayList<>();

            for (Map.Entry element : dataMap.entrySet()) {
                String key = (String) element.getKey();
                Double value = (Double) element.getValue();

                int intValue = value.intValue();

                data.add(new ValueDataEntry(key, intValue));
                Log.i("onCreateView", "onCreateView: " + intValue);
            }

            buildAnalysisChart(anyChartView, "Your Expenses per category this Month", "Budget Categories"
                    , data);

            /*
             * Added by Mahan
             * Issue: Clickable Span
             * ==================================================
             */

            TextView textView = root.findViewById(R.id.clickableSpanText);

            // Create a SpannableString from the text
            SpannableString spannableString = new SpannableString(textView.getText());

            // Create a ClickableSpan for the URL
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    // Handle the click action by opening the URL in a web browser
                    String url = "https://github.com/Nada-Nasser/Money-Manager-App"; // Replace with your URL
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                }
            };

            // Set the ClickableSpan to the specific range of the URL
            int start = textView.getText().toString().indexOf("website");
            int end = start + "website".length();
            spannableString.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            // Set the modified SpannableString to the TextView
            textView.setText(spannableString);
            textView.setMovementMethod(LinkMovementMethod.getInstance());

            /*
             * ==================================================
             */
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return root;
    }

    //anyChartView , Pie title , channelsTitle , List<DataEntry>
    private void buildAnalysisChart(AnyChartView anyChartView , String pieTitle ,String channelsTitle
                , List<DataEntry> data) {


        Pie pie = AnyChart.pie();

        pie.setOnClickListener(new ListenersInterface.OnClickListener() {
            @Override
            public void onClick(Event event) {

            }
        });

        pie.data(data);

        pie.title(pieTitle);

        pie.labels().position("outside");

        pie.legend().title().enabled(true);
        pie.legend().title()
                .text(channelsTitle)
                .padding(0d, 0d, 10d, 0d);

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);

        anyChartView.setChart(pie);

    }
}