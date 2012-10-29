package com.android.systemui.statusbar.powerwidget;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.util.Log;

import com.android.systemui.R;


public class FChargeButton extends PowerButton {

    public static final String FAST_CHARGE_DIR = "/sys/kernel/fast_charge";
    public static final String FAST_CHARGE_FILE = "force_fast_charge";
	protected boolean on = true;

    public FChargeButton() { mType = BUTTON_FCHARGE;}

    @Override
    protected void updateState() {
        if (getFastChargesState(null)) {
            mIcon = R.drawable.stat_fcharge_on;
            mState = STATE_ENABLED;
        } else {
            mIcon = R.drawable.stat_fcharge_off;
            mState = STATE_DISABLED;
        }
    }

    @Override
    protected void toggleState() {
	try {
            on = !getFastChargesState(null);
            File fastcharge = new File(FAST_CHARGE_DIR, FAST_CHARGE_FILE);
            FileWriter fwriter = new FileWriter(fastcharge);
            BufferedWriter bwriter = new BufferedWriter(fwriter);
            bwriter.write(on ? "1" : "0");
            bwriter.close();
        } catch (IOException e) {
            Log.e("FChargeToggle", "Couldn't write fast_charge file");
        }

    }
    
    @Override
    protected boolean handleLongClick() {
         return true;
    }

    private static boolean getFastChargesState(Context context) {
        try {
            File fastcharge = new File(FAST_CHARGE_DIR, FAST_CHARGE_FILE);
            FileReader reader = new FileReader(fastcharge);
            BufferedReader breader = new BufferedReader(reader);
			String line = breader.readLine();
			breader.close();
            return (line.equals("1"));
        } catch (IOException e) {
            Log.e("FChargeToggle", "Couldn't read fast_charge file");
            return true;
        }
    }

}
