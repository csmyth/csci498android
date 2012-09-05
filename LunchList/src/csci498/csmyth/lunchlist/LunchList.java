package csci498.csmyth.lunchlist;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class LunchList extends Activity {
	List<Restaurant> model = new ArrayList<Restaurant>();
	RestaurantAdapter adapter = null;
	ArrayAdapter<String> addr_adapter = null;
	
	class RestaurantAdapter extends ArrayAdapter<Restaurant> {
		RestaurantAdapter() {
			super(LunchList.this, android.R.layout.simple_list_item_1, model);
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			
			if (row == null) {
				LayoutInflater inflater = getLayoutInflater();
				
				row = inflater.inflate(R.layout.row, null);
			}
			
			Restaurant r = model.get(position);
			
			((TextView)row.findViewById(R.id.title)).setText(r.getName());
			((TextView)row.findViewById(R.id.address)).setText(r.getAddress());
			
			ImageView icon = (ImageView)row.findViewById(R.id.icon);
			
			if (r.getType().equals("@string/sit_down")) {
				icon.setImageResource(R.drawable.ball_red);
			}
			else if (r.getType().equals("@string/take_out")) {
				icon.setImageResource(R.drawable.ball_yellow);
			}
			else {
				icon.setImageResource(R.drawable.ball_green);
			}
			
			return row;
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
			}
			else if (r.getType().equals("@string/take_out")) {
				icon.setImageResource(R.drawable.ball_yellow);
			}
			else {
				icon.setImageResource(R.drawable.ball_green);
			}
		}
		
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_list);
    
        //TODO: Tutorial #3 Extra Credit: Excessive RadioButtons via Java
        //RadioButton extra1 = new RadioButton(null);
        //RadioGroup types = (RadioGroup)findViewById(R.id.types);
        //types.addView(extra1);
        
        Button save = (Button)findViewById(R.id.save);
        
        save.setOnClickListener(onSave);
        
        Spinner spin = (Spinner)findViewById(R.id.restaurant_spinner);
        
        adapter = new RestaurantAdapter();
        spin.setAdapter(adapter);
        
        AutoCompleteTextView auto_complete_addr = (AutoCompleteTextView)findViewById(R.id.addr);
        addr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
        auto_complete_addr.setAdapter(addr_adapter);
        
        
    }

    private View.OnClickListener onSave = new View.OnClickListener() {

    	public void onClick(View v) {
			Restaurant r = new Restaurant();
			EditText name = (EditText)findViewById(R.id.name);
			EditText address = (EditText)findViewById(R.id.addr);
			
			r.setName(name.getText().toString());
			r.setAddress(address.getText().toString());
			
			RadioGroup types = (RadioGroup)findViewById(R.id.types);
			
			switch (types.getCheckedRadioButtonId()) {
			case R.id.sit_down:
				r.setType("@string/sit_down");
				break;
				
			case R.id.take_out:
				r.setType("@string/take_out");
				break;
				
			case R.id.delivery:
				r.setType("@string/delivery");
				break;
			}
			
			adapter.add(r);
			addr_adapter.add(r.getAddress());
		}
		
	};
	
}
