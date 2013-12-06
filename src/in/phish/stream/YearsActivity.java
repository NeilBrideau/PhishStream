package in.phish.stream;

import in.phish.stream.phishin.ErasRequest;
import in.phish.stream.phishin.ErasResponse;
import in.phish.stream.phishin.PhishInGsonSpringAndroidSpiceService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class YearsActivity extends ListActivity {
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
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);   
	    return;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
        	case android.R.id.home:
        		NavUtils.navigateUpFromSameTask(this);
        		overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
            	break;
        	default:
        		return super.onOptionsItemSelected(item);
        }
        return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState	) {
		super.onCreate(savedInstanceState);
		
		
		// Create a list of items for the list view
		String[] values = new String[] { "1983", "2012" };		
		final ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < values.length; i++) 
			list.add(values[i]);		
		
		setContentView(R.layout.activity_years);
		LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);		
		linlaHeaderProgress.setVisibility(View.VISIBLE);
		
		//requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		//setProgressBarIndeterminateVisibility(true);
		ErasRequest request = new ErasRequest();
		String lastRequestCacheKey = request.createCacheKey();
		spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_MINUTE, new ErasRequestListener(this));		
		return;
	}

	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
		//Intent myIntent = new Intent(v.getContext(), YearsActivity.class);
        //startActivity(myIntent);
        //overridePendingTransition (R.anim.right_slide_in, R.anim.left_slide_out);
		return;
	}

	// inner class of your spiced Activity
	private class ErasRequestListener implements RequestListener<ErasResponse> {
		final YearsActivity activity;
		
		public ErasRequestListener(YearsActivity yearsActivity) {
			super();
			activity = yearsActivity;
			return;
		}

		@Override
		public void onRequestFailure(SpiceException e) {
			//setProgressBarIndeterminateVisibility(false);
			Toast.makeText(activity, "Could not load list of years.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		@Override
		public void onRequestSuccess(ErasResponse eras) {			
			// update your UI
			
			List<EraRow> eralist = new ArrayList<EraRow>();			
			if (eras.data.one.length > 0) {				
				eralist.add(new EraRow(true, 1, 1));
				for (String year : eras.data.one) {
					String [] years = year.split("-");
					if (years.length > 1) 
						eralist.add(new EraRow(false, Integer.parseInt(years[0]), Integer.parseInt(years[1])));
					else
						eralist.add(new EraRow(false, Integer.parseInt(year), Integer.parseInt(year)));					
				}				
			}
			if (eras.data.two.length > 0) {				
				eralist.add(new EraRow(true, 1, 1));
				for (String year : eras.data.one) {
					String [] years = year.split("-");
					if (years.length > 1) 
						eralist.add(new EraRow(false, Integer.parseInt(years[0]), Integer.parseInt(years[1])));
					else
						eralist.add(new EraRow(false, Integer.parseInt(year), Integer.parseInt(year)));					
				}				
			}
			if (eras.data.three.length > 0) {				
				eralist.add(new EraRow(true, 1, 1));
				for (String year : eras.data.one) {
					String [] years = year.split("-");
					if (years.length > 1) 
						eralist.add(new EraRow(false, Integer.parseInt(years[0]), Integer.parseInt(years[1])));
					else
						eralist.add(new EraRow(false, Integer.parseInt(year), Integer.parseInt(year)));					
				}				
			}
			ErasAdapter adapter = new ErasAdapter(activity, eralist);
			activity.setListAdapter(adapter);
			//setProgressBarIndeterminateVisibility(false);
			return;
		}
	}

	private class EraRow {
		private boolean isEra;
		private int yearStart;
		private int yearEnd;		
		
		public EraRow(boolean isera, int start, int end) {
			isEra = isera;
			yearStart = start;
			yearEnd = end;
			return;
		}
		public boolean isEra() {
			return isEra;
		}
				
		public String toString() {
			if (isEra) 
				return String.format(Locale.US, "%.1f", (float)yearStart);
			if (yearStart != yearEnd) 
				return String.format(Locale.US, "%d-%d", yearStart, yearEnd);
			return String.format(Locale.US, "%d", yearStart);
		}
	}
	
	private class ErasAdapter extends ArrayAdapter<EraRow> {
		private final Context context;
		private final List<EraRow> eras;
		
		public ErasAdapter(Context context, List<EraRow> eras) {
			super(context, R.layout.row_years, eras);
			this.context = context;
			this.eras = eras;
			return;
		}
		
		@Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.row_years, parent, false);
            TextView value = (TextView)rowView.findViewById(R.id.years_rowtext);            
            EraRow row = eras.get(position);
            if (row.isEra()) {
            	value.setGravity(Gravity.RIGHT);
            	value.setTypeface(value.getTypeface(), Typeface.BOLD_ITALIC);
            }
            value.setText(row.toString());
			return rowView;
		}
	}
}
