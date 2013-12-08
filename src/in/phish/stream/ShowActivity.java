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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowActivity extends PhishInRestListActivity {
	protected ShowDetail show;
	
	protected void performRequest() {
		displayProgressBar();
		Intent i = getIntent();
		setTitle(i.getStringExtra("Date"));
		ShowDetailRequest request = new ShowDetailRequest(i.getIntExtra("ShowId", 666));
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
			ShowDetail show = shows.data;
			List<MenuItem> menu = new ArrayList<MenuItem>();
			
			// TODO Add a venue activity
			menu.add(new MenuItem(show.venue.name, null));
			for (Track t : show.tracks) 				
				menu.add(new MenuItem(t.title, null));			
			
			ShowDetailAdapter adapter = new ShowDetailAdapter(outer, menu);
			setListAdapter(adapter);			
			hideProgressBar();
			return;
		}
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
	
	private class ShowDetailAdapter extends ArrayAdapter<MenuItem> {
		private final Context context;
		private final List<MenuItem> items;
		
		public ShowDetailAdapter(Context context, List<MenuItem> mi) {
			super(context, android.R.layout.simple_list_item_1, mi);
			this.context = context;
			this.items = mi;
			return;
		}
		
		@Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View showView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            TextView text  = (TextView)showView.findViewById(android.R.id.text1);            
            MenuItem mi = items.get(position);
            
            text.setText(mi.toString());                        
            
			return showView;
		}
	}
}
