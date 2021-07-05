package me.ketrab2004.contactlenses;

import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public void startLeft(View button){
        Log.d("Debug", "Start left " + button.getId());
    }
    public void startRight(View button){
        Log.d("Debug", "Start right " + button.getId());
    }
    //Stop lens counter (day)
    public void stopLeft(View button){
        Log.d("Debug", "Stop left " + button.getId());
    }
    public void stopRight(View button){
        Log.d("Debug", "Stop right " + button.getId());
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