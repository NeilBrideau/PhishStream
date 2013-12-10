package in.phish.stream;

import in.phish.stream.phishin.PhishInGsonSpringAndroidSpiceService;

import com.octo.android.robospice.SpiceManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.app.ListActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

abstract public class PhishInRestListActivity extends ListActivity {
	protected SpiceManager spiceManager = new SpiceManager(PhishInGsonSpringAndroidSpiceService.class);
	private   LinearLayout progressBar;	
	private String LIST_STATE = this.getClass().toString();
	protected Parcelable listState = null;
	
	
	@Override
	protected void onRestoreInstanceState(Bundle state) {
	    super.onRestoreInstanceState(state);
	    listState = state.getParcelable(LIST_STATE);
	    return;
	}

	@Override
	protected void onSaveInstanceState(Bundle state) {
	    super.onSaveInstanceState(state);
	    listState = getListView().onSaveInstanceState();
	    state.putParcelable(LIST_STATE, listState);
	    return;
	}
	
	@Override 
	protected void onResume() {
		super.onResume();
		if (listState != null)
	        getListView().onRestoreInstanceState(listState);	    	
		return;
	}
	
	@Override
	protected void onStart() {
	  super.onStart();
	  spiceManager.start(this);
      return;
	}

	@Override
	protected void onStop() {
	  spiceManager.shouldStop();
	  super.onStop();
      return;
	}
	
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);   
	    return;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {        	
        	//NavUtils.navigateUpFromSameTask(this);
        	finish();
        	overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);				
		setContentView(R.layout.activity_years);		
		progressBar = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
		
		// Only perform the update if the list adapted has not been set 
		if (getListAdapter() == null) 
			performRequest();
		
		return;
	}
 
	protected int checkProgressBar() {
		return progressBar.getVisibility();		
	}
	
    protected void displayProgressBar() {
		progressBar.setVisibility(View.VISIBLE);
        return;
    }

    protected void hideProgressBar() {
		progressBar.setVisibility(View.GONE);
        return;
    }

    abstract protected void performRequest();        
}
