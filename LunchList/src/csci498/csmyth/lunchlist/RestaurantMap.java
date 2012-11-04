package csci498.csmyth.lunchlist;

import android.os.Bundle;
import com.google.android.maps.MapActivity;

public class RestaurantMap extends MapActivity {
	double lat;
	double lon;
	
	public static final String EXTRA_LATITUDE = "csci498.csmyth.lunchlist.EXTRA_LATITUDE";
	public static final String EXTRA_LONGITUDE = "csci498.csmyth.lunchlist.EXTRA_LONGITUDE";
	public static final String EXTRA_NAME = "csci498.csmyth.lunchlist.EXTRA_NAME";
	public static final Double LAT_LON_DEFAULT = 0.0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		
		lat = getIntent().getDoubleExtra(EXTRA_LATITUDE, LAT_LON_DEFAULT);
		lon = getIntent().getDoubleExtra(EXTRA_LONGITUDE, LAT_LON_DEFAULT);
		
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
