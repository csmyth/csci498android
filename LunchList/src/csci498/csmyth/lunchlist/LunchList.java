package csci498.csmyth.lunchlist;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class LunchList extends FragmentActivity implements LunchFragment.OnRestaurantListener {
	public final static String ID_EXTRA = "csci498.csmyth.lunchlist._ID";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        LunchFragment lunch = (LunchFragment)getSupportFragmentManager().findFragmentById(R.id.lunch);
        lunch.setOnRestaurantListener(this);
    }
	
	public void onRestaurantSelected(long id) {
		Intent intent = new Intent(this, DetailForm.class);
		
		intent.putExtra(ID_EXTRA, String.valueOf(id));
		startActivity(intent);
	}
	
}
