/*
*    This file is part of GPSLogger for Android.
*
*    GPSLogger for Android is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    GPSLogger for Android is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with GPSLogger for Android.  If not, see <http://www.gnu.org/licenses/>.
*/


package com.mendhak.gpslogger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mendhak.gpslogger.common.AppSettings;
import com.mendhak.gpslogger.common.Utilities;
import com.mendhak.gpslogger.views.GpsSimpleViewFragment;

import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewTrip extends ActionBarActivity {

    WebView browser;
    private org.slf4j.Logger tracer = LoggerFactory.getLogger(NewTrip.class.getSimpleName());
    /**
     * Event raised when the form is created for the first time
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_trip);

        // Fetch filename of the tracked data
        Intent intent = getIntent();
        String filename = intent.getStringExtra(GpsSimpleViewFragment.FILE_TO_SEND);

        // Get and set TextView for the file to be sent
        TextView textView_filename = (TextView) findViewById(R.id.newTrip_filename);
        textView_filename.setText(filename);

        // Choose Trip type
        Spinner spinner = (Spinner) findViewById(R.id.newTrip_tripTypeSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.trip_types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // Set up the toolbar with the Activity Name
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onDestroy() {
        setVisible(false);
        super.onDestroy();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setVisible(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
