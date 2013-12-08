package in.phish.stream;

import in.phish.stream.phishin.ErasRequest;
import in.phish.stream.phishin.ErasResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class YearsActivity extends PhishInRestListActivity {	
		
	@Override
	protected void performRequest() {					
		displayProgressBar();
		ErasRequest request = new ErasRequest();
		String lastRequestCacheKey = request.createCacheKey();
		spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_DAY, new ErasRequestListener());		
		return;
	}

	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
		EraRow er = (EraRow)l.getAdapter().getItem(position);
		if (er == null || er.isEra()) return;
		Intent intent = new Intent(v.getContext(), ShowsActivity.class);		
		intent.putExtra("StartYear", er.yearStart);
		intent.putExtra("EndYear",   er.yearEnd);
        startActivity(intent);
        overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
		return;
	}

	private class ErasRequestListener implements RequestListener<ErasResponse> {
		final YearsActivity outer = YearsActivity.this;

		@Override
		public void onRequestFailure(SpiceException e) {			
			hideProgressBar();
			Toast.makeText(outer, "Could not load list of years.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		@Override
		public void onRequestSuccess(ErasResponse eras) {			
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
				eralist.add(new EraRow(true, 2, 2));
				for (String year : eras.data.two) {
					String [] years = year.split("-");
					if (years.length > 1) 
						eralist.add(new EraRow(false, Integer.parseInt(years[0]), Integer.parseInt(years[1])));
					else
						eralist.add(new EraRow(false, Integer.parseInt(year), Integer.parseInt(year)));					
				}				
			}
			if (eras.data.three.length > 0) {				
				eralist.add(new EraRow(true, 3, 3));
				for (String year : eras.data.three) {
					String [] years = year.split("-");
					if (years.length > 1) 
						eralist.add(new EraRow(false, Integer.parseInt(years[0]), Integer.parseInt(years[1])));
					else
						eralist.add(new EraRow(false, Integer.parseInt(year), Integer.parseInt(year)));					
				}				
			}			
			setListAdapter(new ErasAdapter(outer, eralist));	
			if (listState != null)
				getListView().onRestoreInstanceState(listState);
			hideProgressBar();
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
				return String.format(Locale.US, "%d - %d", yearStart, yearEnd);
			return String.format(Locale.US, "%d", yearStart);
		}
	}
	
	private class ErasAdapter extends ArrayAdapter<EraRow> {
		private final Context context;
		private final List<EraRow> eras;
		
		public ErasAdapter(Context context, List<EraRow> eras) {
			super(context, android.R.layout.simple_list_item_1, eras);
			this.context = context;
			this.eras = eras;
			return;
		}
		
		@Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            TextView value = (TextView)rowView.findViewById(android.R.id.text1);            
            EraRow row = eras.get(position);
            if (row.isEra()) {
            	value.setGravity(Gravity.CENTER);
            	value.setTypeface(value.getTypeface(), Typeface.BOLD);            	            	            	            	
            	value.setBackgroundColor(Color.DKGRAY);
            	value.setTextColor(Color.LTGRAY);
            	
            	// Want to make era sections small >:|
            	//rowView.setPadding(0, 0, 0, 0);
            	//value.setPadding(0, 0, 0, 0);
            	//rowView.setMinimumHeight(0);
            	//value.setMinimumHeight(0);
            	
            }
            value.setText(row.toString());
			return rowView;
		}
		
		@Override
		public boolean isEnabled(int position) {
			return !eras.get(position).isEra(); 
		}
	}
}
