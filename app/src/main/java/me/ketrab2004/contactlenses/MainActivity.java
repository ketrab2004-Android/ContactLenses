package me.ketrab2004.contactlenses;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.sql.Time;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public LensesSettings sets = new LensesSettings();
    public SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        //region Load settings into sets
        //lens wearing and notification
        sets.lensMaxWearTime = prefs.getFloat("lensMaxWearTime", sets.lensMaxWearTime);
        sets.earlyRemoveLenses = prefs.getFloat("earlyRemoveLenses", sets.earlyRemoveLenses);
        sets.earlyRemoveLensesNotification = prefs.getBoolean("earlyRemoveLensesNotification", sets.earlyRemoveLensesNotification);

        //lens wearing time
        sets.startLensLeft = new Time( prefs.getLong("startLensLeft", sets.startLensLeft.getTime()) );
        sets.startLensRight = new Time( prefs.getLong("startLensRight", sets.startLensRight.getTime()) );

        //lens replacing
        sets.lensMaxUses = prefs.getInt("lensMaxUses", sets.lensMaxUses);
        sets.replaceLensesNotification = new Time( prefs.getLong("replaceLensesNotification", sets.replaceLensesNotification.getTime()) );

        //lens wearing date
        sets.newLensLeft = new Date( prefs.getLong("newLensLeft", sets.newLensLeft.getTime()) );
        sets.newLensLeft = new Date( prefs.getLong("newLensRight", sets.newLensRight.getTime()) );

        //lens replace notification
        sets.earlyReplaceLenses = prefs.getInt("earlyReplaceLenses", sets.earlyReplaceLenses);
        sets.earlyReplaceLensesNotification = prefs.getBoolean("earlyReplaceLensesNotification", sets.earlyReplaceLensesNotification);

        //skip day stuff
        sets.skippedDays = prefs.getInt("skippedDays", sets.skippedDays);
        sets.skipToday = prefs.getBoolean("skipToday", sets.skipToday);
        sets.skipDay = new Date( prefs.getLong("skipDay", sets.skipDay.getTime()) );
        //endregion Load settings into sets

        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_settings) //list of nav menu items
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    //Start lens counter (day)
    public void toggleLeft(View button){
        if (!sets.startLensLeft.equals(new Time(0))){
            //stop counter
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_POSITIVE){
                        sets.startLensLeft = new Time(0);

                        ((TextView) button).setText(R.string.home_reset_left);
                        ((TextView) button).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_home_play_24dp, 0,0,0);
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to stop the counter for your left lens?").setPositiveButton("Stop", dialogClickListener)
                    .setNegativeButton("Cancel", dialogClickListener).show();
        }else{
            //start counter
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_POSITIVE){
                        sets.startLensLeft = new Time(System.currentTimeMillis());

                        ((TextView) button).setText(R.string.home_stop_left);
                        ((TextView) button).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_home_stop_24dp, 0,0,0);
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to start the counter for your left lens?").setPositiveButton("Start", dialogClickListener)
                    .setNegativeButton("Cancel", dialogClickListener).show();
        }
    }
    public void toggleRight(View button){
        if (!sets.startLensRight.equals(new Time(0))){
            //stop counter
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_POSITIVE){
                        sets.startLensRight = new Time(0);

                        ((TextView) button).setText(R.string.home_reset_right);
                        ((TextView) button).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_home_play_24dp, 0,0,0);
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to stop the counter for your right lens?").setPositiveButton("Stop", dialogClickListener)
                    .setNegativeButton("Cancel", dialogClickListener).show();
        }else{
            //start counter
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_POSITIVE){
                        sets.startLensRight = new Time(System.currentTimeMillis());

                        ((TextView) button).setText(R.string.home_stop_right);
                        ((TextView) button).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_home_stop_24dp, 0,0,0);
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to start the counter for your right lens?").setPositiveButton("Start", dialogClickListener)
                    .setNegativeButton("Cancel", dialogClickListener).show();
        }
    }

    //Reset lens counter (full)
    public void resetLeft(View button){
        Log.d("Debug", "Reset left " + button.getId());
    }
    public void resetRight(View button){
        Log.d("Debug", "Reset right " + button.getId());
    }

    //Skip today switch
    public void skipDay(View button){
        CheckBox checkBox = (CheckBox)button;
        Log.d("Debug", "Skip day " + button.getId() + " " + checkBox.isChecked());
    }

    int hour, minute;
    //Timepicker
    public void pickTime(View view){
        // time picker dialog
        TimePickerDialog picker = new TimePickerDialog(MainActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                        hour = sHour;
                        minute = sMinute;

                        TextView textInput = (TextView) view;
                        textInput.setText(String.format(Locale.UK, "%02d:%02d", hour, minute ));

                        //TODO save set time
                    }
                }, hour, minute, true);

        //picker.setTitle("Choose a time to");
        //picker.setMessage("display the \"replace contact lenses\" message.");
        picker.show();
    }
}