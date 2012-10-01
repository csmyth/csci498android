package csci498.csmyth.lunchlist;

import android.os.Bundle;
import android.app.ListActivity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

public class LunchList extends ListActivity {
	Cursor model = null;
	EditText name = null;
	EditText address = null;
	RadioGroup types = null;
	EditText notes = null;
	RestaurantHelper helper = null;
	RestaurantAdapter adapter = null;
	
	public final static String ID_EXTRA = "csci498.csmyth.lunchlist._ID";
	
	static final int LIST_TAB = 0;
	static final int DETAIL_TAB = 1;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        helper = new RestaurantHelper(this);
        model = helper.getAll();
        startManagingCursor(model);
        adapter = new RestaurantAdapter(model);
        setListAdapter(adapter);
        
    }
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		helper.close();
	}
	
	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent i = new Intent(LunchList.this, DetailForm.class);
			
			startActivity(i);
		}
	};
	
	class RestaurantAdapter extends CursorAdapter {
		RestaurantAdapter(Cursor c) {
			super(LunchList.this, c);
		}
		
		@Override
		public void bindView(View row, Context ctxt, Cursor c) {
			RestaurantHolder holder = (RestaurantHolder)row.getTag();
			
			holder.populateFrom(c, helper);
		}
		
		@Override
		public View newView(Context ctxt, Cursor c, ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
			View row = inflater.inflate(R.layout.row, parent, false);
			RestaurantHolder holder = new RestaurantHolder(row);
			
			row.setTag(holder);
		
			return (row);
		}
	}
	
	static class RestaurantHolder {
		private TextView name = null;
		private TextView address = null;
		private ImageView icon = null;
		
		RestaurantHolder(View row) {
			name = (TextView)row.findViewById(R.id.title);
			address = (TextView)row.findViewById(R.id.address);
			icon = (ImageView)row.findViewById(R.id.icon);
		}
		
		void populateFrom(Cursor c, RestaurantHelper helper) {
			name.setText(helper.getName(c));
			address.setText(helper.getAddress(c));
			
			if (helper.getType(c).equals("@string/sit_down")) {
				icon.setImageResource(R.drawable.ball_red);
			} else if (helper.getType(c).equals("@string/take_out")) {
				icon.setImageResource(R.drawable.ball_yellow);
			} else {
				icon.setImageResource(R.drawable.ball_green);
			}
		}	
	}

}
