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

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mendhak.gpslogger.common.AppSettings;
import com.mendhak.gpslogger.common.Utilities;

import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SavedTrips extends ActionBarActivity {

    WebView browser;
    private org.slf4j.Logger tracer = LoggerFactory.getLogger(SavedTrips.class.getSimpleName());
    /**
     * Event raised when the form is created for the first time
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_saved_trips);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ListView listview = (ListView) findViewById(R.id.listView_saved_trips);
        // Get the files saved
        File gpxFolder = new File(AppSettings.getGpsLoggerFolder());
        List<File> files = new ArrayList<File>(Arrays.asList(Utilities.GetFilesInFolder(gpxFolder, new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.contains(".cycphillyBody") && !s.contains("zip");
            }
        })));
        ArrayList<String> filenames = new ArrayList<String>();
        for (int i = 0; i < files.size(); ++i) {
            filenames.add(files.get(i).getName());
        }
        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, filenames);
        listview.setAdapter(adapter);
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
