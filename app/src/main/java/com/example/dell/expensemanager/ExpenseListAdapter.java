package com.example.dell.expensemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ExpenseListAdapter extends ArrayAdapter<FirebaseData> {
    private Context context;
    private List<FirebaseData> Model;

    public ExpenseListAdapter(@NonNull Context context, ArrayList<FirebaseData> data) {
        super(context, 0, data);
        this.context = context;
        this.Model = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, null, true);

        TextView category = view.findViewById(R.id.category);
        TextView amount = view.findViewById(R.id.amount);
        TextView detail = view.findViewById(R.id.detail);
        TextView time_date = view.findViewById(R.id.time_date);


        category.setText(Model.get(position).getCategory());
        amount.setText(Model.get(position).getAmount() + "₹");
        detail.setText(Model.get(position).getDetail());
        time_date.setText(Model.get(position).getTime() + " | " + Model.get(position).getDate() + "-" + Model.get(position).getMonth() + "-" + Model.get(position).getYear());

        return view;
    }
}
