package csci498.csmyth.lunchlist;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class OnBootReceiver extends BroadcastReceiver {
	private static final String TIME_PREFERENCE_KEY = "alarm_time";
	private static final String DEFAULT_TIME = "12:00";
	private static final int SET_SECOND = 0;
	private static final int SET_MILLISECOND = 0;
	private static final int INCREMENT = 1;
	private static final int REQUEST_CODE = 0;
	private static final int NUM_FLAGS = 0;
	
	@Override
	public void onReceive(Context ctxt, Intent intent) {
		setAlarm(ctxt);
	}
	
	public static void setAlarm(Context ctxt) {
		AlarmManager mgr = (AlarmManager)ctxt.getSystemService(Context.ALARM_SERVICE);
		Calendar cal = Calendar.getInstance();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctxt);
		String time = prefs.getString(TIME_PREFERENCE_KEY, DEFAULT_TIME);
		
		cal.set(Calendar.HOUR_OF_DAY, TimePreference.getHour(time));
		cal.set(Calendar.MINUTE, TimePreference.getMinute(time));
		cal.set(Calendar.SECOND, SET_SECOND);
		cal.set(Calendar.MILLISECOND, SET_MILLISECOND);
		
		if (cal.getTimeInMillis() < System.currentTimeMillis()) {
			cal.add(Calendar.DAY_OF_YEAR, INCREMENT);
		}
		
		mgr.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, getPendingIntent(ctxt));	
	}
	
	public static void cancelAlarm(Context ctxt) {
		AlarmManager mgr = (AlarmManager)ctxt.getSystemService(Context.ALARM_SERVICE);
		mgr.cancel(getPendingIntent(ctxt));
	}
	
	private static PendingIntent getPendingIntent(Context ctxt) {
		Intent intent = new Intent(ctxt, OnAlarmReceiver.class);
		return PendingIntent.getBroadcast(ctxt, REQUEST_CODE, intent, NUM_FLAGS);
	}

}
