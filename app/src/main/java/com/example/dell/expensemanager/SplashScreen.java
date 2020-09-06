package com.example.dell.expensemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    public static SQLite_Login sqLite_login;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private final int SCREEN_TIME = 5000;

    String useremail = "";
    String userpassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        sqLite_login = new SQLite_Login(this,"LOGINDB.sqlite",null,1);

        sqLite_login.queryData("CREATE TABLE IF NOT EXISTS LoginDetail(email VARCHAR, password VARCHAR)");


        firebaseAuth = FirebaseAuth.getInstance();
/////////// if User already logged in ////////////////////////////////////////////////////////
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth != null){
                    startActivity(new Intent(SplashScreen.this,Dashboard.class));
                }
            }
        };
///////////////////////////////////////////////////////////////////////////////////////////


        final Intent i ;

        if(sqLite_login.isEmpty("LoginDetail"))
        {
            i = new Intent(this,Login.class);
        } else {
            i = new Intent(this,Dashboard.class);
            Cursor cursor = SplashScreen.sqLite_login.getData("SELECT * FROM LoginDetail");
            if(cursor.moveToNext()){
                useremail = cursor.getString(0);
                userpassword = cursor.getString(1);
                startSignIn();
            }
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(i);
                finish();
            }
        },SCREEN_TIME);

    }

    private void startSignIn() {

        firebaseAuth.signInWithEmailAndPassword(useremail, userpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
//                    startActivity(new Intent(SplashScreen.this, Dashboard.class));
                    return;
                } else {
                    startActivity(new Intent(SplashScreen.this,Login.class));
                }
            }
        });
    }
}
