package csci498.csmyth.lunchlist;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.os.Bundle;
import android.os.SystemClock;
import android.app.TabActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class LunchList extends TabActivity {
	List<Restaurant> model = new ArrayList<Restaurant>();
	RestaurantAdapter adapter = null;
	ArrayAdapter<String> addr_adapter = null;
	EditText name = null;
	EditText address = null;
	RadioGroup types = null;
	EditText notes = null;
	Restaurant current = null;
	RestaurantHelper helper = null;
	
	static final int ROW_TYPE_TAKE_OUT = 1;
	static final int ROW_TYPE_SIT_DOWN = 2;
	static final int ROW_TYPE_DELIVERY = 3;
	
	static final int LIST_TAB = 0;
	static final int DETAIL_TAB = 1;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_list);
        helper = new RestaurantHelper(this);
        
        name = (EditText)findViewById(R.id.name);
		address = (EditText)findViewById(R.id.addr);
		types = (RadioGroup)findViewById(R.id.types);
		notes = (EditText)findViewById(R.id.notes);
        
        Button save = (Button)findViewById(R.id.save);
        save.setOnClickListener(onSave);
        
        ListView list = (ListView)findViewById(R.id.restaurants);
        adapter = new RestaurantAdapter();
        list.setAdapter(adapter);
        list.setOnItemClickListener(onListClick);
        
        AutoCompleteTextView auto_complete_addr = (AutoCompleteTextView)findViewById(R.id.addr);
        addr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
        auto_complete_addr.setAdapter(addr_adapter);
        
        TabHost.TabSpec spec = getTabHost().newTabSpec("tag1");
        
        spec.setContent(R.id.restaurants);
        spec.setIndicator("List", getResources().getDrawable(R.drawable.list));
        getTabHost().addTab(spec);
        
        spec = getTabHost().newTabSpec("tag2");
        spec.setContent(R.id.details);
        spec.setIndicator("Details", getResources().getDrawable(R.drawable.restaurant));
        getTabHost().addTab(spec);
        
        getTabHost().setCurrentTab(LIST_TAB);
        
    }
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		helper.close();
	}
	
	private View.OnClickListener onSave = new View.OnClickListener() {
    	public void onClick(View v) {
			String type = null;
			
			switch (types.getCheckedRadioButtonId()) {
			case R.id.sit_down:
				type = "@string/sit_down";
				break;
				
			case R.id.take_out:
				type = "@string/take_out";
				break;
				
			case R.id.delivery:
				type = "@string/delivery";
				break;
			}
			
			helper.insert(name.getText().toString(), address.getText().toString(), type, notes.getText().toString());
		}
	};
	
	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			current = model.get(position);
			
			name.setText(current.getName());
			address.setText(current.getAddress());
			notes.setText(current.getNotes());
			
			if (current.getType().equals("@string/sit_down")) {
				types.check(R.id.sit_down);
			} else if (current.getType().equals("@string/take_out")) {
				types.check(R.id.take_out);
			} else {
				types.check(R.id.delivery);
			}
			
			getTabHost().setCurrentTab(DETAIL_TAB);
		}
	};
	
	class RestaurantAdapter extends ArrayAdapter<Restaurant> {
		RestaurantAdapter() {
			super(LunchList.this, R.layout.row, model);
		}
		
		/*Code for the overrides of getViewTypeCount() and getItemViewType() prompted by: 
			http://stackoverflow.com/questions/5300962/getviewtypecount-and-getitemviewtype-methods-of-arrayadapter
			and class discussion on Piazza */		
		@Override
		public int getViewTypeCount() {
			return 3;
		}
		
		@Override
		public int getItemViewType(int position) {
			String type = model.get(position).getType();
			if (type.equals("@string/take_out")) {
				return ROW_TYPE_TAKE_OUT;
			} else if (type.equals("@string/sit_down")) {
				return ROW_TYPE_SIT_DOWN;
			} else {
				return ROW_TYPE_DELIVERY;
			}
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			RestaurantHolder holder = null;
			int view_type = getItemViewType(position);
			
			if (row == null) {
				LayoutInflater inflater = getLayoutInflater();
				
				
				if (view_type == ROW_TYPE_TAKE_OUT) {
					row = inflater.inflate(R.layout.row, parent, false);
				} else if (view_type == ROW_TYPE_SIT_DOWN) {
					row = inflater.inflate(R.layout.row2, parent, false);
				} else {
					row = inflater.inflate(R.layout.row3, parent, false);
				}
				
				holder = new RestaurantHolder(row);
				row.setTag(holder);
			} else {
				holder = (RestaurantHolder)row.getTag();
			}
			
			holder.populateFrom(model.get(position));
			
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
		
		void populateFrom(Restaurant r) {
			name.setText(r.getName());
			address.setText(r.getAddress());
			
			if (r.getType().equals("@string/sit_down")) {
				icon.setImageResource(R.drawable.ball_red);
			} else if (r.getType().equals("@string/take_out")) {
				icon.setImageResource(R.drawable.ball_yellow);
			} else {
				icon.setImageResource(R.drawable.ball_green);
			}
		}	
	}

}
