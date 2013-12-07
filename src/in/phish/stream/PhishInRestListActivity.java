package in.phish.stream;

import in.phish.stream.phishin.PhishInGsonSpringAndroidSpiceService;

import com.octo.android.robospice.SpiceManager;
import android.os.Bundle;
import android.app.ListActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

abstract public class PhishInRestListActivity extends ListActivity {
	protected SpiceManager spiceManager = new SpiceManager(PhishInGsonSpringAndroidSpiceService.class);
	private   LinearLayout progressBar;	
	
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
        	NavUtils.navigateUpFromSameTask(this);
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
