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


package com.mendhak.gpslogger.senders.http;


import com.mendhak.gpslogger.common.AppSettings;
import com.mendhak.gpslogger.common.Utilities;
import com.mendhak.gpslogger.common.events.UploadEvents;
import com.mendhak.gpslogger.senders.IFileSender;
import com.path.android.jobqueue.JobManager;
import de.greenrobot.event.EventBus;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

public class HttpHelper implements IFileSender {
    private static final org.slf4j.Logger tracer = LoggerFactory.getLogger(HttpHelper.class.getSimpleName());

    public HttpHelper() {
    }

    void TestHttp() {

        File gpxFolder = new File(AppSettings.getGpsLoggerFolder());
        if (!gpxFolder.exists()) {
            gpxFolder.mkdirs();
        }

        tracer.debug("Creating gpslogger_test.xml");
        File testFile = new File(gpxFolder.getPath(), "gpslogger_test.xml");

        try {
            if (!testFile.exists()) {
                testFile.createNewFile();

                FileOutputStream initialWriter = new FileOutputStream(testFile, true);
                BufferedOutputStream initialOutput = new BufferedOutputStream(initialWriter);

                initialOutput.write("<x>This is a test file</x>".getBytes());
                initialOutput.flush();
                initialOutput.close();

                Utilities.AddFileToMediaDatabase(testFile, "text/xml");
            }

        } catch (Exception ex) {
            EventBus.getDefault().post(new UploadEvents.Ftp(false));
        }

        JobManager jobManager = AppSettings.GetJobManager();
        jobManager.addJobInBackground(new HttpJob(testFile));
    }

    @Override
    public void UploadFile(List<File> files) {
        for (File f : files) {
            UploadFile(f);
        }
    }

    public void UploadFile(File f) {

        JobManager jobManager = AppSettings.GetJobManager();
        jobManager.addJobInBackground(new HttpJob(f));
    }

    @Override
    public boolean accept(File file, String s) {
        return true;
    }

}

