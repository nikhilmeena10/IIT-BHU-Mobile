package com.nikhilmeena10.iitbhumobile;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        //Starting Maps Activity
        final LinearLayout toMap = (LinearLayout)findViewById(R.id.map_button);
        toMap.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent geoIntent = new Intent(MainActivity.this, MapsActivity.class);
                geoIntent.putExtra("latitude",25.262345);
                geoIntent.putExtra("longitude",82.989608);
                startActivity(geoIntent);
            }
        });

    }
}
