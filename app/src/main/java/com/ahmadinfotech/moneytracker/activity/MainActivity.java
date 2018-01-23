package com.ahmadinfotech.moneytracker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmadinfotech.moneytracker.R;
import com.ahmadinfotech.moneytracker.fragement.LenderFragement;
import com.ahmadinfotech.moneytracker.fragement.ManageLenderFragement;
import com.ahmadinfotech.moneytracker.fragement.ManageTransactionFragement;
import com.ahmadinfotech.moneytracker.fragement.UserFragement;
import com.ahmadinfotech.moneytracker.helper.DBHelper;
import com.ahmadinfotech.moneytracker.helper.Helper;
import com.ahmadinfotech.moneytracker.helper.SessionManager;
import com.ahmadinfotech.moneytracker.model.User;
import com.ahmadinfotech.moneytracker.other.CircleTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView txtFullName;
    private TextView txtEmail;
    private ImageView  imgProfile;
    private SessionManager session;

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private View navHeader;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    private FirebaseAuth mAuth;
    private DBHelper dbHelper;

    private int currentNavigationIndex = 0;

    // urls to load navigation header background image
    // and profile image
    private static final String urlNavHeaderBg = "https://img2.goodfon.ru/original/1366x768/3/b6/aa.jpg";
    private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // session manager
        session = new SessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            logoutUser();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentNavigationIndex == 0){
                    Bundle bundle = new Bundle();
                    bundle.putString("id", "");
                    ManageLenderFragement fragement = new ManageLenderFragement();
                    fragement.setArguments(bundle);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragement).addToBackStack("fragBack");
                    ft.commit();
                }
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);

        User user = session.getUser();
        //imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);
        txtFullName = (TextView)navHeader.findViewById(R.id.txtFullName);
        txtEmail = (TextView)navHeader.findViewById(R.id.txtEmail);

        txtFullName.setText(user.getName());
        txtEmail.setText(user.getEmail());

        // loading header background image
        //Glide.with(this).load(urlNavHeaderBg)
        //        .crossFade()
        //        .diskCacheStrategy(DiskCacheStrategy.ALL)
        //        .into(imgNavHeaderBg);

        // Loading profile image
        //Glide.with(this).load(urlProfileImg)
        //        .crossFade()
        //        .thumbnail(0.5f)
         //       .bitmapTransform(new CircleTransform(this))
        //        .diskCacheStrategy(DiskCacheStrategy.ALL)
          //      .into(imgProfile);

        mAuth = FirebaseAuth.getInstance();
        dbHelper = DBHelper.getInstance();

        navigationView.getMenu().getItem(0).setChecked(true);
        loadDefaultFragement();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_lender) {
            currentNavigationIndex = 0;
            // Handle the lender action
            LenderFragement fragement = new LenderFragement();
            //UserFragement fragement = new UserFragement();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragement);
            ft.commit();
        }
        else if (id == R.id.nav_transaction) {
            Bundle bundle = new Bundle();
            bundle.putString("id", "");
            bundle.putString("action","add");
            ManageTransactionFragement fragement = new ManageTransactionFragement();
            fragement.setArguments(bundle);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragement);
            ft.commit();
        }
        else if (id == R.id.nav_logout) {
            mAuth.signOut();
            session.setLogin(false);
            session.setUser(null);
            Helper.displayMessageToast(this, "You have successfully logout from application.");
            finish();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void loadDefaultFragement(){
        LenderFragement fragement = new LenderFragement();
        //UserFragement fragement = new UserFragement();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragement);
        ft.commit();
    }
}
