package com.example.poskorafinaru;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        MainFragment.OnFragmentInteractionListener,
        AreaFragment.OnListFragmentInteractionListener,
        WitelFragment.OnListFragmentInteractionListener,
        UbisFragment.OnListFragmentInteractionListener,
        CrewFragment.OnFragmentInteractionListener,
        AboutFragment.OnFragmentInteractionListener {

    FragmentTransaction fragmentTransaction;
    private FrameLayout container;
    boolean doubleBackToExitPressedOnce = false;
    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        container = (FrameLayout) findViewById(R.id.containerFragment);

        sharedPrefManager = SharedPrefManager.getInstance(this);

        if (Calendar.getInstance().get(Calendar.MONTH) + 1 != 1) {
            sharedPrefManager.setTextDatePick1("26-" + Calendar.getInstance().get(Calendar.MONTH) + "-" + Calendar.getInstance().get(Calendar.YEAR));
            if (Calendar.getInstance().get(Calendar.MONTH)+1<10)
                sharedPrefManager.setDatePick1(Calendar.getInstance().get(Calendar.YEAR) + "0" + Calendar.getInstance().get(Calendar.MONTH));
            else sharedPrefManager.setDatePick1(Calendar.getInstance().get(Calendar.YEAR) + "" + Calendar.getInstance().get(Calendar.MONTH));
        } else {
            sharedPrefManager.setTextDatePick1("26-12-" + Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR) - 1));
            sharedPrefManager.setDatePick1(Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR) - 1) + "12");
        }

        if (Calendar.getInstance().get(Calendar.MONTH)+1 > 12) {
            sharedPrefManager.setTextDatePick2("25-01-" + Calendar.getInstance().get(Calendar.YEAR));
            sharedPrefManager.setDatePick2(Calendar.getInstance().get(Calendar.YEAR)+"01");
        } else {
            sharedPrefManager.setTextDatePick2("25-"+Integer.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1)+"-"+Calendar.getInstance().get(Calendar.YEAR));
            if (Calendar.getInstance().get(Calendar.MONTH)+1 < 10)
                sharedPrefManager.setDatePick2(Calendar.getInstance().get(Calendar.YEAR)+"0"+Integer.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1));
            else sharedPrefManager.setDatePick2(""+Calendar.getInstance().get(Calendar.YEAR)+Integer.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1));
        }

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        swapFragment(R.id.nav_home);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(Treg item) {

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        swapFragment(item.getItemId());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void swapFragment(int id) {
        if (id == R.id.nav_home) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            MainFragment mainFragment = MainFragment.newInstance();
            fragmentTransaction.replace(R.id.containerFragment,mainFragment);
            fragmentTransaction.disallowAddToBackStack();
            fragmentTransaction.commit();
        } else if (id == R.id.nav_area) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            AreaFragment areaFragment = AreaFragment.newInstance();
            fragmentTransaction.replace(R.id.containerFragment, areaFragment);
            fragmentTransaction.disallowAddToBackStack();
            fragmentTransaction.commit();
        } else if (id == R.id.nav_witel) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            WitelFragment witelFragment = WitelFragment.newInstance();
            fragmentTransaction.replace(R.id.containerFragment, witelFragment);
            fragmentTransaction.disallowAddToBackStack();
            fragmentTransaction.commit();
        } else if (id == R.id.nav_ubis) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            UbisFragment ubisFragment = UbisFragment.newInstance();
            fragmentTransaction.replace(R.id.containerFragment, ubisFragment);
            fragmentTransaction.disallowAddToBackStack();
            fragmentTransaction.commit();
        } else if (id == R.id.nav_crew) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            CrewFragment crewFragment = CrewFragment.newInstance();
            fragmentTransaction.replace(R.id.containerFragment, crewFragment);
            fragmentTransaction.disallowAddToBackStack();
            fragmentTransaction.commit();
        } else if (id == R.id.nav_about) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            AboutFragment aboutFragment = AboutFragment.newInstance();
            fragmentTransaction.replace(R.id.containerFragment, aboutFragment);
            fragmentTransaction.disallowAddToBackStack();
            fragmentTransaction.commit();
        } else if (id == R.id.nav_logout) {
            Intent bukaLoginActivity = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(bukaLoginActivity);
            sharedPrefManager.logout();
            finish();
        }
    }
}
