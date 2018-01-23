package com.ahmadinfotech.moneytracker.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ahmadinfotech.moneytracker.R;
import com.ahmadinfotech.moneytracker.helper.DBHelper;
import com.ahmadinfotech.moneytracker.helper.Helper;
import com.ahmadinfotech.moneytracker.helper.SessionManager;
import com.ahmadinfotech.moneytracker.listener.OnGetDataListener;
import com.ahmadinfotech.moneytracker.model.User;
import com.ahmadinfotech.moneytracker.util.AppUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by root on 15/1/18.
 */

public class RegisterActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private DBHelper dbHelper;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputFullName = (EditText) findViewById(R.id.txtFullName);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        //DB Helper
        dbHelper = DBHelper.getInstance();

        // Check if user is already logged in or not
        /*if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }*/

        mAuth = FirebaseAuth.getInstance();

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String name= inputFullName.getText().toString().trim();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(email)){
                    Helper.displayMessageToast(RegisterActivity.this, "Register fields must be filled");
                    return;
                }
                if(!Helper.isValidEmail(email)){
                    Helper.displayMessageToast(RegisterActivity.this, "Invalid email entered");
                    return;
                }

                registerUser(email, password, name);
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerUser(final String email, final String password, final String name) {
        if(!AppUtil.isNetworkAvailable(this)){
            Helper.displayMessageToast(getApplicationContext(), "No internet connection available");
            return;
        }

        pDialog.setMessage("Registering ...");
        showDialog();

        if (dbHelper.isUserExist(email)) {
            Helper.displayMessageToast(getApplicationContext(), "Email already registered. Please use different email.");
            hideDialog();
            return;
        }
        else{
            User user = new User();
            user.setuId("");
            user.setEmail(email);
            user.setPassword(AppUtil.generateHashedPassword(password));
            user.setName(name);
            user = dbHelper.saveUser(user);

            session.setLogin(true);
            session.setUser(user);
            dbHelper.setLoggedInUser(user);
            dbHelper.init(new OnGetDataListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(DataSnapshot data) {
                    dbHelper.calculateBalances();
                    Helper.displayMessageToast(getApplicationContext(), "You have successfully registered !");
                    Intent profileIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(profileIntent);
                    finish();
                    hideDialog();
                }

                @Override
                public void onFailed(DatabaseError databaseError) {

                }
            });




        }

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}