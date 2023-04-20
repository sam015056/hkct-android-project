package com.hkct.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class informationActivity extends AppCompatActivity {

    private final String TAG="informationActivity===>";
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        mRecyclerView = findViewById(R.id.recyclerView);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(informationActivity.this));
    }

    private void setNavigationDrawer(){
        // drawer layout instance
        drawerLayout = findViewById(R.id.drawerLayout);
        // Toggle the menu icon
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.nav_open,R.string.nav_close);
        actionBarDrawerToggle.syncState();

        // pass the toggle for the drawer layout listener
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

    } //setNavigationDrawer()

    // override the onOptionsItemSelected() function to implement
    // the item click listener callback to open and close the navigation
    // drawer when the icon is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG,"onOptionsItemSelected->" + item.getItemId());

//        if (item.getItemId()==R.id.nav_account){
//            Log.d(TAG,"onOptionsItemSelected->" + "id=" + R.id.nav_account + "title=" + item.getTitle());
//            txtOutput.setText("Account clicked");
//        }

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    } //onOptionsItemSelected()

    public void menu1_click(MenuItem m){
        Log.d(TAG,"menu1_click()->" + m.getItemId() + ","+ m.getTitle());
        startActivity(new Intent(this, DiscoverActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        drawerLayout.closeDrawers();
    }
    //    public void menu2_click(MenuItem m){
//        Log.d(TAG,"menu2_click()->" + m.getItemId() + ","+ m.getTitle());
////        txtOutput.setText(R.string.msg2);
//        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//        drawerLayout.closeDrawers();
//    }
    public void menu3_click(MenuItem m){
        Log.d(TAG,"menu3_click()->" + m.getItemId() + ","+ m.getTitle());
        startActivity(new Intent(this, ProfileActivity.class));
//        txtOutput.setText(R.string.msg3);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        drawerLayout.closeDrawers();
    }

    public void menu5_click(MenuItem menuItem) {
        Log.d(TAG,"menu5_click()->" + menuItem.getItemId() + ","+ menuItem.getTitle());
        startActivity(new Intent(this, EventsActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        drawerLayout.closeDrawers();
    }

    public void menu6_click(MenuItem menuItem) {
        Log.d(TAG,"menu6_click()->" + menuItem.getItemId() + ","+ menuItem.getTitle());
        startActivity(new Intent(this, VideoActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        drawerLayout.closeDrawers();
    }

    public void menu7_click(MenuItem menuItem) {
        Log.d(TAG,"menu7_click()->" + menuItem.getItemId() + ","+ menuItem.getTitle());
        startActivity(new Intent(this, informationActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        drawerLayout.closeDrawers();
    }

    //    MembershipActivity
    public void menu8_click(MenuItem menuItem) {
        Log.d(TAG,"menu8_click()->" + menuItem.getItemId() + ","+ menuItem.getTitle());
        startActivity(new Intent(this, MembershipActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        drawerLayout.closeDrawers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile, menu);
        getMenuInflater().inflate(R.menu.logout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void menu_profile_click(MenuItem m) {
        startActivity(new Intent(getApplicationContext(),SetUpActivity.class));
    }

    public void menu_logout_click(MenuItem m) {
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        FirebaseAuth.getInstance().signOut();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        Toast.makeText(informationActivity.this, "Logout successful", Toast.LENGTH_SHORT).show();
    }
}