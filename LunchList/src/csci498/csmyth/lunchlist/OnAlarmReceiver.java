package csci498.csmyth.lunchlist;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class OnAlarmReceiver extends BroadcastReceiver {
	private static final String USE_NOTIFICATION_KEY = "use_notification";
	private static final int NOTIFY_ME_ID = 1337;
	private static final String TIME_NOTICE = "It's time for lunch!";
	private static final String TIME_NOTICE_EXTENDED = "It's time for lunch! Aren't you hungry?";
	private static final String APP_NAME = "LunchList";
	private static final int REQUEST_CODE = 0;
	private static final int NUM_FLAGS = 0;
	
	@Override
	public void onReceive(Context ctxt, Intent intent) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctxt);
		boolean useNotification = prefs.getBoolean(USE_NOTIFICATION_KEY, true);
		
		if (useNotification) {
			NotificationManager mgr = (NotificationManager)ctxt.getSystemService(Context.NOTIFICATION_SERVICE);
			Notification note = new Notification(R.drawable.stat_notify_chat, TIME_NOTICE, System.currentTimeMillis());
			PendingIntent pend_intent = PendingIntent.getActivity(ctxt, REQUEST_CODE, new Intent(ctxt, AlarmActivity.class), NUM_FLAGS);
			
			note.setLatestEventInfo(ctxt, APP_NAME, TIME_NOTICE_EXTENDED, pend_intent);
			note.flags |= Notification.FLAG_AUTO_CANCEL;
			
			mgr.notify(NOTIFY_ME_ID, note);
		} else {
			Intent i = new Intent(ctxt, AlarmActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			ctxt.startActivity(i);
		}
	}

}
