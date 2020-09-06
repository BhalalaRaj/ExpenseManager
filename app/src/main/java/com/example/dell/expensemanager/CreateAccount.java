package com.example.dell.expensemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.firebase.client.Firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;
import static com.basgeekball.awesomevalidation.ValidationStyle.COLORATION;
import static com.basgeekball.awesomevalidation.ValidationStyle.UNDERLABEL;

public class CreateAccount extends AppCompatActivity {

    EditText name, email, contact_no;
    EditText password, confirmpassword;

    Button create_acc;

    Firebase firebase;
    ImageButton pass_eye, confirm_pass_eye;

    boolean showpass = false;
    boolean show_confirm_pass = false;

    //    Validation

    AwesomeValidation mAwesomeValidation;

    //    FireBase creating account
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        contact_no = findViewById(R.id.contact_no);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirm_password);
        pass_eye = findViewById(R.id.pass_eye);
        confirm_pass_eye = findViewById(R.id.confirm_pass_eye);

        create_acc = findViewById(R.id.create_acc_btn);

        mAuth = FirebaseAuth.getInstance();



        mAwesomeValidation = new AwesomeValidation(BASIC);
        AwesomeValidation.disableAutoFocusOnFirstFailure();

        mAwesomeValidation.addValidation(this, R.id.name, "[a-zA-Z\\s]+", R.string.name);
        mAwesomeValidation.addValidation(this, R.id.contact_no, "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}$", R.string.contact);
        mAwesomeValidation.addValidation(this, R.id.email, Patterns.EMAIL_ADDRESS, R.string.email);
// to validate the confirmation of another field
        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        mAwesomeValidation.addValidation(this, R.id.password, regexPassword, R.string.password);
// to validate a confirmation field (don't validate any rule other than confirmation on confirmation field)
        mAwesomeValidation.addValidation(this, R.id.confirm_password, R.id.password, R.string.not_match_password);


        create_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAwesomeValidation.validate()){
                    createAccount();
                }
            }
        });

//******************************************** Password Toggle methods ************************************************************
        pass_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getTransformationMethod().getClass().getSimpleName().equals("PasswordTransformationMethod")){
                    password.setTransformationMethod(new SingleLineTransformationMethod());
                } else {
                    password.setTransformationMethod(new PasswordTransformationMethod());
                }
                password.setSelection(password.getText().length());
            }
        });
        confirm_pass_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(confirmpassword.getTransformationMethod().getClass().getSimpleName().equals("PasswordTransformationMethod")){
                    confirmpassword.setTransformationMethod(new SingleLineTransformationMethod());
                } else {
                    confirmpassword.setTransformationMethod(new PasswordTransformationMethod());
                }
                confirmpassword.setSelection(confirmpassword.getText().length());
            }
        });
//***************************************************************************************************************************************

    }

    private void createAccount() {
        String c_email = email.getText().toString().trim();
        String c_password = password.getText().toString().trim();
        mAuth.createUserWithEmailAndPassword(c_email, c_password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(CreateAccount.this, "Authentication Successful.",Toast.LENGTH_SHORT).show();
                    storeData();
                } else {
                    // If sign in fails, display a message to the user...
                    Toast.makeText(CreateAccount.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void storeData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = "";
        if (user != null) {
            uid = user.getUid();
            Map<String,Object> userInfo = new HashMap<>();
            userInfo.put("Name",name.getText().toString().trim());
            userInfo.put("Email",email.getText().toString().trim());
            userInfo.put("Contact",contact_no.getText().toString().trim());
            userInfo.put("Password",password.getText().toString().trim());

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(uid);
            databaseReference.child("Personal_Detail").setValue(userInfo);

            databaseReference.child("Expense_Detail").child("Total").setValue(0);
            Toast.makeText(this,"Account created..",Toast.LENGTH_LONG).show();
            startActivity(new Intent(CreateAccount.this,Login.class));
        }


    }
}
