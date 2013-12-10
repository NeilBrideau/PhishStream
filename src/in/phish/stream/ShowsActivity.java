package in.phish.stream;

import in.phish.stream.phishin.Show;
import in.phish.stream.phishin.ShowsRequest;
import in.phish.stream.phishin.ShowsResponse;

import java.util.ArrayList;
import java.util.Collections;
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

/**
 * @author corn
 *
 */
public class ShowsActivity extends PhishInRestListActivity {
	/**
	 * 
	 */
	List<Show> showList;
	
	/* (non-Javadoc)
	 * @see in.phish.stream.PhishInRestListActivity#performRequest()
	 */
	protected void performRequest() {
		displayProgressBar();
		Intent i = getIntent();
		int startYear = 0;
		int endYear = 0;
		
		if (i.getIntExtra("StartYear", 0) != 0) 
			startYear = i.getIntExtra("StartYear", 0);
		if (i.getIntExtra("EndYear", 0) != 0) 
			endYear = i.getIntExtra("EndYear", 0);
		else 
			endYear = startYear;
		
		if (startYear != endYear) 
			setTitle(Integer.toString(startYear) + " - " + Integer.toString(endYear));
		else 
			setTitle(Integer.toString(startYear));
		
		
		showList = new ArrayList<Show>();
		
		// TODO We need to check that end year != start year in the event
		// we need to load multiple years i.e. 1983-1987
		for (int year = startYear; year <= endYear; year++) {
			ShowsRequest request = new ShowsRequest(year);
			String lastRequestCacheKey = request.createCacheKey();
			spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_DAY, new ShowsRequestListener());
		}
		return;
	}		

	/* (non-Javadoc)
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
		Show show = (Show)l.getAdapter().getItem(position);
		if (show == null) return;
		Intent intent = new Intent(v.getContext(), ShowActivity.class);		
		intent.putExtra("ShowId", show.id);
		intent.putExtra("Date", show.date);
        startActivity(intent);
        overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
		return;
	}

	// inner class of your spiced Activity
	/**
	 * @author corn
	 *
	 */
	private class ShowsRequestListener implements RequestListener<ShowsResponse> {
		/**
		 * 
		 */
		final ShowsActivity outer = ShowsActivity.this;

		/* (non-Javadoc)
		 * @see com.octo.android.robospice.request.listener.RequestListener#onRequestFailure(com.octo.android.robospice.persistence.exception.SpiceException)
		 */
		@Override
		public void onRequestFailure(SpiceException e) {			
			hideProgressBar();
			Toast.makeText(outer, "Could not load list of shows.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		/* (non-Javadoc)
		 * @see com.octo.android.robospice.request.listener.RequestListener#onRequestSuccess(java.lang.Object)
		 */
		@Override
		public void onRequestSuccess(ShowsResponse shows) {	
			for (Show show : shows.data) 
				showList.add(show);			
			Log.e("SHOWS", "Got shows pending: " + spiceManager.getPendingRequestCount());
			if (spiceManager.getPendingRequestCount() <= 1) {
				Log.e("SHOWS", "No pending operations listsize " + showList.size());
				Collections.sort(showList);
				Log.e("SHOWS", "No pending operations listsize " + showList.size());
				ShowsAdapter adapter = new ShowsAdapter(outer, showList);
				setListAdapter(adapter);
				hideProgressBar();
			}
			return;
		}
	}
	
	/**
	 * @author corn
	 *
	 */
	private class ShowsAdapter extends ArrayAdapter<Show> {
		/**
		 * 
		 */
		private final Context context;
		/**
		 * 
		 */
		private final List<Show> shows;
		
		/**
		 * @param context
		 * @param shows
		 */
		public ShowsAdapter(Context context, List<Show> shows) {
			super(context, android.R.layout.simple_list_item_2, shows);
			this.context = context;
			this.shows = shows;
			return;
		}
		
		/* (non-Javadoc)
		 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View showView = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
            TextView date  = (TextView)showView.findViewById(android.R.id.text1);
            TextView venue = (TextView)showView.findViewById(android.R.id.text2);
            Show show = shows.get(position);
            date.setText(show.date);
            venue.setText(show.location + " - " + show.venue_name);
			return showView;
		}
	}
}
