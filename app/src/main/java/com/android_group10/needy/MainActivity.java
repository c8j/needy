package com.android_group10.needy;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android_group10.needy.ui.InNeed.AddPostRecordFragment;
import com.android_group10.needy.ui.InNeed.InNeedFragment;
import com.android_group10.needy.ui.LogInAndRegistration.LogIn;
import com.android_group10.needy.ui.NeedsAndDeeds.NeedsAndDeedsFragment;

import com.android_group10.needy.ui.Profile.ProfileFragment;
import com.android_group10.needy.ui.ToDo.ToDoFragment;
import com.facebook.login.LoginManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PopupMenu.OnMenuItemClickListener {
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private FloatingActionButton floatingActionButton;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private String userId;
    private FirebaseUser user;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private FirebaseAuth firebaseAuth;
    private EditText phoneNumber_dialog, city_dialog, zipCode_dialog;
    private View header;
    private ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialization();
        updateHeader();

        setSupportActionBar(toolbar);
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(view -> Snackbar.make(view, "Click here to add a help request", Snackbar.LENGTH_LONG)
                .setAction("New Need", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, new AddPostRecordFragment());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }).show());
        floatingActionButton.setImageResource(R.drawable.add_icon);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar
                , R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new InNeedFragment());
        fragmentTransaction.commit();

        ProfilePictureManager ppManager = new ProfilePictureManager();
        ppManager.displayProfilePic(this, profileImage);

        // Handle Image on on the Header.-X--Editing image only to be done in profile fragment to avoid confusion.
        /*
        profileImage.setOnClickListener(v -> {
            drawer.closeDrawer(GravityCompat.START);
            showPopup(profileImage);
            drawer.openDrawer(GravityCompat.START);
        });

         */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragment_container);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_in_need:
                changeFragment(new InNeedFragment());
                floatingActionButton.show();
                break;
            case R.id.nav_needs_and_deeds:
                changeFragment(new NeedsAndDeedsFragment());
                floatingActionButton.hide();
                break;
            case R.id.nav_to_do:
                changeFragment(new ToDoFragment());
                floatingActionButton.hide();
                break;
            case R.id.nav_profile:
                changeFragment(new ProfileFragment());
                floatingActionButton.hide();
                break;

            case R.id.log_out:
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                startActivity(new Intent(this, LogIn.class));
                break;
        }
        //This helps to close the navigation bar after choose an item from it.
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Change fragments when press on navigation drawer.
    public void changeFragment(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    // Item menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.about_us) {
            about_us_dialog();
        }

        return super.onOptionsItemSelected(item);
    }


    // Can press back button and only close the navigation bar and not the activity
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void initialization() {
        mAuth = FirebaseAuth.getInstance();
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);


        header = navigationView.getHeaderView(0);
        profileImage = (ImageView) header.findViewById(R.id.profileImageHeader);


        phoneNumber_dialog = findViewById(R.id.Phone_dialog);
        city_dialog = findViewById(R.id.City_dialog);
        zipCode_dialog = findViewById(R.id.zip_dialog);

    }

    //Update the User name and User email in the Header.
    public void updateHeader() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView userNameOnHeader = headerView.findViewById(R.id.user_name);
        TextView userEmailOnHeader = headerView.findViewById(R.id.user_email_header);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();


        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if (user != null) {
                    String name = user.getFirstName();
                    String email = user.getEmail();

                    userNameOnHeader.setText("Welcome " + name);
                    userEmailOnHeader.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void about_us_dialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.about_us, null);
        final AlertDialog alertD = new AlertDialog.Builder(this).create();
        alertD.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertD.setView(view);
        alertD.show();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void register_dialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.register_dialog, null);
        final AlertDialog alertD = new AlertDialog.Builder(this).create();
        alertD.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertD.setView(view);
        alertD.show();
    }

    public void checkIfFacebookUserExistsInDataBase() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference uidRef = rootRef.child("users").child(uid);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    register_dialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };
        uidRef.addListenerForSingleValueEvent(eventListener);
    }

    public void showPopup(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.image_menu);
        popupMenu.show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_image:
                Toast.makeText(this, "Change image", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.delete_image:
                Toast.makeText(this, "Delete image", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.take_image:
                Toast.makeText(this, "Take image", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return false;
        }
    }
}