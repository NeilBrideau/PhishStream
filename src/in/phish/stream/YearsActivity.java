package in.phish.stream;

import in.phish.stream.phishin.ErasRequest;
import in.phish.stream.phishin.ErasResponse;
import in.phish.stream.phishin.PhishInGsonSpringAndroidSpiceService;

import java.util.ArrayList;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import android.os.Bundle;
import android.app.ListActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;

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
	protected void onCreate(Bundle savedInstanceState	) {
		super.onCreate(savedInstanceState);
		
		
		// Create a list of items for the list view
		String[] values = new String[] { "1983", "2012" };		
		final ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < values.length; i++) 
			list.add(values[i]);		
		
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);  
		setProgressBarIndeterminateVisibility(true);
		
		setContentView(R.layout.activity_years);
		LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);		
		linlaHeaderProgress.setVisibility(View.VISIBLE);
		
		performRequest();
		return;
	}

	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
		//Intent myIntent = new Intent(v.getContext(), YearsActivity.class);
        //startActivity(myIntent);
        //overridePendingTransition (R.anim.right_slide_in, R.anim.left_slide_out);
		return;
	}

	private void performRequest() {
		// TODO 
		//this.setProgressBarIndeterminateVisibility(true);
		ErasRequest request = new ErasRequest();
		String lastRequestCacheKey = request.createCacheKey();
		spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_MINUTE, new ErasRequestListener());
		return;
	}

	// inner class of your spiced Activity
	private class ErasRequestListener implements RequestListener<ErasResponse> {
		@Override
		public void onRequestFailure(SpiceException e) {
			Log.e("ROBOSPICE", "WE ARE A FAILURE!!!!");
			// update your UI
			return;
		}
		
		@Override
		public void onRequestSuccess(ErasResponse eras) {
			Log.e("ROBOSPICE", "WE ARE SUCCESS!!!!");
			// update your UI
			
			Log.v("PHISHIN", "---------------------- 1.0");
			for (String year : eras.data.one) {
				Log.v("PHISHIN", year);
			}
			Log.v("PHISHIN", "---------------------- 2.0");
			for (String year : eras.data.two) {
				Log.v("PHISHIN", year);
			}
			Log.v("PHISHIN", "---------------------- 3.0");
			for (String year : eras.data.three) {
				Log.v("PHISHIN", year);
			}
			
			return;
		}
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
}
