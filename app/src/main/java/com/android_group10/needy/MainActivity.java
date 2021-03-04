package com.android_group10.needy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;


import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.android_group10.needy.LocalDatabase.DbBitmapUtility;
import com.android_group10.needy.LocalDatabase.LocalDatabaseHelper;
import com.android_group10.needy.ui.InNeed.AddPostRecordFragment;
import com.android_group10.needy.ui.InNeed.InNeedFragment;
import com.android_group10.needy.ui.LogInAndRegistration.LogIn;
import com.android_group10.needy.ui.NeedsAndDeeds.NeedsAndDeedsFragment;

import com.android_group10.needy.ui.Profile.ProfileFragment;
import com.android_group10.needy.ui.ToDo.ToDoFragment;
import com.bumptech.glide.Glide;
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


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
    private boolean hasChanged = false;
    private Bitmap bitmap;
    private long pressedTime;


    private String imageFilePath;
    File photoFile = null;
    private int five = 5;
    private SQLiteDatabase database;
    private Uri selectedImageUri;
    private LocalDatabaseHelper localDatabaseHelper;

    private static final int SELECT_PICTURE = 100;
    private static final int CAMERA_REQUEST = 1;
    private static final String TAG = "StoreImageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        localDatabaseHelper = new LocalDatabaseHelper(this);
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

        profileImage.setOnClickListener(v -> {
            drawer.closeDrawer(GravityCompat.START);
            showPopup(profileImage);
            drawer.openDrawer(GravityCompat.START);
        });

        //this.deleteDatabase("Images.db");
        loadImageFromDB();

    }


    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        } else if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
            pressedTime = System.currentTimeMillis();
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

    // Header Photo methods.
    public void showPopup(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.image_menu);
        popupMenu.show();
    }

    //This will open the gallery

    public void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @SuppressLint("QueryPermissionsNeeded")
    public void openCamera() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            //Create a file to store the image

            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.android_group10.needy.provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI);
                startActivityForResult(pictureIntent,
                        CAMERA_REQUEST);
            }
        }

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.change_image:

                //This will open the gallery
                openImageChooser();
                return true;

            case R.id.delete_image:
                localDatabaseHelper.deleteImage(userId);
                profileImage.setImageResource(R.drawable.anonymous_mask);
                return true;

            case R.id.take_image:
                openCamera();
                return true;

            default:
                return false;
        }
    }


    // will update the header and Local database.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                profileImage.setImageURI(selectedImageUri);
                saveImageInDB();

            } else if (requestCode == CAMERA_REQUEST) {
                Glide.with(this).load(imageFilePath).into(profileImage);

                selectedImageUri = Uri.fromFile(photoFile);
                saveImageInDB();
            }

        }
    }

    public File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    // Retrieve the image from the database.
    public void loadImageFromDB() {
        // check first if there is an image in the database(Check if the table is empty or not)
        boolean empty = localDatabaseHelper.checkTableEmpty();

        if (empty) {
            // if the table in the database is empty so will set back the default image
            profileImage.setImageResource(R.drawable.anonymous_mask);
        } else {
            new Thread((Runnable) () -> {
                try {
                    localDatabaseHelper.open();
                    final byte[] bytes = localDatabaseHelper.retreiveImageFromDB(userId);
                    localDatabaseHelper.close();
                    // Show Image from DB in ImageView
                    profileImage.post((Runnable) () -> {
                        try {

                            profileImage.setImageBitmap(DbBitmapUtility.getImage(bytes));
                        } catch (Exception e) {
                            profileImage.setImageResource(R.drawable.anonymous_mask);
                        }
                        Toast.makeText(MainActivity.this, "Image loaded from database", Toast.LENGTH_SHORT).show();
                    });
                } catch (Exception e) {
                    localDatabaseHelper.close();
                }
            }).start();
        }
    }

    // Save the images has been picked from the gallery or the Camera into the SQLite database.
    public void saveImageInDB() {
        try {
            localDatabaseHelper.open();
            InputStream iStream = getContentResolver().openInputStream(selectedImageUri);
            byte[] inputData = DbBitmapUtility.getBytes(iStream);
            localDatabaseHelper.insertImage(inputData, userId);
            localDatabaseHelper.close();
            Log.d("insertImage", String.valueOf(LocalDatabaseHelper.newImage));
        } catch (IOException ioe) {
            Log.e(TAG, "<saveImageInDB> Error : " + ioe.getLocalizedMessage());
            localDatabaseHelper.close();
        }

    }
}