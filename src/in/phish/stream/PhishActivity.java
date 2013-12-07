package in.phish.stream;

import in.phish.stream.phishin.PhishInGsonSpringAndroidSpiceService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.octo.android.robospice.SpiceManager;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class PhishActivity extends ListActivity {
	protected SpiceManager spiceManager = new SpiceManager(PhishInGsonSpringAndroidSpiceService.class);
	
	@Override
	protected void onStart() {
	  super.onStart();
	  spiceManager.start(this);
	}

	@Override
	protected void onStop() {
	  spiceManager.shouldStop();
	  super.onStop();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
				
		// Create list of menu items	
		final ArrayList<MenuItem> menu = new ArrayList<MenuItem>();
		menu.add(new MenuItem("Years", YearsActivity.class));
		menu.add(new MenuItem("Tours", null));
		menu.add(new MenuItem("Venues", null));
		menu.add(new MenuItem("Songs", null));
		menu.add(new MenuItem("Current Playlist", null));
		
		// Create adapter to turn list of strings into menu items
		setListAdapter(new MenuItemArrayAdapter(this, android.R.layout.simple_list_item_1, menu));		
	    return;
	}
	
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
		MenuItem mi = (MenuItem)l.getAdapter().getItem(position);
		if (mi == null || mi.getActivity() == null)
			return;
        startActivity(new Intent(v.getContext(), mi.getActivity()));
        overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
		return;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) { 
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	private class MenuItem {
		private String  name;
		private    int  id;
		private Class<?> activity;
		private MenuItem(String name, Class<?> cls) {
			this.name 		= name;			
			this.activity 	= cls;
			return;
		}	
		public Class<?> getActivity() { return activity; }		
		public String toString() { return name; }
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
	}
	
	// Associates id with a item view mIdMap
	private class MenuItemArrayAdapter extends ArrayAdapter<MenuItem> {
		private HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
		
		public MenuItemArrayAdapter(Context context, int textViewResourceId, List<MenuItem> objects) {
			super(context, textViewResourceId, objects);			
			for (int i = 0; i < objects.size(); ++i) {
				mIdMap.put(objects.get(i).toString(), i);
				objects.get(i).setId(i);
			}
			return;
		}
		
		@Override
		public long getItemId(int position) {
			MenuItem item = getItem(position);
			item.getId();
			return mIdMap.get(item.toString());
		}
		
		@Override
		public boolean hasStableIds() {
			return true;
		}
	}
}
