package me.ketrab2004.contactlenses.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Locale;

import me.ketrab2004.contactlenses.LensesSettings;
import me.ketrab2004.contactlenses.MainActivity;
import me.ketrab2004.contactlenses.R;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    ViewGroup viewGroup;

    public LensesSettings sets;

    TextView textMaxWearTime;
    TextView textEarlyRemoveLenses;
    CheckBox checkEarlyRemoveLenses;

    TextView textLensMaxUses;
    TextView buttonReplaceLensesTime;
    TextView textEarlyReplaceLenses;
    CheckBox checkEarlyReplaceLenses;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        updateSets(); //load settings into fragment

        viewGroup = container;

        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
    private LensesSettings updateSets(){ return sets = ((MainActivity)getActivity()).sets; }

    private void textChangedCheck(TextView v) {
        SharedPreferences.Editor editor = ((MainActivity)getActivity()).prefEditor;

        //switch case doesn't work with TextView
        if (v == textMaxWearTime){
            sets.lensMaxWearTime = Float.parseFloat( v.getText().toString() );

            editor.putFloat("lensMaxWearTime", sets.lensMaxWearTime);
        }else if (v == textEarlyRemoveLenses){
            sets.earlyRemoveLenses = Float.parseFloat( v.getText().toString() );

            editor.putFloat("earlyRemoveLenses", sets.earlyRemoveLenses);
        }else if (v == textLensMaxUses){
            sets.lensMaxUses = Integer.parseInt( v.getText().toString() );

            editor.putInt("lensMaxUses", sets.lensMaxUses);
        }else if (v == textEarlyReplaceLenses){
            sets.earlyReplaceLenses = Integer.parseInt( v.getText().toString() );

            editor.putInt("earlyReplaceLenses", sets.earlyReplaceLenses);
        }
    }

    private TextView.OnEditorActionListener onEditFunction = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (actionId){
                case EditorInfo.IME_ACTION_DONE:
                case EditorInfo.IME_ACTION_NEXT:
                case EditorInfo.IME_ACTION_PREVIOUS:

                    textChangedCheck(v);
                    return false; //return false so keyboard closes
            }
            return true;
        }
    };

    private View.OnFocusChangeListener onFocusFunction = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus){
                textChangedCheck((TextView) v);
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();

        //get views
        textMaxWearTime = (TextView) viewGroup.findViewById(R.id.lensDurationDay);
        textEarlyRemoveLenses = (TextView) viewGroup.findViewById(R.id.lensNotificationDay);
        checkEarlyRemoveLenses = (CheckBox) viewGroup.findViewById(R.id.lensNotificationDayEnabled);

        textLensMaxUses = (TextView) viewGroup.findViewById(R.id.lensDurationFull);
        buttonReplaceLensesTime = (TextView) viewGroup.findViewById(R.id.lensNotificationTimeFull);
        textEarlyReplaceLenses = (TextView) viewGroup.findViewById(R.id.lensNotificationFull);
        checkEarlyReplaceLenses = (CheckBox) viewGroup.findViewById(R.id.lensNotificationFullEnabled);

        loadInSettings();

        //Editor action
        textMaxWearTime.setOnEditorActionListener(onEditFunction);
        textEarlyReplaceLenses.setOnEditorActionListener(onEditFunction);

        textLensMaxUses.setOnEditorActionListener(onEditFunction);
        textEarlyRemoveLenses.setOnEditorActionListener(onEditFunction);
        textLensMaxUses.setOnEditorActionListener(onEditFunction);
        textEarlyReplaceLenses.setOnEditorActionListener(onEditFunction);

        //Focus changed
        textMaxWearTime.setOnFocusChangeListener(onFocusFunction);
        textEarlyReplaceLenses.setOnFocusChangeListener(onFocusFunction);

        textLensMaxUses.setOnFocusChangeListener(onFocusFunction);
        textEarlyRemoveLenses.setOnFocusChangeListener(onFocusFunction);
        textLensMaxUses.setOnFocusChangeListener(onFocusFunction);
        textEarlyReplaceLenses.setOnFocusChangeListener(onFocusFunction);
    }

    private void loadInSettings() {
        textMaxWearTime.setText( String.valueOf(sets.lensMaxWearTime) );
        textEarlyRemoveLenses.setText( String.valueOf(sets.earlyRemoveLenses) );
        checkEarlyRemoveLenses.setChecked( sets.earlyRemoveLensesNotification );

        textLensMaxUses.setText( String.valueOf(sets.lensMaxUses) );
        buttonReplaceLensesTime.setText( new SimpleDateFormat("HH:mm", Locale.CANADA).format( sets.replaceLensesNotification ) );
        textEarlyReplaceLenses.setText( String.valueOf( sets.earlyReplaceLenses ) );
        checkEarlyReplaceLenses.setChecked( sets.earlyReplaceLensesNotification );
    }
}