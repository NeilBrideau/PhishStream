package in.phish.stream;

import in.phish.stream.phishin.ShowDetail;
import in.phish.stream.phishin.ShowDetailRequest;
import in.phish.stream.phishin.ShowDetailResponse;
import in.phish.stream.phishin.Track;

import java.util.ArrayList;
import java.util.List;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowActivity extends PhishInRestListActivity {
	/**
	 * 
	 */
	protected ShowDetail show;
	
	/* (non-Javadoc)
	 * @see in.phish.stream.PhishInRestListActivity#performRequest()
	 */
	protected void performRequest() {
		displayProgressBar();
		Intent i = getIntent();
		setTitle(i.getStringExtra("Date"));
		ShowDetailRequest request = new ShowDetailRequest(i.getIntExtra("ShowId", 666));
		String lastRequestCacheKey = request.createCacheKey();
		spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_DAY, new ShowDetailRequestListener());		
		return;
	}		
		
	/* (non-Javadoc)
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
	       
	    ListMenuItem mi = (ListMenuItem) l.getAdapter().getItem(position);
		if (mi == null || mi.getUrl() == null)
			return;
		Log.e("LISTCLICK", mi.getUrl());
		Intent i = new Intent(v.getContext(), MusicPlayerService.class);  
		i.putExtra("URL", mi.getUrl());
		i.setAction(MusicPlayerService.ACTION_PLAY);
		startService(i);
		return;
	}

	/**
	 * @author corn
	 *
	 */
	private class ShowDetailRequestListener implements RequestListener<ShowDetailResponse> {
		final ShowActivity outer = ShowActivity.this;

		@Override
		public void onRequestFailure(SpiceException e) {			
			hideProgressBar();
			Toast.makeText(outer, "Could not load list of shows.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		@Override
		public void onRequestSuccess(ShowDetailResponse shows) {			
			ShowDetail show = shows.data;
			List<ListMenuItem> menu = new ArrayList<ListMenuItem>();
			
			// TODO Add a venue activity
			menu.add(new ListMenuItem(show.venue.name, null));
			for (Track t : show.tracks) 				
				menu.add(new ListMenuItem(t.title, t.mp3));			
			
			ShowDetailAdapter adapter = new ShowDetailAdapter(outer, menu);
			setListAdapter(adapter);			
			hideProgressBar();
			return;
		}
	}
	
	/**
	 * @author corn
	 *
	 */
	private class ListMenuItem {
		private String  name;
		private    int  id;
		private String url;		
		private ListMenuItem(String name, String url) {
			this.name 	= name;			
			this.url    = url;
			return;
		}	
				
		public String toString() { return name; }
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getUrl() {
		    return url;
		}
		
	}
	
	/**
	 * @author corn
	 *
	 */
	private class ShowDetailAdapter extends ArrayAdapter<ListMenuItem> {
		private final Context context;
		private final List<ListMenuItem> items;
		
		/**
		 * @param context
		 * @param mi
		 */
		public ShowDetailAdapter(Context context, List<ListMenuItem> mi) {
			super(context, android.R.layout.simple_list_item_1, mi);
			this.context = context;
			this.items = mi;
			return;
		}
		
		/* (non-Javadoc)
		 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View showView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            TextView text  = (TextView)showView.findViewById(android.R.id.text1);            
            ListMenuItem mi = items.get(position);
            
            text.setText(mi.toString());                        
            
			return showView;
		}
	}
}
