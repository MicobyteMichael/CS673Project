package com.example.healthapp.ui;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.healthapp.HealthApplication;
import com.example.healthapp.R;
import com.example.healthapp.backend.RESTClient;
import com.example.healthapp.ui.auth.LoginActivity;
import com.example.healthapp.ui.bodycomp.BodyCompositionFragment;
import com.example.healthapp.ui.exercise.ExerciseFragment;
import com.example.healthapp.ui.meals.MealsFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout theDrawer;
    private ActionBarDrawerToggle drawerToggle;

    private HashMap<Integer, Class<? extends Fragment>> screenIDMap = new HashMap<>();
    private HashMap<Integer, Runnable> buttonFunctionMap = new HashMap<>();

    public MainActivity() {
        super(R.layout.activity_main);
    }

    {
        screenIDMap.put(R.layout.home_screen_fragment,    HomeScreenFragment.class);
        screenIDMap.put(R.xml   .root_preferences,        AccountSettingsFragment.class);
        screenIDMap.put(R.layout.fragment_exercise_list,  ExerciseFragment.class);
        screenIDMap.put(R.layout.fragment_body_comp_list, BodyCompositionFragment.class);
        screenIDMap.put(R.layout.fragment_meals_list,     MealsFragment.class);

        buttonFunctionMap.put(R.id.logOutButton, this::logOut);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar theToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(theToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        theDrawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, theDrawer, theToolbar, R.string.drawer_open,  R.string.drawer_close);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();
        theDrawer.addDrawerListener(drawerToggle);

        NavigationView theNav = (NavigationView)findViewById(R.id.nvView);
        selectScreen(theNav.getMenu().findItem(R.layout.home_screen_fragment));
        theNav.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    selectScreen(menuItem);
                    return true;
                }
            }
        );
    }

    private void logOut() {
        RESTClient.clearCookies();
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void selectScreen(MenuItem menuItem) {
        int id = menuItem.getItemId();
        Class<? extends Fragment> frag = screenIDMap.getOrDefault(id, null);

        if(frag == null) {
            Runnable action = buttonFunctionMap.getOrDefault(id, null);
            if(action != null) action.run();
        } else {
            showFrag(frag);
            menuItem.setChecked(true);
            setTitle(menuItem.getTitle());
        }

        theDrawer.closeDrawers();
    }

    public void showFrag(Class<? extends Fragment> frag) {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.flContent, frag, null)
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {}
}