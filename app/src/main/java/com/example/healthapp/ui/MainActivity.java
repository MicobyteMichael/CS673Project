package com.example.healthapp.ui;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.healthapp.R;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout theDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private HashMap<Integer, Class<? extends Fragment>> screenIDMap = new HashMap<>();

    public MainActivity() {
        super(R.layout.activity_main);
    }

    {
        screenIDMap.put(R.layout.home_screen_fragment, HomeScreenFragment.class);
        screenIDMap.put(R.xml.root_preferences, AccountSettingsFragment.class);
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
        selectScreen(theNav.getMenu().getItem(0));
        theNav.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    selectScreen(menuItem);
                    return true;
                }
            });
    }

    private void selectScreen(MenuItem menuItem) {
        Class<? extends Fragment> frag = screenIDMap.getOrDefault(menuItem.getItemId(), null);

        if(frag != null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.flContent, frag, null)
                    .addToBackStack(null)
                    .commit();
        }

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        theDrawer.closeDrawers();
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