package csci498.csmyth.lunchlist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class DetailForm extends Activity {
	EditText name = null;
	EditText address = null;
	EditText notes = null;
	RadioGroup types = null;
	RestaurantHelper helper = null;
	String restaurantId = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_form);
		
		helper = new RestaurantHelper(this);
        
        name = (EditText)findViewById(R.id.name);
		address = (EditText)findViewById(R.id.addr);
		types = (RadioGroup)findViewById(R.id.types);
		notes = (EditText)findViewById(R.id.notes);
        
        Button save = (Button)findViewById(R.id.save);
        save.setOnClickListener(onSave);
        
        restaurantId = getIntent().getStringExtra(LunchList.ID_EXTRA);
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
		}
	};
	
}
