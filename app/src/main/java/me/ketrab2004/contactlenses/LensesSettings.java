package me.ketrab2004.contactlenses;

import android.text.format.DateFormat;
import android.util.Pair;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LensesSettings {
    protected int milliInSecond =    1_000;
    protected int milliInMinute =   60_000;
    protected int milliInHour =  3_600_000;
    protected int milliInDay =  86_400_000;

    /** How long you can wear your lenses */
    public float lensMaxWearTime = 12f;
    /** Amount of hours a notification should show earlier*/
    public float earlyRemoveLenses = 4f;
    /** Whether a early take of lenses notification should appear */
    public boolean earlyRemoveLensesNotification = true;

    /** Time when user started wearing their left lens */
    public Time startLensLeft = new Time(0);
    /** Time when user started wearing their right lens */
    public Time startLensRight = new Time(0);

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
    public int skippedDays = 0;
    /** Whether today is being skipped */
    public boolean skipToday = false;
    /** Day on which to skip */
    public Date skipDay = new Date(0);

    /** Checks if skipDay should be reset */
    public void resetSkipDay(){
        if (skipToday){
            Date now = new Date(System.currentTimeMillis());
            if (skipDay.before(now)){ //skipday has passed
                skipToday = false;
                skippedDays++;
            }
        }
    }

    /** Returns clock that shows how long until left lens needs to be removed and whether its negative
     * @return Pair< String, Boolean >
     */
    public Pair<String, Boolean> timerDayLeft(){
        String out = "--:--:--";
        Boolean isNegative = false;

        if ( !startLensLeft.equals(new Time(0)) ){ //if lens is in
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.US);
            Time now = new Time(System.currentTimeMillis());
            Time end = new Time( startLensLeft.getTime() + (long)Math.round(lensMaxWearTime * milliInHour)); //start time + maxWearTime*(millis in hour)

            if ( now.getTime() - end.getTime() > 0 ){ //if now is after end
                isNegative = true;
            }

            out = formatter.format(new Time( Math.abs( end.getTime() - now.getTime() ) + (isNegative ? 0 : 1000) ));
        }

        return new Pair(out, isNegative);
    }

    /** Returns clock that shows how long until right lens needs to be removed and whether its negative
     * @return Pair< String, Boolean >
     */
    public Pair<String, Boolean> timerDayRight(){
        String out = "--:--:--";
        Boolean isNegative = false;

        if ( !startLensRight.equals(new Time(0)) ){ //if lens is in
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.US);
            Time now = new Time(System.currentTimeMillis());
            Time end = new Time( startLensRight.getTime() + (long)Math.round(lensMaxWearTime * milliInHour)); //start time + maxWearTime*(millis in hour)

            if ( now.getTime() - end.getTime() > 0 ){ //if now is after end
                isNegative = true;
            }

            out = formatter.format(new Time( Math.abs( end.getTime() - now.getTime() ) + (isNegative ? 0 : 1000) ));
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
            Time end = new Time( startLensLeft.getTime() + (long)Math.round(lensMaxWearTime * milliInHour)); //start time + maxWearTime*(millis in hour)

            out = formatter.format(end);

            Time start = new Time( (long) (Math.floor( startLensLeft.getTime() / (float)milliInDay ) * milliInDay)); //round time to day
            end = new Time( (long) (Math.floor( end.getTime() / (float)milliInDay ) * milliInDay)); //round time to day
            if (end.after(start)){ //rounded to day, so if end is on the day after start (or more)
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
            Time end = new Time( startLensRight.getTime() + (long)Math.round(lensMaxWearTime * milliInHour)); //start time + maxWearTime*(millis in hour)

            out = formatter.format(end);

            Time start = new Time( (long) (Math.floor( startLensRight.getTime() / (float)milliInDay ) * milliInDay)); //round time to day
            end = new Time( (long) (Math.floor( end.getTime() / (float)milliInDay ) * milliInDay)); //round time to day
            if (end.after(start)){ //rounded to day, so if end is on the day after start (or more)
                formatter = new SimpleDateFormat("  dd/MM/yy", Locale.CANADA);

                out += formatter.format(end);
            }
        }
        return out;
    }

    /** Returns int that shows how long until left lens needs to be replaced and whether its negative
     * @return Pair< String, Boolean >
     */
    public Pair<Integer, Boolean> timerFullLeft(){
        Integer out = 0;
        Boolean isNegative = false;

        if ( !newLensLeft.equals(new Date(0)) ){ //if you have new lens
            Date now = new Date(System.currentTimeMillis());

            out = Integer.parseInt( DateFormat.format("dd", now.compareTo(newLensLeft)).toString() );

            if ( (newLensLeft.getTime() - (long)Math.round(lensMaxUses * 86400000))% 86400000 >= 1){ //if now is more than a day after startLens + lensMaxWearTime
                isNegative = true;
            }
        }

        return new Pair(out, isNegative);
    }

    /** Returns int that shows how long until right lens needs to be replaced and whether its negative
     * @return Pair< String, Boolean >
     */
    public Pair<Integer, Boolean> timerFullRight(){
        Integer out = 0;
        Boolean isNegative = false;

        if ( !newLensRight.equals(new Date(0)) ){ //if you have new lens
            Date now = new Date(System.currentTimeMillis());

            out = Integer.parseInt( DateFormat.format("dd", now.compareTo(newLensRight)).toString() );

            if ( (newLensRight.getTime() - (long)Math.round(lensMaxUses * 86400000))% 86400000 >= 1){ //if now is more than a day after startLens + lensMaxWearTime
                isNegative = true;
            }
        }

        return new Pair(out, isNegative);
    }
}
