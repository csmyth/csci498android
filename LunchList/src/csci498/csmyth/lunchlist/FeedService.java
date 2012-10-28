package csci498.csmyth.lunchlist;

import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSReader;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class FeedService extends IntentService {
	private static final String APP_NAME = "LunchList";
	private static final String PARSE_FEED_EXCEPTION = "Exception parsing feed";
	
	public static final String FEED_SERVICE_NAME = "FeedService";
	public static final String EXTRA_URL = "csci498.csmyth.lunchlist.EXTRA_URL";
	
	public FeedService() {
		super(FEED_SERVICE_NAME);
	}
	
	@Override
	public void onHandleIntent(Intent intent) {
		RSSReader reader = new RSSReader();
		
		try {
			RSSFeed result = reader.load(intent.getStringExtra(EXTRA_URL));
		} catch (Exception excp) {
			Log.e(APP_NAME, PARSE_FEED_EXCEPTION, excp);
		}
	}

}
