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


/**
 * @author corn
 *
 */
public class PhishActivity extends ListActivity {
	protected SpiceManager spiceManager = new SpiceManager(PhishInGsonSpringAndroidSpiceService.class);
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
	  super.onStart();
	  spiceManager.start(this);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
	  spiceManager.shouldStop();
	  super.onStop();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
				
		// Create list of menu items	
		final ArrayList<ListMenuItem> menu = new ArrayList<ListMenuItem>();
		menu.add(new ListMenuItem("Years", YearsActivity.class));
		menu.add(new ListMenuItem("Tours", null));
		menu.add(new ListMenuItem("Venues", null));
		menu.add(new ListMenuItem("Songs", null));
		menu.add(new ListMenuItem("Current Playlist", null));
		
		// Create adapter to turn list of strings into menu items
		setListAdapter(new MenuItemArrayAdapter(this, android.R.layout.simple_list_item_1, menu));		
	    return;
	}
	
	/* (non-Javadoc)
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
		ListMenuItem mi = (ListMenuItem)l.getAdapter().getItem(position);
		if (mi == null || mi.getActivity() == null)
			return;
        startActivity(new Intent(v.getContext(), mi.getActivity()));
        overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
		return;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) { 
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	/**
	 * @author corn
	 *
	 */
	private class ListMenuItem {
		private String  name;
		private    int  id;
		private Class<?> activity;
		private ListMenuItem(String name, Class<?> cls) {
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
	/**
	 * @author corn
	 *
	 */
	private class MenuItemArrayAdapter extends ArrayAdapter<ListMenuItem> {
		private HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
		
		public MenuItemArrayAdapter(Context context, int textViewResourceId, List<ListMenuItem> objects) {
			super(context, textViewResourceId, objects);			
			for (int i = 0; i < objects.size(); ++i) {
				mIdMap.put(objects.get(i).toString(), i);
				objects.get(i).setId(i);
			}
			return;
		}
		
		/* (non-Javadoc)
		 * @see android.widget.ArrayAdapter#getItemId(int)
		 */
		@Override
		public long getItemId(int position) {
			ListMenuItem item = getItem(position);
			item.getId();
			return mIdMap.get(item.toString());
		}
		
		/* (non-Javadoc)
		 * @see android.widget.BaseAdapter#hasStableIds()
		 */
		@Override
		public boolean hasStableIds() {
			return true;
		}
	}
}
