package csci498.csmyth.lunchlist;

import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSReader;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.util.Log;

public class FeedActivity extends ListActivity {
	public static final String EXCP_TITLE = "Exception!";
	public static final String POS_BUTTON_TXT = "OK";
	
	private void goBlooey(Throwable thrwbl) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder
			.setTitle(EXCP_TITLE)
			.setMessage(thrwbl.toString())
			.setPositiveButton(POS_BUTTON_TXT, null)
			.show();
	}
	
	private static class FeedTask extends AsyncTask<String, Void, Void> {
		private RSSReader reader = new RSSReader();
		private Exception excp = null;
		private FeedActivity activity = null;
		
		private static final String APP_NAME = "LunchList";
		private static final String PARSE_FEED_EXCEPTION = "Exception parsing feed";
		
		FeedTask(FeedActivity activity) {
			attach(activity);
		}
		
		void attach(FeedActivity activity) {
			this.activity = activity;
		}
		
		void detach() {
			this.activity = null;
		}
		
		@Override
		public RSSFeed doInBackground(String... urls) {
			RSSFeed result = null;
			
			try {
				result = reader.load(urls[0]);
			} catch (Exception excp) {
				this.excp = excp;
			}
			return result;
		}
		
		@Override
		public void onPostExecute(RSSFeed feed) {
			if (excp == null) {
				activity.setFeed(feed);
			} else {
				Log.e(APP_NAME, PARSE_FEED_EXCEPTION, excp);
				activity.goBlooey(excp);
			}
		}
		
	}

}
