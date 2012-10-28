package csci498.csmyth.lunchlist;

import android.app.IntentService;
import android.content.Intent;

public class FeedService extends IntentService {
	public static final String FEED_SERVICE_NAME = "FeedService";
	
	public FeedService() {
		super(FEED_SERVICE_NAME);
	}
	
	@Override
	public void onHandleIntent(Intent intent) {
		// TODO: do something
	}

}
