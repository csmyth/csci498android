package csci498.csmyth.lunchlist;

import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSReader;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public class FeedService extends IntentService {
	private static final String APP_NAME = "LunchList";
	private static final String PARSE_FEED_EXCEPTION = "Exception parsing feed";
	private static final String SEND_RESULTS_EXCEPTION = "Exception sending results to activity";
	
	public static final String FEED_SERVICE_NAME = "FeedService";
	public static final String EXTRA_URL = "csci498.csmyth.lunchlist.EXTRA_URL";
	public static final String EXTRA_MESSENGER = "csci498.csmyth.lunchlist.EXTRA_MESSENGER";
	
	public FeedService() {
		super(FEED_SERVICE_NAME);
	}
	
	@Override
	public void onHandleIntent(Intent intent) {
		RSSReader reader = new RSSReader();
		Messenger messenger = (Messenger)intent.getExtras().get(EXTRA_MESSENGER);
		Message msg = Message.obtain();
		
		try {
			RSSFeed result = reader.load(intent.getStringExtra(EXTRA_URL));
			msg.arg1 = Activity.RESULT_OK;
			msg.obj = result;
		} catch (Exception excp) {
			Log.e(APP_NAME, PARSE_FEED_EXCEPTION, excp);
			msg.arg1 = Activity.RESULT_CANCELED;
			msg.obj = excp;
		}
		
		try {
			messenger.send(msg);
		} catch (Exception excp) {
			Log.w(APP_NAME, SEND_RESULTS_EXCEPTION, excp);
		}
	}

}
