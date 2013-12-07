package in.phish.stream;

import in.phish.stream.phishin.Show;
import in.phish.stream.phishin.ShowsRequest;
import in.phish.stream.phishin.ShowsResponse;

import java.util.List;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowsActivity extends PhishInRestListActivity {
	
	protected void performRequest() {
		displayProgressBar();
		Intent i = getIntent();
		setTitle(Integer.toString(i.getIntExtra("StartYear", 666)));
		// TODO We need to check that end year != start year in the event
		// we need to load multiple years i.e. 1983-1987
		ShowsRequest request = new ShowsRequest(i.getIntExtra("StartYear", 666));
		String lastRequestCacheKey = request.createCacheKey();
		spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_DAY, new ShowsRequestListener());		
		return;
	}		

	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
		Show show = (Show)l.getAdapter().getItem(position);
		if (show == null) return;
		Intent intent = new Intent(v.getContext(), ShowActivity.class);		
		intent.putExtra("Show", show.id);
		intent.putExtra("Year", show.date);
        startActivity(intent);
        overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
		return;
	}

	// inner class of your spiced Activity
	private class ShowsRequestListener implements RequestListener<ShowsResponse> {
		final ShowsActivity outer = ShowsActivity.this;

		@Override
		public void onRequestFailure(SpiceException e) {			
			hideProgressBar();
			Toast.makeText(outer, "Could not load list of shows.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		@Override
		public void onRequestSuccess(ShowsResponse shows) {			
			ShowsAdapter adapter = new ShowsAdapter(outer, shows.data);
			setListAdapter(adapter);			
			hideProgressBar();
			return;
		}
	}
	
	private class ShowsAdapter extends ArrayAdapter<Show> {
		private final Context context;
		private final List<Show> shows;
		
		public ShowsAdapter(Context context, List<Show> shows) {
			super(context, android.R.layout.simple_list_item_2, shows);
			this.context = context;
			this.shows = shows;
			return;
		}
		
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
