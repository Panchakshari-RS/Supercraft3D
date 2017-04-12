package com.trova.supercraft;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import static com.trova.supercraft.SuperCraftUtils.logInfo;


/**
 * Created by Trova on 11/3/2016.
 */
public class CommonDateTimeZone {
    String timezone;
    String time;
    String currDate;
    long messageId;
    long timemilliseconds;
    int timeZoneOffsetMilliSeconds;
    public CommonDateTimeZone() {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        this.timezone= tz.getDisplayName();
        this.timeZoneOffsetMilliSeconds=tz.getRawOffset();
        logInfo("zone", timezone);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        this.currDate= df.format(cal.getTime());
        logInfo("zone", currDate);
        df = new SimpleDateFormat("HH:mm");
        this.time= df.format(cal.getTime());
        logInfo("zone", time);
        this.messageId=System.currentTimeMillis();
        this.timemilliseconds=System.currentTimeMillis();
    }
}
