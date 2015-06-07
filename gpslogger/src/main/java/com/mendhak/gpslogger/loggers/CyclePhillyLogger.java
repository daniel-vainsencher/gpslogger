/**  Cycle Philly, Copyright 2014 Code for Philly
 *   
 *   Incorporated into GPSLogger for Princeton's deployment. Based on the CyclePhily codebase.
 *
 *   @author Lloyd Emelle <lloyd@codeforamerica.org>
 *   @author Christopher Le Dantec <ledantec@gatech.edu>
 *   @author Anhong Guo <guoanhong15@gmail.com>
 *
 *   Updated/Modified for Philly's app deployment. Based on the
 *   CycleTracks codebase for SFCTA and Cycle Atlanta.
 *
 *   CycleTracks, Copyright 2009,2010 San Francisco County Transportation Authority
 *                                    San Francisco, CA, USA
 *
 *   @author Billy Charlton <billy.charlton@sfcta.org>
 *
 *   This file is part of CycleTracks.
 *
 *   CycleTracks is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   CycleTracks is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with CycleTracks.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mendhak.gpslogger.loggers;

// test logger imports
import android.location.Location;
import com.mendhak.gpslogger.common.Utilities;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Locale;
import org.json.JSONObject;
import java.lang.String;
import java.text.SimpleDateFormat;



/*
*    This file is part of GPSLogger for Android.
*
*    GPSLogger for Android is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 2 of the License, or
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




/**
 * Writes a comma separated plain text file.<br/>
 * First line of file is a header with the logged fields: time,lat,lon,elevation,accuracy,bearing,speed
 *
 * @author Jeroen van Wilgenburg
 *         https://github.com/jvwilge/gpslogger/commit/a7d45bcc1d5012513ff2246022ce4da2708adf47
 */
public class CyclePhillyLogger implements IFileLogger {

    private File file;
    protected final String name = "TXT";

    public CyclePhillyLogger(File file) {
        this.file = file;
    }

    @Override
    public void Write(Location loc) throws Exception {
        if (!file.exists()) {
            file.createNewFile();
            Utilities.AddFileToMediaDatabase(file, "text/csv");
        }

        FileWriter writer = new FileWriter(file);
        BufferedWriter output = new BufferedWriter(writer);
        try {

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

            // Build JSON objects for each coordinate:
            JSONObject coord = new JSONObject();

            coord.put("rec", df.format(loc.getTime()));
            coord.put("lat", loc.getLatitude());
            coord.put("lon", loc.getLongitude());
            coord.put("alt", loc.getAltitude());
            coord.put("spd", loc.getSpeed());
            coord.put("hac", loc.getAccuracy());
            coord.put("vac", loc.getAccuracy());

            output.write(coord.toString());
            output.newLine();
            output.flush();
        }
        finally
        {
            output.close();
        }
    }

    @Override
    public void Annotate(String description, Location loc) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public String getName() {
        return name;
    }

}
