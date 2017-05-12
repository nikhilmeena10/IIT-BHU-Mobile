package com.nikhilmeena10.iitbhumobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class TPOActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tpo);

        //Starting TPO WebView
        WebView tpowv = (WebView)findViewById(R.id.tpowebview);
        tpowv.loadUrl("http://www.placement.iitbhu.ac.in/");
    }
}
