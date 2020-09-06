package com.example.dell.expensemanager;

import android.app.Application;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class FireBaseContext extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // previous version
        Firebase.setAndroidContext(this);

        // new version
        if(!FirebaseApp.getApps(this).isEmpty()){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

    }
}
