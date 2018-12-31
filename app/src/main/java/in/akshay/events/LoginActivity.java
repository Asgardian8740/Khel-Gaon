package in.akshay.events;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.transition.Slide;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rubensousa.raiflatbutton.RaiflatButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.picasso.Picasso;


import java.util.Stack;

public class LoginActivity extends AppCompatActivity  {

    TextView email;
    Bundle bundle;
    ImageView photo;
    FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    Boolean check;
    String[] listitems;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    SharedPreferences.Editor Ed;
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        listitems = getResources().getStringArray(R.array.items);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        String email = user.getEmail();

        Singleton.Instance().setEmail(String.valueOf(user.getEmail()));
        Singleton.Instance().setName(String.valueOf(user.getDisplayName()));
        Toast.makeText(this, Singleton.Instance().getEmail(), Toast.LENGTH_SHORT).show();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        organiser(tabLayout);







        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext()) //Use app context to prevent leaks using activity
                //.enableAutoManage(this /* FragmentActivity */, connectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
        /*AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoginActivity.this);
        mBuilder.setTitle("Choose your Mode");


        mBuilder.setSingleChoiceItems(listitems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 1) {
                    player(tabLayout);
                    dialogInterface.dismiss();
                } else {
                    organiser(tabLayout);
                    dialogInterface.dismiss();
                }
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();*/

        bundle = getIntent().getExtras();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return false;
            }
        });

        final View hview = navigationView.getHeaderView(0);
        CircularImageView img = (CircularImageView) hview.findViewById(R.id.userimage);

            Picasso.get()
                    .load(user.getPhotoUrl())
                    .into(img);

        TextView register =(TextView)hview.findViewById(R.id.eventregister);
        TextView usernav =(TextView)hview.findViewById(R.id.username);
        usernav.setText(user.getDisplayName());
        TextView emailnav =(TextView)hview.findViewById(R.id.email);
        emailnav.setText(user.getEmail());


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                viewPager.setCurrentItem(3);
                drawerLayout.closeDrawer(Gravity.LEFT);




            }
        });
        TextView myevents =(TextView)hview.findViewById(R.id.myEvents);
        myevents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(2);
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
        TextView allevents =(TextView)hview.findViewById(R.id.Allevents);
        allevents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                viewPager.setCurrentItem(0);
                drawerLayout.closeDrawer(Gravity.LEFT);

            }
        });





        RaiflatButton logout=(RaiflatButton)hview.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                mAuth.signOut();
                                // ...
                                FancyToast.makeText(LoginActivity.this,"You have Successfully Logged Out !",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();

                                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                                finish();
                                startActivity(i);
                            }
                        });

            }
        });


    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void player(TabLayout tabLayout) {
        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
        tabLayout.removeTabAt(0);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(0).setVisibility(View.GONE);
        //Set the next  tab as selected tab
        TabLayout.Tab tab = tabLayout.getTabAt(1);
        tab.select();

        final fragmentpageadapter adapter = new fragmentpageadapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        });

    }*/

    public void organiser(TabLayout tabLayout) {
        tabLayout.addTab(tabLayout.newTab().setText("All Events"));
        tabLayout.addTab(tabLayout.newTab().setText("Posts"));
        tabLayout.addTab(tabLayout.newTab().setText("My Events"));
        tabLayout.addTab(tabLayout.newTab().setText("Register "+"\n"+"your event"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);
        final fragmentpageadapter adapter = new fragmentpageadapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        });

    }

    @Override
    public void onBackPressed() {

    }



    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        mGoogleApiClient.connect();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }




}
