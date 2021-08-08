package me.ketrab2004.contactlenses;

import android.text.format.DateFormat;
import android.util.Log;
import android.util.Pair;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class LensesSettings {
    public static final int milliInSecond =    1_000;
    public static final int milliInMinute =   60_000;
    public static final int milliInHour =  3_600_000;
    public static final int milliInDay =  86_400_000;

    /** How long you can wear your lenses */
    public float lensMaxWearTime = 12f;
    /** Amount of hours a notification should show earlier*/
    public float earlyRemoveLenses = 4f;
    /** Whether a early take of lenses notification should appear */
    public boolean earlyRemoveLensesNotification = true;

    /** Time when user started wearing their left lens */
    public Date startLensLeft = new Date(0);
    /** Time when user started wearing their right lens */
    public Date startLensRight = new Date(0);

    /** Amount of days you can wear these lenses before replacing them */
    public int lensMaxUses = 30;
    /** Replace lenses notification time (hours) */
    public Time replaceLensesNotification = new Time((long) (milliInHour * 8.5f)); //8.5 hours is 8:30

    /** How many days a notification should show telling you that you need to replace your lenses */
    public int earlyReplaceLenses = 7;
    /** Whether a notification should show before you need to replace your lenses */
    public boolean earlyReplaceLensesNotification = true;

    //** Date when started wearing new lens for left eye */
    public Date newLensLeft = new Date(0);
    //** Date when started wearing new lens for right eye */
    public Date newLensRight = new Date(0);

    /** How many days have been skipped (get added to max uses) */
    public int skippedDaysLeft = 0;
    public int skippedDaysRight = 0;
    /** Whether today is being skipped */
    public boolean skipToday = false;
    /** Day on which to skip */
    public Date skipDay = new Date(0);

    public static long roundToDay(long in){    return (long) (Math.floor( (double)in / (double)milliInDay ) * milliInDay);  }

    /** Checks if skipDay should be reset
     * @return whether or not skipDay was changed
     */
    public Boolean resetSkipDay(){
        if (skipToday){
            Date now = new Date( roundToDay(System.currentTimeMillis()) );
            if (skipDay.before(now)){ //skipday has passed
                skipToday = false;
                skippedDaysLeft++;
                skippedDaysRight++;

                skipDay = new Date( 0 );

                return true;
            }
            //else because after return
            skipDay = new Date( roundToDay(System.currentTimeMillis()) );
        }
        return false;
    }

    /** Returns clock that shows how long until left lens needs to be removed and whether its negative
     * @return {@link Pair}<{@link String}, {@link Boolean}>
     */
    public Pair<String, Boolean> timerDayLeft(){
        String out = "--:--:--";
        Boolean isNegative = false;

        if ( !startLensLeft.equals(new Time(0)) ){ //if lens is in
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.UK);
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

            Date now = new Date(System.currentTimeMillis());
            Date end = new Date( startLensLeft.getTime() + (long)Math.round(lensMaxWearTime * milliInHour)); //start time + maxWearTime*(millis in hour)

            long diff = end.getTime() - now.getTime();
            if ( diff < 0 ){ //if now is after end
                isNegative = true;
            }

            out = formatter.format(new Time( Math.abs( diff ) ));
        }

        return new Pair(out, isNegative);
    }

    /** Returns clock that shows how long until right lens needs to be removed and whether its negative
     * @return {@link Pair}<{@link String}, {@link Boolean}>
     */
    public Pair<String, Boolean> timerDayRight(){
        String out = "--:--:--";
        Boolean isNegative = false;

        if ( !startLensRight.equals(new Time(0)) ){ //if lens is in
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.UK);
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

            Date now = new Date(System.currentTimeMillis());
            Date end = new Date( startLensRight.getTime() + (long)Math.round(lensMaxWearTime * milliInHour)); //start time + maxWearTime*(millis in hour)

            long diff = end.getTime() - now.getTime();
            if ( diff < 0 ){ //if now is after end
                isNegative = true;
            }

            out = formatter.format(new Time( Math.abs( diff ) ));
        }

        return new Pair(out, isNegative);
    }

    /**
     * See at what time you need to remove left lens (and date if next day)
     * @return string that shows at what time you need to remove the left lens
     */
    public String lastDayLeft(){
        String out = "";
        if ( !startLensLeft.equals(new Time(0)) ) { //if lens is in
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.CANADA);
            Date end = new Date( startLensLeft.getTime() + (long)Math.round(lensMaxWearTime * milliInHour)); //start time + maxWearTime*(millis in hour)

            out = formatter.format(end);

            if ( roundToDay(end.getTime() +getTimezoneOffset()) != roundToDay(System.currentTimeMillis()) ){ //rounded to day, so if end is another day (add timezone to remove offset)
                formatter = new SimpleDateFormat("  dd/MM/yy", Locale.CANADA);

                out += formatter.format(end);
            }
        }
        return out;
    }

    /**
     * See at what time you need to remove right lens (and date if next day)
     * @return string that shows at what time you need to remove the right lens
     */
    public String lastDayRight(){
        String out = "";
        if ( !startLensRight.equals(new Time(0)) ) { //if lens is in
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.CANADA);
            Date end = new Date( startLensRight.getTime() + (long)Math.round(lensMaxWearTime * milliInHour)); //start time + maxWearTime*(millis in hour)

            out = formatter.format(end);

            if (roundToDay(end.getTime() +getTimezoneOffset()) != roundToDay(System.currentTimeMillis()) ){ //rounded to day, so if end is another day (add timezone to remove offset)
                formatter = new SimpleDateFormat("  dd/MM/yy", Locale.CANADA);

                out += formatter.format(end);
            }
        }
        return out;
    }

    public static long getTimezoneOffset() { return TimeZone.getDefault().getOffset(System.currentTimeMillis()); }

    public int getSkippedDaysLeft() { return (skippedDaysLeft + (skipToday ? 1 : 0)); }
    public int getSkippedDaysRight() { return (skippedDaysRight + (skipToday ? 1 : 0)); }
    /** Returns int that shows how long until left lens needs to be replaced and whether its negative
     * @return {@link Pair}<{@link String}, {@link Boolean}>
     */
    public Pair<String, Boolean> timerFullLeft(){
        String out = "--";
        Boolean isNegative = false;

        if ( !newLensLeft.equals(new Time(0)) ){ //if lens is in
            Date now = new Date( roundToDay(System.currentTimeMillis()) );
            Date end = new Date( roundToDay(newLensLeft.getTime() + (long)Math.round((double) lensMaxUses * (double)milliInDay) )); //start time + lensMaxUses* (milli in day)
            //Have to cast to double and then long otherwise it gives the wrong output

            long diff = end.getTime() - now.getTime()       + getSkippedDaysLeft() * milliInDay;
            out = String.valueOf( Math.abs(Math.round(diff / milliInDay) )); //(so replace on 0)

            if ( diff <= 0 ){ //if now is after end
                isNegative = true;
            }
        }

        return new Pair(out, isNegative);
    }

    /** Returns int that shows how long until right lens needs to be replaced and whether its negative
     * @return {@link Pair}<{@link String}, {@link Boolean}>
     */
    public Pair<String, Boolean> timerFullRight(){
        String out = "--";
        Boolean isNegative = false;

        if ( !newLensRight.equals(new Time(0)) ){ //if lens is in
            Date now = new Date( roundToDay(System.currentTimeMillis()) );
            Date end = new Date( roundToDay(newLensRight.getTime() + (long)Math.round((double) lensMaxUses * (double)milliInDay) )); //start time + lensMaxUses* (milli in day)
            //Have to cast to double and then long otherwise it gives the wrong output

            long diff = end.getTime() - now.getTime()       + getSkippedDaysRight() * milliInDay;
            out = String.valueOf( Math.abs(Math.round(diff / milliInDay) ));

            if ( diff <= 0 ){ //if now is after end
                isNegative = true;
            }
        }

        return new Pair(out, isNegative);
    }

    /**
     * See on what day you need to replace left lens
     * @return string that shows on what date you need to replace the left lens
     */
    public String lastFullLeft(){
        if ( !newLensLeft.equals(new Time(0)) ) { //if lens is in
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.CANADA);
            Date end = new Date( newLensLeft.getTime() + (long)Math.round((double)(lensMaxUses + getSkippedDaysLeft()) * (double)milliInDay)); //start time + maxWearTime*(millis in hour)

            return formatter.format(end);
        }
        return "";
    }

    /**
     * See on what day you need to replace your right lens
     * @return string that shows on what date you need to replace your right lens
     */
    public String lastFullRight(){
        if ( !newLensRight.equals(new Time(0)) ) { //if lens is in
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.CANADA);
            Date end = new Date( newLensRight.getTime() + (long)Math.round((double)(lensMaxUses + getSkippedDaysRight()) * (double)milliInDay)); //start time + maxWearTime*(millis in hour)

            return formatter.format(end);
        }
        return "";
    }
}
