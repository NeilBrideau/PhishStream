package in.phish.stream;

import in.phish.stream.phishin.PhishInGsonSpringAndroidSpiceService;

import com.octo.android.robospice.SpiceManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

/**
 * @author corn
 *
 */
abstract public class PhishInRestListActivity extends ListActivity {
	/**
	 * 
	 */
	protected SpiceManager spiceManager = new SpiceManager(PhishInGsonSpringAndroidSpiceService.class);	// 
	/**
	 * 
	 */
	private   LinearLayout progressBar;	
	/**
	 * 
	 */
	private String LIST_STATE = this.getClass().toString();
	/**
	 * 
	 */
	protected Parcelable listState = null;
	
	
	/** Restore list scroll position 
	 * @param state Application state
	 * @see android.app.ListActivity#onRestoreInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onRestoreInstanceState(Bundle state) {
	    super.onRestoreInstanceState(state);
	    listState = state.getParcelable(LIST_STATE);
	    return;
	}

	/** Save our scroll position
	 * @param state Application state
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle state) {
	    super.onSaveInstanceState(state);
	    listState = getListView().onSaveInstanceState();
	    state.putParcelable(LIST_STATE, listState);
	    return;
	}
	
	/** Resume application and reset our scroll position
	 * @see android.app.Activity#onResume()
	 */
	@Override 
	protected void onResume() {
		super.onResume();
		if (listState != null)
	        getListView().onRestoreInstanceState(listState);	    	
		return;
	}
	
	
	/** Start the spice manager
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
	  super.onStart();
	  spiceManager.start(this);
      return;
	}

	/** Tell spice manager it can stop
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		spiceManager.shouldStop();
		super.onStop();
		return;
	}
	
	/** Overide activity animation
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);   
	    return;
	}
	
	/** Create options menu
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
		
	}
	
	/** Override animation and close properly to resume previous activity
	 * @param item Menu item chosen
	 * @return  
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_stop) {
			Intent i = new Intent(getBaseContext(), MusicPlayerService.class);  
			i.setAction(MusicPlayerService.ACTION_STOP);
			startService(i);
			return true;
		}
        if (item.getItemId() == android.R.id.home) {        	
        	//NavUtils.navigateUpFromSameTask(this);
        	finish();
        	overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
	}

	/** Start our activity and perform REST request
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);				
		setContentView(R.layout.activity_years);		
		progressBar = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
		performRequest();		
		return;
	}
 
	/** Check if progress bar is visible
	 * @return
	 */
	protected int checkProgressBar() {
		return progressBar.getVisibility();		
	}
	
    /** Force progress bar to be visible
     */
    protected void displayProgressBar() {
		progressBar.setVisibility(View.VISIBLE);
        return;
    }

    /** Force progress bar to hide
     */
    protected void hideProgressBar() {
		progressBar.setVisibility(View.GONE);
        return;
    }

    
    /** Override this and perform spice requests.
     */
    abstract protected void performRequest();        
}
