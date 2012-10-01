package csci498.csmyth.lunchlist;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Context;
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

public class LunchList extends TabActivity {
	Cursor model = null;
	RestaurantAdapter adapter = null;
	ArrayAdapter<String> addr_adapter = null;
	EditText name = null;
	EditText address = null;
	RadioGroup types = null;
	EditText notes = null;
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
        
        model = helper.getAll();
        startManagingCursor(model);
        adapter = new RestaurantAdapter(model);
        list.setAdapter(adapter);
        
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
        
        list.setOnItemClickListener(onListClick);
        
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
			model.requery();
		}
	};
	
	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			model.moveToPosition(position);
			
			name.setText(helper.getName(model));
			address.setText(helper.getAddress(model));
			notes.setText(helper.getNotes(model));
			
			if (helper.getType(model).equals("@string/sit_down")) {
				types.check(R.id.sit_down);
			} else if (helper.getType(model).equals("@string/take_out")) {
				types.check(R.id.take_out);
			} else {
				types.check(R.id.delivery);
			}
			
			getTabHost().setCurrentTab(DETAIL_TAB);
		}
	};
	
	class RestaurantAdapter extends CursorAdapter {
		RestaurantAdapter(Cursor c) {
			super(LunchList.this, c);
		}
		
		/*Code for the overrides of getViewTypeCount() and getItemViewType() prompted by: 
			http://stackoverflow.com/questions/5300962/getviewtypecount-and-getitemviewtype-methods-of-arrayadapter
			and class discussion on Piazza */		
		@Override
		public int getViewTypeCount() {
			return 3;
		}
		
		public int getItemViewType(Cursor c) {
			int current_position = c.getPosition();
			c.moveToPosition(c.getCount() - 1);
			String type = helper.getType(c);
			
			if (type.equals("@string/take_out")) {
				c.moveToPosition(current_position);
				return ROW_TYPE_TAKE_OUT;
			} else if (type.equals("@string/sit_down")) {
				c.moveToPosition(current_position);
				return ROW_TYPE_SIT_DOWN;
			} else {
				c.moveToPosition(current_position);
				return ROW_TYPE_DELIVERY;
			}
		}
		
		@Override
		public void bindView(View row, Context ctxt, Cursor c) {
			RestaurantHolder holder = (RestaurantHolder)row.getTag();
			
			holder.populateFrom(c, helper);
		}
		
		@Override
		public View newView(Context ctxt, Cursor c, ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
			
			int view_type = getItemViewType(c);
			View row = null;
			
			if (view_type == ROW_TYPE_TAKE_OUT) {
				row = inflater.inflate(R.layout.row, parent, false);
			} else if (view_type == ROW_TYPE_SIT_DOWN) {
				row = inflater.inflate(R.layout.row2, parent, false);
			} else {
				row = inflater.inflate(R.layout.row3, parent, false);
			}
			
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
