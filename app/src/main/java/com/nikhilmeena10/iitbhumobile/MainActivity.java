package com.nikhilmeena10.iitbhumobile;

import android.*;
import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static java.security.AccessController.getContext;

@SuppressWarnings("WeakerAccess")
public class MainActivity extends AppCompatActivity {

    private LinearLayout toMap, toCal, toHol, toTPO, toFees;

    public final static int REQUEST_CODE = 10101;


    static final Integer LOCATION = 0x1;
    static final Integer CALL = 0x2;
    static final Integer WRITE_EXST = 0x3;
    static final Integer READ_EXST = 0x4;
    static final Integer CAMERA = 0x5;
    static final Integer ACCOUNTS = 0x6;
    static final Integer GPS_SETTINGS = 0x7;

    GoogleApiClient client;
    LocationRequest mLocationRequest;
    PendingResult<LocationSettingsResult> result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getSupportActionBar().hide();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        /*if (checkDrawOverlayPermission()) {
            //startService(new Intent(this, PowerButtonService.class));
        }*/

        client = new GoogleApiClient.Builder(this)
                .addApi(AppIndex.API)
                .addApi(LocationServices.API)
                .build();


        //Starting Maps Activity
        toMap = (LinearLayout) findViewById(R.id.map_button);
        toMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent geoIntent = new Intent(MainActivity.this, MapsActivity.class);
                geoIntent.putExtra("latitude", 25.262345);
                geoIntent.putExtra("longitude", 82.989608);
                startActivity(geoIntent);
            }
        });


        //Starting Calendar Activity
        toCal = (LinearLayout) findViewById(R.id.calendar_button);
        toCal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(isPermissionAllowed(WRITE_EXST)) {
                    new OpenLocalPDF(getApplicationContext(), "calendar_17_18.pdf").execute();
                }

                else {
                    ask(WRITE_EXST);
                }

            }
        });


        //Starting Holidays Activity
        toHol = (LinearLayout) findViewById(R.id.holidays_button);
        toHol.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(isPermissionAllowed(WRITE_EXST)) {
                    new OpenLocalPDF(getApplicationContext(), "holidays_2017_IITBHU.pdf").execute();
                }

                else {
                    ask(WRITE_EXST);
                }

            }
        });


        //Starting TPO Activity
        toTPO = (LinearLayout) findViewById(R.id.tpo_button);
        toTPO.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TPOActivity.class);
                startActivity(intent);
            }
        });


        //Starting Pay Fees Activity
        toFees = (LinearLayout) findViewById(R.id.fees_button);
        toFees.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.onlinesbi.com/prelogin/icollecthome.htm"));
                startActivity(browserIntent);
            }
        });

    }


    /*public boolean checkDrawOverlayPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }*/

    /*@Override
    @TargetApi(Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (Settings.canDrawOverlays(this)) {
                //startService(new Intent(this, PowerButtonService.class));
            }
        }
    }*/


    private boolean isPermissionAllowed(Integer permCode) {
        switch(permCode) {
            case 0x1: return ((ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) == PackageManager.PERMISSION_GRANTED);

            case 0x2: return ((ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)) == PackageManager.PERMISSION_GRANTED);

            case 0x3: return ((ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) == PackageManager.PERMISSION_GRANTED);

            case 0x4: return ((ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) == PackageManager.PERMISSION_GRANTED);

            case 0x5: return ((ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)) == PackageManager.PERMISSION_GRANTED);

            case 0x6: return ((ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.GET_ACCOUNTS)) == PackageManager.PERMISSION_GRANTED);

        }
        return false;
    }

    public void ask(Integer permCode){
        switch (permCode){
            case 0x1:
                askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,LOCATION);
                break;
            case 0x2:
                askForPermission(Manifest.permission.CALL_PHONE,CALL);
                break;
            case 0x3:
                askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,WRITE_EXST);
                break;
            case 0x4:
                askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE,READ_EXST);
                break;
            case 0x5:
                askForPermission(Manifest.permission.CAMERA,CAMERA);
                break;
            case 0x6:
                askForPermission(Manifest.permission.GET_ACCOUNTS,ACCOUNTS);
                break;
            default:
                break;
        }
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
            switch (requestCode) {
                //Location
                case 1:
                    askForGPS();
                    break;
                //Call
                case 2:
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + "{This is a telephone number}"));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        startActivity(callIntent);
                    }
                    break;
                //Write external Storage
                case 3:
                    break;
                //Read External Storage
                case 4:
                    Intent imageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(imageIntent, 11);
                    break;
                //Camera
                case 5:
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, 12);
                    }
                    break;
                //Accounts
                case 6:
                    AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
                    Account[] list = manager.getAccounts();
                    Toast.makeText(this,""+list[0].name,Toast.LENGTH_SHORT).show();
                    for(int i=0; i<list.length;i++){
                        Log.e("Account "+i,""+list[i].name);
                    }
            }


        }else{

        }

    }

    private void askForGPS(){
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        result = LocationServices.SettingsApi.checkLocationSettings(client, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(MainActivity.this, GPS_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }
}