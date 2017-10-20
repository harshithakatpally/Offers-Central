package com.example.vamsi.firebaseauth1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeScreen extends AppCompatActivity implements TabLayout.OnTabSelectedListener,NavigationView.OnNavigationItemSelectedListener  {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabsPagerAdapter mAdapter;
    private android.app.ActionBar actionBar;

    private String[] tabs = {"FOOD", "MOVIES", "SHOPPING"};

    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private RecyclerView recyclerView;

    private DatabaseReference rootReference;
    private FirebaseUser firebaseUser;
    //ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sliding_main);

        rootReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();



        /*
        mNavItems.add(new NavItem("Home", "Meetup destination", R.drawable.email));
        mNavItems.add(new NavItem("Preferences", "Change your preferences", R.drawable.email));
        mNavItems.add(new NavItem("About", "Get to know about us", R.drawable.email));
        */

        /* DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });
        */

        //TODO Check what is that happening with this code
        Toolbar toolbar = (Toolbar) findViewById(R.id.couponsToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Offers Central");

        Toolbar toolbars = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.couponsPager);

        mAdapter = new TabsPagerAdapter(getFragmentManager());

        viewPager.setAdapter(mAdapter);

        tabLayout = (TabLayout) findViewById(R.id.couponsTabs);





        for(String tab_name : tabs)
        {
            tabLayout.addTab(tabLayout.newTab().setText(tab_name));
        }
        tabLayout.addOnTabSelectedListener(this);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbars, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    /*
    private void selectItemFromDrawer(int position) {
        Fragment fragment = new PreferencesFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainContent, fragment).commit();
        mDrawerList.setItemChecked(position, true);
        setTitle(mNavItems.get(position).mTitle);

        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    */


    @Override
    public void onStart() {
        super.onStart();

        rootReference.child("Users").child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        Toast.makeText(HomeScreen.this, "Mobile Verified : "+user.isMobileVerified(), Toast.LENGTH_SHORT).show();
                        if(!(user.isEmailVerified() && user.isMobileVerified()))
                        {
                            Intent i = new Intent(HomeScreen.this,VerifyCredentials.class);
                            startActivity(i);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

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

        if (id == R.id.nav_wallet) {
            Intent w= new Intent(getApplicationContext(), WalletHomeScreen.class);
            startActivity(w);
        } else if (id == R.id.nav_home) {
            Intent h= new Intent(getApplicationContext(), HomeScreen.class);
            startActivity(h);
        } else if (id == R.id.nav_wishlist) {
            Intent wl = new Intent(getApplicationContext(), WishList.class);
            startActivity(wl);
        } else if (id == R.id.nav_profilesettings) {
            Intent ps = new Intent(getApplicationContext(), PersonalSettings.class);
            startActivity(ps);

        } else if(id == R.id.reminder){
            Intent r =new Intent(getApplicationContext(), SetReminder.class);
            r.putExtra("type","general");
            startActivity(r);
        }

        else if(id == R.id.logOut)
        {

            FirebaseAuth firebaseAuth  = FirebaseAuth.getInstance();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            String emailID = user.getEmail();

            SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            editor.putString("emailID",emailID);
            editor.commit();

            firebaseAuth.signOut();
            Intent i = new Intent(HomeScreen.this,MainActivity.class);
            startActivity(i);
        }

        else if(id == R.id.dummyLayout)
        {
            Intent i = new Intent(HomeScreen.this,DummyClass.class);
            startActivity(i);
        }

        else if(id == R.id.preferencesLayout)
        {
            Intent i = new Intent(HomeScreen.this, PreferencesScreen.class);
            startActivity(i);
        }

        else if(id == R.id.verifyCredentials)
        {
            Intent i = new Intent(this,VerifyCredentials.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
