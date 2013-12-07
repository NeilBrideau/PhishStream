package in.phish.stream;

import in.phish.stream.phishin.Show;
import in.phish.stream.phishin.ShowDetail;
import in.phish.stream.phishin.ShowDetailRequest;
import in.phish.stream.phishin.ShowDetailResponse;
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

public class ShowActivity extends PhishInRestListActivity {
	
	protected void performRequest() {
		displayProgressBar();
		Intent i = getIntent();
		
		setTitle(Integer.toString(i.getIntExtra("Year", 666)));
		
		
		ShowDetailRequest request = new ShowDetailRequest(i.getIntExtra("Show", 666));
		String lastRequestCacheKey = request.createCacheKey();
		spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_DAY, new ShowDetailRequestListener());		
		return;
	}		

	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
		//EraRow er = (EraRow)l.getAdapter().getItem(position);
		//if (er == null)// || er.getActivity() == null)
		//	return;
		//Log.e("LISTCLICK", er.toString());
        //startActivity(new Intent(v.getContext(), er.getActivity()));
        //overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
		return;
	}

	// inner class of your spiced Activity
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
			ShowDetailAdapter adapter = new ShowDetailAdapter(outer, shows.data);
			setListAdapter(adapter);			
			hideProgressBar();
			return;
		}
	}
	
	private class ShowDetailAdapter extends ArrayAdapter<ShowDetail> {
		private final Context context;
		private final List<ShowDetail> shows;
		
		public ShowDetailAdapter(Context context, ShowDetail shows) {
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
            /*ShowDetail show = shows.get(position);
            date.setText(show.date);
            venue.setText(show.location + " - " + show.venue_name);*/
            
			return showView;
		}
	}
}
