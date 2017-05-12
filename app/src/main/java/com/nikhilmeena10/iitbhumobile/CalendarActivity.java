package com.nikhilmeena10.iitbhumobile;

import android.app.Activity;
        import android.app.DownloadManager;
        import android.app.DownloadManager.Query;
        import android.app.DownloadManager.Request;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.database.Cursor;
        import android.net.Uri;
        import android.os.Bundle;
        import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;

public class CalendarActivity extends Activity{

    /** Called when the activity is first created. */

    PhotoView calimage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calimage = (PhotoView) findViewById(R.id.calendarimage);
        //calimage.setImageResource(R.drawable.calendar_16_17even);

    }


}