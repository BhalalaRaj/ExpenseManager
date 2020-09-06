package com.example.dell.expensemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

public class All_Expense_List extends AppCompatActivity {

    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_all__expense__list);
        list = findViewById(R.id.list);
        ExpenseListAdapter adapter = new ExpenseListAdapter(All_Expense_List.this,Dashboard.data);
        adapter.notifyDataSetChanged();
        list.setAdapter(adapter);
    }
}
