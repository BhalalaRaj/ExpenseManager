package com.example.dell.expensemanager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.icu.util.IndianCalendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddExpense extends AppCompatActivity {

    EditText ammount, detail;
    Spinner select_category, select_payment_method;
    Button add_expense;
    static DatabaseReference ref;
    private int current_expense = 0;

    String uid = "";
    Date d;
    String year = "";
    String month = "";
    String day = "";

    SimpleDateFormat sdf;
    String currentDateTimeString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_add_expense);

        ammount = findViewById(R.id.ammount);
        detail = findViewById(R.id.short_detail);
        select_category = findViewById(R.id.select_category);
        select_payment_method = findViewById(R.id.payment_method);
        add_expense = findViewById(R.id.add_expense_btn);

        ArrayAdapter<CharSequence> category_spinner_addapter = ArrayAdapter.createFromResource(this,
                R.array.expense_array_spinner, android.R.layout.simple_spinner_item);
        category_spinner_addapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        select_category.setAdapter(category_spinner_addapter);

        ArrayAdapter<CharSequence> payment_method_spinner_addapter = ArrayAdapter.createFromResource(this,
                R.array.payment_method_array_spinner, android.R.layout.simple_spinner_item);
        payment_method_spinner_addapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        select_payment_method.setAdapter(payment_method_spinner_addapter);

        add_expense.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                storeExpense();
                updateTotal();
            }
        });

    }


    private void storeExpense() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
            d = new Date();
            year = d.getYear() + 1900 + "";
            month = d.getMonth() + 1 + "";
            day = d.getDate() + "";

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            String currentDateTimeString = sdf.format(d);

            final DatabaseReference monthTotalRef = FirebaseDatabase.getInstance().getReference().child(uid).child("Expense_Detail").child(year).child(month);

            monthTotalRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild("Month_Total")) {
                        Toast.makeText(AddExpense.this,"hasChild",Toast.LENGTH_SHORT).show();
                        addIntoMonth();
                    }
//                    else {
//                        create_Month();
//                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {
            Toast.makeText(this, "No User...", Toast.LENGTH_SHORT).show();
        }
    }

    private void addIntoMonth() {
        d = new Date();
        year = d.getYear() + 1900 + "";
        month = d.getMonth() + 1 + "";
        day = d.getDate() + "";

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String currentDateTimeString = sdf.format(d);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(uid).
                child("Expense_Detail").
                child(year).
                child(month).
                child(day).
                child(currentDateTimeString);

        current_expense = Integer.parseInt(ammount.getText().toString());
        final Map<String, String> expense_detail = new HashMap<>();
        expense_detail.put("Category", select_category.getSelectedItem().toString());
        expense_detail.put("Payment Method", select_payment_method.getSelectedItem().toString());
        expense_detail.put("Ammount", ammount.getText().toString());
        expense_detail.put("Detail", detail.getText().toString());
        ref.setValue(expense_detail); /////////////////////////////////////////////////////////////////++++++++++++++++++++++++++++++++++++

        final DatabaseReference monthTotalRef = FirebaseDatabase.getInstance().getReference().child(uid).child("Expense_Detail").child(year).child(month).child("Month_Total");

        monthTotalRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NewApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int monthly_total = Integer.parseInt(snapshot.getValue() + "");
                monthly_total = monthly_total + Integer.parseInt(expense_detail.get("Ammount"));
                monthTotalRef.setValue(monthly_total);
                expense_detail.replace("Ammount","0");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
    }

//    private void create_Month() {
//
//
//        d = new Date();
//        year = d.getYear() + 1900 + "";
//        month = d.getMonth() + 1 + "";
//        day = d.getDate() + "";
//
//        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
//        String currentDateTimeString = sdf.format(d);
//
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(uid).
//                child("Expense_Detail").
//                child(year).
//                child(month).
//                child(day).
//                child(currentDateTimeString);
//
//        current_expense = Integer.parseInt(ammount.getText().toString());
//        final Map<String, String> expense_detail = new HashMap<>();
//        expense_detail.put("Category", select_category.getSelectedItem().toString());
//        expense_detail.put("Payment Method", select_payment_method.getSelectedItem().toString());
//        expense_detail.put("Ammount", ammount.getText().toString());
//        expense_detail.put("Detail", detail.getText().toString());
//        ref.setValue(expense_detail); /////////////////////////////////////////////////////////////////++++++++++++++++++++++++++++++++++++
//
//        final DatabaseReference monthRef = FirebaseDatabase.getInstance().getReference().child(uid).child("Expense_Detail").child(year).child(month).child("Month_Total");
//        monthRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                monthRef.setValue(current_expense);
//                current_expense = 0;
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
//
//    }

    private void updateTotal() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            ref = FirebaseDatabase.getInstance().getReference().child(uid).child("Expense_Detail").child("Total");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int total = Integer.parseInt(snapshot.getValue().toString());
//                  UPDATING TOTAL EXPENSE
                    ref.setValue(total + current_expense);
                    current_expense = 0;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No User...", Toast.LENGTH_SHORT).show();
        }
    }
}
