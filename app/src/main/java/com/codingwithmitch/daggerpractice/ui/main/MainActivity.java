package com.codingwithmitch.daggerpractice.ui.main;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.codingwithmitch.daggerpractice.BaseActivity;
import com.codingwithmitch.daggerpractice.R;
import com.codingwithmitch.daggerpractice.ui.main.posts.PostsFragment;
import com.codingwithmitch.daggerpractice.ui.main.profile.ProfileFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends BaseActivity  implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        init();
    }

    /**
     * Here we are setting up the NavigationView with the Navigation library. We created drawer_view in menu and
     * main.xml in navigation directory that defines fragments using already created layouts and routes them. Then in
     * activity_main we define a fragment that will hold this main.xml as a NavGraph.
     *
     * Then here we use NavController class to setup some provided UI through the Navigation library and set a
     * NavigationLisener Callback up.
     */
    private void init() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mDrawerLayout);
        NavigationUI.setupWithNavController(mNavigationView, navController);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout: {
                mSessionManager.logout();
                return true;
            }
            // This references the Hamberger menu button
            case android.R.id.home: {
                if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                } else {
                    // If drawer is not open do not consume the click
                    return false;
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Instead of having to use fragment transactions here we utilize Navigation again to do this
     * @param menuItem
     * @return
     *
     * Also creating NavOptions builder to handle clearing the backstack when needed. We reference the navGraph in
     * setPopUpTo. Then we implement it into findNavController on nav_profile so we clear the backstack when returning
     * to that screen.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_profile: {
                NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.main, true).build();
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.profileScreen, null, navOptions);
                break;
            }
            case R.id.nav_posts: {
                if(isValidDestination(R.id.postScreen)){
                    Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.postScreen);
                }
                break;
            }
        }
        menuItem.setChecked(true);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * checks to see if we are already on the posts screen so we can't add a ton to the back stack.
     * @param destination
     * @return
     */
    private boolean isValidDestination(int destination) {
        return destination != Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId();
    }

    /**
     * Helps to control how back navgiation (or navigating up) works correctly. Here we tell it when we want to
     * navigate up reference the NavController and the Drawer layout. Also the Hamberger Menu now works when this
     * method is implemented.
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), mDrawerLayout);
    }
}
