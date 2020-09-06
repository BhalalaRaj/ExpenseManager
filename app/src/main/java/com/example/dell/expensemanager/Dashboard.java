package com.example.dell.expensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView tvTillDate, tvTPrice, monthly_total;
    ListView dashboard_list_items;
    FragmentManager fragmentManager;
    Fragment TopFragment, BottomFragment;

    androidx.appcompat.widget.Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawerLayout;

    TextView header_email, header_name;
    String user_id = "";
    private static final String TAG = "DATA";
    static Personal_Detail_Navigation_Header p;
    ExpenseListAdapter listAdapter;
    ArrayList<FirebaseData> recent_List;
    static ArrayList<FirebaseData> data;
    private ArrayList<FirebaseData> rececnt_spends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        recent_List = new ArrayList<>();

        tvTillDate = findViewById(R.id.tvTillDate);
        tvTPrice = findViewById(R.id.tvTPrice);
        monthly_total = findViewById(R.id.monthly_total);
        dashboard_list_items = findViewById(R.id.list_dashboard);
        fragmentManager = getSupportFragmentManager();
        TopFragment = fragmentManager.findFragmentById(R.id.TopFragment);
        BottomFragment = fragmentManager.findFragmentById(R.id.BottomFragment);
        fragmentManager.beginTransaction()
                .show(TopFragment).show(BottomFragment).
                commit();

        Firebase.setAndroidContext(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Dashboard");


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(Dashboard.this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

//        getting View from navigation header......
        View v = navigationView.getHeaderView(0);
        header_name = v.findViewById(R.id.nav_person_name);
        header_email = v.findViewById(R.id.nav_person_email);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user_id = user.getUid();
//            Toast.makeText(this, user.getUid(), Toast.LENGTH_LONG).show();
            getHeader();
        }

//****************************************** Getting total expanse till now and Monthly Expense ******************************************
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(user_id).child("Expense_Detail").
                child("Total");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvTPrice.setText(snapshot.getValue().toString() + " ₹");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Date d = new Date();
        String year = d.getYear() + 1900 + "";
        String month = d.getMonth() + 1 + "";
        final DatabaseReference monthly_ref = FirebaseDatabase.getInstance().getReference().child(user_id).child("Expense_Detail").child(year).child(month);
        monthly_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("Month_Total")) {
                    monthly_total.setText(snapshot.child("Month_Total").getValue() + " ₹");
                } else {
                    monthly_ref.child("Month_Total").setValue(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//*************************************************************************************************************************

    }

    private void getList() {

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(user_id).child("Expense_Detail");
        ref.addValueEventListener(new ValueEventListener() {

            CountDownLatch countDownLatch = new CountDownLatch(1);

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot year : snapshot.getChildren()) {
                    for (DataSnapshot month : year.getChildren()) {
                        for (DataSnapshot date : month.getChildren()) {
                            for (DataSnapshot time : date.getChildren()) {
                                data.add(new FirebaseData(
                                        time.child("Ammount").getValue() + "",
                                        time.child("Category").getValue() + "",
                                        time.child("Detail").getValue() + "",
                                        time.child("Payment Method").getValue() + "",
                                        time.getKey() + "",
                                        Integer.parseInt(date.getKey() + ""),
                                        Integer.parseInt(month.getKey() + ""),
                                        Integer.parseInt(year.getKey() + "")));
//                                listAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                    countDownLatch.countDown();
                }

                try {
                    countDownLatch.await();
                    getRecentList();

                } catch (Exception e) {
                    Toast.makeText(Dashboard.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Dashboard.this, "Some Error Occurs try after Some time..!", Toast.LENGTH_SHORT).show();
            }
        });

        listAdapter = new ExpenseListAdapter(Dashboard.this, data);
        dashboard_list_items.setAdapter(listAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        data = new ArrayList<>();
        getList();

        recent_List.clear();
        getRecentList();
//        dashboard_list_items.notify();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        data.clear();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//**********************************************    Getting recent 10 Expense Item ***********************************************
    private void getRecentList() {
        recent_List = new ArrayList<>();
        listAdapter = new ExpenseListAdapter(Dashboard.this, recent_List);
        int length = data.size();
        if (length <= 10) {
            for (int i = length - 1; i >= 0; i--) {
                recent_List.add(data.get(i));
                listAdapter.notifyDataSetChanged();
            }
        } else {
            for (int i = length - 1; i >= length - 10; i--) {
                recent_List.add(data.get(i));
                listAdapter.notifyDataSetChanged();
            }
        }
        dashboard_list_items.setAdapter(listAdapter);
    }
//********************************************************************************************************************************


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.add_expense: {
                startActivity(new Intent(Dashboard.this, AddExpense.class));
                Toast.makeText(Dashboard.this, "Add Expense", Toast.LENGTH_SHORT).show();
                break;
            }

            case R.id.all_expense: {
                startActivity(new Intent(Dashboard.this, All_Expense_List.class));
                Toast.makeText(Dashboard.this, "recent added", Toast.LENGTH_SHORT).show();
                break;
            }

            case R.id.view_expense_category_wise: {
                startActivity(new Intent(Dashboard.this, CategoryWiseGraph.class));
                Toast.makeText(Dashboard.this, "category wise", Toast.LENGTH_SHORT).show();
                break;
            }

            case R.id.view_expense_date_wise: {
                startActivity(new Intent(Dashboard.this, DateWiseGraph.class));
                Toast.makeText(Dashboard.this, "date wise", Toast.LENGTH_SHORT).show();
                break;
            }

            case R.id.logout: {
                SplashScreen.sqLite_login.deleteData(Dashboard.p.getEmail());
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Dashboard.this, Login.class));
                Toast.makeText(Dashboard.this, "Log out", Toast.LENGTH_SHORT).show();
                finish();
                break;
            }
            default: {
                Toast.makeText(Dashboard.this, "NULL", Toast.LENGTH_SHORT).show();
            }

        }
        return true;
    }

    private void getHeader() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(user_id).child("Personal_Detail");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    p = new Personal_Detail_Navigation_Header(snapshot.child("Name").getValue().toString(),
                            snapshot.child("Email").getValue().toString(),
                            snapshot.child("Password").getValue().toString(),
                            snapshot.child("Contact").getValue().toString());
                    header_name.setText(p.getName());
                    header_email.setText(p.getEmail());

                } catch (Exception e) {
                    Toast.makeText(Dashboard.this, "Error try after some time..!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
