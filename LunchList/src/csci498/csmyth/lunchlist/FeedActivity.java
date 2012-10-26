package csci498.csmyth.lunchlist;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.util.Log;

public class FeedActivity extends ListActivity {
	private static class FeedTask extends AsyncTask<String, Void, Void> {
		private Exception excp = null;
		private FeedActivity activity = null;
		
		private static final String APP_NAME = "LunchList";
		private static final String PARSE_FEED_EXCEPTION = "Exception parsing feed";
		
		FeedTask(FeedActivity activity) {
			attach(activity);
		}
		
		@Override
		public Void doInBackground(String... urls) {
			try {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet getMethod = new HttpGet(urls[0]);
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				String responseBody = client.execute(getMethod, responseHandler);
				
				Log.d("FeedActivity", responseBody);
			} catch (Exception excp) {
				this.excp = excp;
			}
			return null;
		}
		
		@Override
		public void onPostExecute(Void unused) {
			if (excp == null) {
				// TODO
			} else {
				Log.e(APP_NAME, PARSE_FEED_EXCEPTION, excp);
				activity.goBlooey(excp);
			}
		}
		
	}

}
