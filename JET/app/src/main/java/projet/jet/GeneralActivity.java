package projet.jet;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.app.FragmentTransaction;
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


/**
 * Created by Jess on 18/12/2016.
 */
public class GeneralActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private TextView usernameheader;

    GlobalApp ga;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);
        ga = (GlobalApp) getApplication();

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                    case R.id.inbox:
                        Toast.makeText(getApplicationContext(),"Inbox Selected", Toast.LENGTH_SHORT).show();
                        ContentFragment fragment = new ContentFragment();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame,fragment);
                        fragmentTransaction.commit();
                        return true;

                    case R.id.fbaccount:
                        Intent gotoFBLogin = new Intent(GeneralActivity.this, FacebookActivity.class);
                        startActivity(gotoFBLogin);
                        Toast.makeText(getApplicationContext(),"Fb account Selected",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.account:
                        Toast.makeText(getApplicationContext(),"Account Selected",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.friends:
                        Intent gotoFriendsList = new Intent(GeneralActivity.this, UserFriendsActivity.class);
                        startActivity(gotoFriendsList);
                        Toast.makeText(getApplicationContext(),"Friends Selected",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.connect:
                        Intent gotoConnect = new Intent(GeneralActivity.this, ConnectActivity.class);
                        startActivity(gotoConnect);
                        Toast.makeText(getApplicationContext(),"Connect Selected",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.favoris:
                        Toast.makeText(getApplicationContext(),"Favoris Selected",Toast.LENGTH_SHORT).show();
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

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        Integer t = navigationView.getHeaderCount();
        View hv  =  navigationView.getHeaderView(0);
        usernameheader = (TextView) hv.findViewById(R.id.usernameheader);
        usernameheader.setText(ga.prefs.getString("login",""));
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
}
