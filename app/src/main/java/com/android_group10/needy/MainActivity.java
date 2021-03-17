package com.android_group10.needy;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android_group10.needy.LocalDatabase.DbBitmapUtility;
import com.android_group10.needy.LocalDatabase.LocalDatabaseHelper;
import com.android_group10.needy.ui.InNeed.InNeedFragmentDirections;
import com.android_group10.needy.ui.LogInAndRegistration.LogIn;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rom4ek.arcnavigationview.ArcNavigationView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, NavController.OnDestinationChangedListener {
    private String first_name_from_Facebook, user_email_from_Facebook;
    private DrawerLayout drawerLayout;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;
    private FloatingActionButton floatingActionButton;
    private String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private View headerView;
    private ImageView profileImage;


    private String imageFilePath;
    private File photoFile = null;
    private Uri selectedImageUri;
    private LocalDatabaseHelper localDatabaseHelper;

    private static final int SELECT_PICTURE = 100;
    private static final int CAMERA_REQUEST = 1;

    private static final String TAG = "StoreImageActivity";
    private ShareDialog shareDialog;
    private CallbackManager callbackManager;
    private TextView userNameOnHeader, userEmailOnHeader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        localDatabaseHelper = new LocalDatabaseHelper(this);
        initialization();
        getFbDetails();
        updateHeader();
        facebookSDKInitialize();

        profileImage.setOnClickListener(v -> {
            showPopup(profileImage);
        });

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                ProfilePictureManager ppManager = new ProfilePictureManager();
                ppManager.displayProfilePic(MainActivity.this, profileImage, true, userId);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }


    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        if (destination.getId() == R.id.in_need) {
            floatingActionButton.show();
        } else {
            floatingActionButton.hide();
        }
    }

    // Item menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.about_us) {
            about_us_dialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Can press back button and only close the navigation bar and not the activity
    @Override
    public void onBackPressed() {
        //This way the back stack behaves normally, TODO: find a way to also implement double tap exit
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    public void initialization() {
        drawerLayout = findViewById(R.id.drawer_layout);
        ArcNavigationView navigationView = findViewById(R.id.nav_view);

        //Set-up appbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        appBarConfiguration =
                new AppBarConfiguration.Builder(R.id.in_need, R.id.needs_and_deeds, R.id.messaging, R.id.profile)
                        .setOpenableLayout(drawerLayout)
                        .build();

        //Initialize navigation
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);
        }

        //Initialize logout and share buttons in drawer
        //Logout
        navigationView.getMenu().findItem(R.id.log_out).setOnMenuItemClickListener(menuItem -> {
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            startActivity(new Intent(this, LogIn.class));
            finish();
            return true;
        });
        //Share
        navigationView.getMenu().findItem(R.id.share).setOnMenuItemClickListener(menuItem -> {
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareOnFacebook shareOnFacebook = new ShareOnFacebook();
                shareDialog.show(shareOnFacebook.createShare());
            }
            return true;
        });

        //Initialize fab
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(view -> Snackbar.make(view, "Click here to add a help request", Snackbar.LENGTH_LONG)
                .setAction("New Need", v -> {
                    navController.navigate(InNeedFragmentDirections.actionInNeedToAddPost());
                }).show());
        floatingActionButton.setImageResource(R.drawable.add_icon);
        //Set listener for fab behaviour based on fragment
        navController.addOnDestinationChangedListener(this);

        headerView = navigationView.getHeaderView(0);
        profileImage = headerView.findViewById(R.id.profileImageHeader);

        userNameOnHeader = headerView.findViewById(R.id.user_name);
        userEmailOnHeader = headerView.findViewById(R.id.user_email_header);
    }


    //Update the User name and User email in the Header.
    public void updateHeader() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user != null ? user.getUid() : "ERROR_UID_MISSING";

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if (user != null) {

                    String name = "Welcome " + user.getFirstName();
                    String email = user.getEmail();
                    for (UserInfo userInfo : FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
                        if (userInfo.getProviderId().equals("facebook.com")) {
                            userNameOnHeader.setText(first_name_from_Facebook);
                            userEmailOnHeader.setText(user_email_from_Facebook);
                        }
                        if (!userInfo.getProviderId().equals("facebook.com")) {
                            userNameOnHeader.setText(name);
                            userEmailOnHeader.setText(email);
                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getFbDetails() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                (object, response) -> {

                    if (object != null) {
                        first_name_from_Facebook = object.optString("first_name");
                        user_email_from_Facebook = object.optString("email");

                    }

                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,first_name, last_name, email,link");
        request.setParameters(parameters);
        request.executeAsync();

    }

    // About us Dialog
    public void about_us_dialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.about_us, null);
        final AlertDialog alertD = new AlertDialog.Builder(this).create();
        alertD.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertD.setView(view);
        alertD.show();
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

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.change_image) {//This will open the gallery
            openImageChooser();
            return true;
        } else if (itemId == R.id.take_image) {
            openCamera();
            return true;
        }
        return false;
    }


    // will update the header and Local database.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ProfilePictureManager ppManager = new ProfilePictureManager();
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                profileImage.setImageURI(selectedImageUri);
                saveImageInDB();
                ppManager.uploadPicToRemote(selectedImageUri, userId, this);

            } else if (requestCode == CAMERA_REQUEST) {
                Glide.with(this).load(imageFilePath).into(profileImage);

                selectedImageUri = Uri.fromFile(photoFile);
                saveImageInDB();
                ppManager.uploadPicToRemote(selectedImageUri, userId, this);
            }
        }
        //This for sharing on facebook.
        callbackManager.onActivityResult(requestCode, resultCode, data);

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
    // Will keep this method for future work.
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
            Log.d("Inserted image:", String.valueOf(LocalDatabaseHelper.newImage));
            Log.i(TAG, "Inserted image");
        } catch (IOException ioe) {
            Log.e(TAG, "<saveImageInDB> Error : " + ioe.getLocalizedMessage());
            localDatabaseHelper.close();
        }

    }

    protected void facebookSDKInitialize() {
        //FacebookSdk.sdkInitialize(getApplicationContext()); //Deprecated, done automatically now
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
    }
}