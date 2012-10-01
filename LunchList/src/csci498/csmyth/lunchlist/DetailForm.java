package csci498.csmyth.lunchlist;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioGroup;

public class DetailForm extends Activity {
	EditText name = null;
	EditText address = null;
	RadioGroup types = null;
	EditText notes = null;
	RestaurantHelper helper = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_form);
	}
}
