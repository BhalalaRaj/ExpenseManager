package com.example.dell.expensemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DateWiseGraph extends AppCompatActivity {

    TextView textView4, textView5;
    DatePickerDialog datepicker;
    EditText etStart, etEnd;
    Button btnShow;
    final ArrayList<Entry> yValues = new ArrayList<>();
    ArrayList<ILineDataSet> dataset = new ArrayList<>();
    LineDataSet set1;

    Date current, start, end;
    float j = 0;

    private static final String TAG = "DateWiseGraph";
    private LineChart mChart;

    private int start_date, start_month, start_year;
    private int end_date, end_month, end_year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_date_wise_graph);
//
//        textView4 = findViewById(R.id.textView4);
//        textView5 = findViewById(R.id.textView5);
        btnShow = findViewById(R.id.btnShow);

        etStart = findViewById(R.id.etStart);
        etEnd = findViewById(R.id.etEnd);
        etStart.setInputType(InputType.TYPE_NULL);
        etEnd.setInputType(InputType.TYPE_NULL);

        mChart = findViewById(R.id.date_graph);
        // mChart.setOnChartGestureListener(CategoryWiseGraph.this);
        //mChart.setOnChartValueSelectedListener(CategoryWiseGraph.this);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);

        etStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                datepicker = new DatePickerDialog(DateWiseGraph.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                initialize_Start(dayOfMonth, monthOfYear, year);
                                etStart.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datepicker.show();
            }
        });

        etEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                datepicker = new DatePickerDialog(DateWiseGraph.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                initialize_End(dayOfMonth, monthOfYear, year);
                                etEnd.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datepicker.show();
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear();
                getGraph();
            }
        });

    }

    private void initialize_Start(int dayOfMonth, int monthOfYear, int year) {
        start_date = dayOfMonth;
        start_month = monthOfYear;
        start_year = year;
    }

    private void initialize_End(int dayOfMonth, int monthOfYear, int year) {
        end_date = dayOfMonth;
        end_month = monthOfYear;
        end_year = year;
    }

    private void clear() {
        j = 0;
        mChart.clear();
        dataset.clear();
        yValues.clear();
    }

    private void getGraph() {
        j = 10;
        for (int i = 0; i < Dashboard.data.size(); i++) {
            current = new Date(Dashboard.data.get(i).getYear() - 0, Dashboard.data.get(i).getMonth() - 1, Dashboard.data.get(i).getDate() - 0);
            start = new Date(start_year, start_month, start_date - 1);
            end = new Date(end_year, end_month, end_date + 1);

            if (current.before(end) && current.after(start)) {
                yValues.add(new Entry(j, Float.parseFloat(Dashboard.data.get(i).getAmount())));
                j = j + 10;
            }
        }
        set1 = new LineDataSet(yValues, "Spends");
        set1.setFillAlpha(110);
        set1.setColor(Color.RED);

        dataset = new ArrayList<>();
        dataset.add(set1);

        LineData data = new LineData(dataset);

        mChart.setData(data);
        mChart.setScaleEnabled(true);
        mChart.setClickable(true);

    }


}


