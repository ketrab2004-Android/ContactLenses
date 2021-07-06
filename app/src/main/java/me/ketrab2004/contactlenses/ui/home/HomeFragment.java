package me.ketrab2004.contactlenses.ui.home;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModelProvider;

import java.sql.Time;

import me.ketrab2004.contactlenses.LensesSettings;
import me.ketrab2004.contactlenses.MainActivity;
import me.ketrab2004.contactlenses.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    ViewGroup viewGroup;

    public LensesSettings sets;

    TextView leftDayCountdown;
    TextView rightDayCountdown;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        updateSets(); //load settings into fragment

        viewGroup = container;

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private LensesSettings updateSets(){
        sets = ((MainActivity)getActivity()).sets;
        return sets;
    }

    @Override
    public void onStart() {
        super.onStart();

        leftDayCountdown = (TextView) viewGroup.findViewById(R.id.CountdownDayLeft);
        rightDayCountdown = (TextView) viewGroup.findViewById(R.id.CountdownDayRight);

        if (updateClockThread.getState() == Thread.State.NEW)
        {
            updateClockThread.start();
        }

        if (!sets.startLensLeft.equals(new Time(0))){ //if lens left counter has started
            TextView button = viewGroup.findViewById(R.id.buttonDayLeftToggle);

            button.setText(R.string.home_stop_left); //change play button to stop button
            button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_home_stop_24dp, 0,0,0);
        }
        if (!sets.startLensRight.equals(new Time(0))){ //if lens right counter has started
            TextView button = viewGroup.findViewById(R.id.buttonDayRightToggle);

            button.setText(R.string.home_stop_right); //change play button to stop button
            button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_home_stop_24dp, 0,0,0);
        }
    }
    
    private void updateCountdownDay(){
        Pair<String, Boolean> left = sets.timerDayLeft();
        leftDayCountdown.setText(left.first);

        Pair<String, Boolean> right = sets.timerDayRight();
        rightDayCountdown.setText(right.first);

        if (getContext() != null) {
            Resources.Theme theme = getContext().getTheme();
            TypedValue typedValue = new TypedValue();
            if (left.second) {
                theme.resolveAttribute(R.attr.counterOverflowTextColor, typedValue, true);
            } else {
                theme.resolveAttribute(R.attr.counterTextColor, typedValue, true);
            }
            leftDayCountdown.setTextColor(typedValue.data);

            typedValue = new TypedValue();
            if (right.second) {
                theme.resolveAttribute(R.attr.counterOverflowTextColor, typedValue, true);
            } else {
                theme.resolveAttribute(R.attr.counterTextColor, typedValue, true);
            }
            rightDayCountdown.setTextColor(typedValue.data);
        }
    }

    Boolean updateClockLoop = true;
    private Thread updateClockThread = new Thread() {
        @Override
        public void run() {
            try {
                while(updateClockLoop) {
                    updateCountdownDay();

                    sleep(999);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
}