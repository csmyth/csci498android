package csci498.csmyth.lunchlist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DetailFragment extends Fragment {
	EditText name = null;
	EditText address = null;
	RadioGroup types = null;
	EditText notes = null;
	EditText feed = null;
	TextView location = null;
	
	RestaurantHelper helper = null;
	LocationManager locMgr = null;
	String restaurantId = null;
	double latitude = 0.0d;
	double longitude = 0.0d;
	
	private static final String UNAVAIL = "Sorry, the Internet is not available";
	private static final String LOCATION_SAVE_TEXT = "Location saved";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		helper.close();
	}
	
	@Override
	public void onPause() {
		save();
		locMgr.removeUpdates(onLocationChange);
		super.onPause();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.detail_form, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		locMgr = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
		
		name = (EditText)getView().findViewById(R.id.name);
		address = (EditText)getView().findViewById(R.id.addr);
		types = (RadioGroup)getView().findViewById(R.id.types);
		notes = (EditText)getView().findViewById(R.id.notes);
		feed = (EditText)getView().findViewById(R.id.feed);
		location = (TextView)getView().findViewById(R.id.location);
	}
	
	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		
		state.putString("name", name.getText().toString());
		state.putString("address", address.getText().toString());
		state.putString("notes", notes.getText().toString());
		state.putInt("type", types.getCheckedRadioButtonId());
	}
	
	@Override
	public void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);
		
		name.setText(state.getString("name"));
		address.setText(state.getString("address"));
		notes.setText(state.getString("notes"));
		types.check(state.getInt("type"));
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (restaurantId == null) {
			menu.findItem(R.id.location).setEnabled(false);
			menu.findItem(R.id.map).setEnabled(false);
		}
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(getActivity()).inflate(R.menu.details_option, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.feed) {
			if (isNetworkAvailable()) {
				Intent intent = new Intent(getActivity(), FeedActivity.class);
				
				intent.putExtra(FeedActivity.FEED_URL, feed.getText().toString());
				startActivity(intent);
			} else {
				Toast
					.makeText(getActivity(), UNAVAIL, Toast.LENGTH_LONG)
					.show();
			}
			return true;
		} else if (item.getItemId() == R.id.location) {
			locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, onLocationChange);
			return true;
		} else if (item.getItemId() == R.id.map) {
			Intent intent = new Intent(getActivity(), RestaurantMap.class);
			
			intent.putExtra(RestaurantMap.EXTRA_LATITUDE, latitude);
			intent.putExtra(RestaurantMap.EXTRA_LONGITUDE, longitude);
			intent.putExtra(RestaurantMap.EXTRA_NAME, name.getText().toString());
			
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return (info != null);
	}
	
	LocationListener onLocationChange = new LocationListener() {
		public void onLocationChanged(Location fix) {
			helper.updateLocation(restaurantId, fix.getLatitude(), fix.getLongitude());
			location.setText(String.valueOf(fix.getLatitude()) + ", " + String.valueOf(fix.getLongitude()));
			locMgr.removeUpdates(onLocationChange);
			
			Toast
				.makeText(getActivity(), LOCATION_SAVE_TEXT, Toast.LENGTH_LONG)
				.show();
		}
		
		public void onProviderDisabled(String provider) {
			// required for interface, not used
		}
		
		public void onProviderEnabled(String provider) {
			// required for interface, not used
		}
		
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// required for interface, not used
		}
	};
	
	private void load() {
		Cursor c = helper.getById(restaurantId);
		
		c.moveToFirst();
		
		name.setText(helper.getName(c));
		address.setText(helper.getAddress(c));
		notes.setText(helper.getNotes(c));
		feed.setText(helper.getFeed(c));
		latitude = helper.getLatitude(c);
		longitude = helper.getLongitude(c);
		location.setText(String.valueOf(helper.getLocation(c)));
		
		if (helper.getType(c).equals("@string/sit_down")) {
			types.check(R.id.sit_down);
		} else if (helper.getType(c).equals("@string/take_out")) {
			types.check(R.id.take_out);
		} else {
			types.check(R.id.delivery);
		}
		
		c.close();
	}
		
	private void save() {
		if (name.getText().toString().length() > 0) {
			String type = null;
		
			switch (types.getCheckedRadioButtonId()) {
				case R.id.sit_down:
					type = "@string/sit_down";
					break;
					
				case R.id.take_out:
					type = "@string/take_out";
					break;
					
				default:
					type = "@string/delivery";
					break;
			}
		
			if (restaurantId == null) {
				helper.insert(name.getText().toString(), address.getText().toString(), type, notes.getText().toString(), feed.getText().toString());
			} else {
				helper.update(restaurantId, name.getText().toString(), address.getText().toString(), type, notes.getText().toString(), feed.getText().toString());
			}
		}
	}
	
}
