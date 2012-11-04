package csci498.csmyth.lunchlist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DetailForm extends Activity {
	EditText name = null;
	EditText address = null;
	RadioGroup types = null;
	EditText notes = null;
	EditText feed = null;
	TextView location = null;
	
	RestaurantHelper helper = null;
	String restaurantId = null;
	
	private static final String UNAVAIL = "Sorry, the Internet is not available";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_form);
		
		helper = new RestaurantHelper(this);
        
        name = (EditText)findViewById(R.id.name);
		address = (EditText)findViewById(R.id.addr);
		types = (RadioGroup)findViewById(R.id.types);
		notes = (EditText)findViewById(R.id.notes);
		feed = (EditText)findViewById(R.id.feed);
        
        restaurantId = getIntent().getStringExtra(LunchList.ID_EXTRA);
        
        if (restaurantId != null) {
        	load();
        }
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		helper.close();
	}
	
	@Override
	public void onPause() {
		save();
		super.onPause();
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
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.details_option, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.feed) {
			if (isNetworkAvailable()) {
				Intent intent = new Intent(this, FeedActivity.class);
				
				intent.putExtra(FeedActivity.FEED_URL, feed.getText().toString());
				startActivity(intent);
			} else {
				Toast
					.makeText(this, UNAVAIL, Toast.LENGTH_LONG)
					.show();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return (info != null);
	}
	
	private void load() {
		Cursor c = helper.getById(restaurantId);
		
		c.moveToFirst();
		
		name.setText(helper.getName(c));
		address.setText(helper.getAddress(c));
		notes.setText(helper.getNotes(c));
		feed.setText(helper.getFeed(c));
		
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
