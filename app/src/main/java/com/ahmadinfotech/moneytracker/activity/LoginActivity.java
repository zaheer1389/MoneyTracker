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

public class LoginActivity extends Activity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private FirebaseAuth mAuth;
    private DBHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegister);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        /*if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Log.d(TAG,"Alraedy logged in");
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }*/

        mAuth = FirebaseAuth.getInstance();

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(email)){
                    Helper.displayMessageToast(LoginActivity.this, "Login fields must be filled");
                    return;
                }
                if(!Helper.isValidEmail(email)){
                    Helper.displayMessageToast(LoginActivity.this, "Invalid email entered");
                    return;
                }

                checkLogin(email, password);
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        dbHelper = DBHelper.getInstance();
    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String email, final String password) {
        if(!AppUtil.isNetworkAvailable(this)){
            Helper.displayMessageToast(getApplicationContext(), "No internet connection available");
            return;
        }
        pDialog.setMessage("Logging in ...");
        showDialog();
        User user = dbHelper.getUser(email, AppUtil.generateHashedPassword(password));
        if(user == null){
            Helper.displayMessageToast(getApplicationContext(), "Invalid email or password");
            hideDialog();
            return;
        }
        else{
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