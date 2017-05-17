package projet.jet;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import projet.jet.activity.RestaurantsActivity;
import projet.jet.fragments.AccountFragment;
import projet.jet.fragments.EventsFragment;
import projet.jet.fragments.GroupsFragment;
import projet.jet.fragments.HomeFragment;
import projet.jet.fragments.RestoFragment;


/**
 * Created by Jess on 18/12/2016.
 */
public class GeneralActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private TextView usernameheader;

    GlobalApp ga;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_RESTAURANTS = "restaurants";
    private static final String TAG_GROUPES = "groupes";
    private static final String TAG_EVENTS = "events";
    private static final String TAG_ACCOUNT = "account";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);
        ga = (GlobalApp) getApplication();

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Checking if the item is in checked state or not, if not make it in checked state
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()){

                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.home:
                        Toast.makeText(getApplicationContext(),"Home Selected",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.account:
                        Toast.makeText(getApplicationContext(),"Account Selected",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.restaurants:
                        Intent restaurantPage = new Intent(GeneralActivity.this, RestaurantsActivity.class);
                        startActivity(restaurantPage);
                        Toast.makeText(getApplicationContext(),"Restaurants Selected",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.groups:
                        Toast.makeText(getApplicationContext(),"Groupes Selected",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.events:
                        Toast.makeText(getApplicationContext(),"Events Selected",Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(),"Somethings Wrong",Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);


        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        Integer t = navigationView.getHeaderCount();
        View hv  =  navigationView.getHeaderView(0);
        usernameheader = (TextView) hv.findViewById(R.id.usernameheader);
        usernameheader.setText(ga.prefs.getString("login",""));

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    public void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawerLayout.closeDrawers();

            // show or hide the fab button
            //toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        //toggleFab();

        //Closing drawer on item click
        drawerLayout.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    public Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                // account
                AccountFragment accountFragment = new AccountFragment();
                return accountFragment;
            case 2:
                // restaurants fragment
                /*Intent restaurantPage = new Intent(GeneralActivity.this, RestaurantsActivity.class);
                startActivity(restaurantPage);*/
                RestoFragment restoFragment = new RestoFragment();
                return restoFragment;
            case 4:
                // events fragment
                EventsFragment eventsFragment = new EventsFragment();
                return eventsFragment;

            case 3:
                // groupes fragment
                GroupsFragment groupsFragment = new GroupsFragment();
                return groupsFragment;
            default:
                return new HomeFragment();
        }
    }

    public void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.account:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_ACCOUNT;
                        break;
                    case R.id.restaurants:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_RESTAURANTS;
                        break;
                    case R.id.groups:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_GROUPES;
                        break;
                    case R.id.events:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_EVENTS;
                        break;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            SharedPreferences.Editor editor = ga.prefs.edit();
            editor.clear();
            editor.commit();
            Intent gotoLogin = new Intent(GeneralActivity.this, LoginActivity.class);
            startActivity(gotoLogin);
            finish();
            return true;
        }
        else if(id == R.id.action_search) {

        }

        return super.onOptionsItemSelected(item);
    }

    public static void setCurrentTag(String currentTag) {
        CURRENT_TAG = currentTag;
    }

    public static void setNavItemIndex(int navItemIndex) {
        GeneralActivity.navItemIndex = navItemIndex;
    }
}
