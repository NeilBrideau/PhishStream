package in.phish.stream.phishin;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

/**
 * @author corn
 *
 */
public class ErasRequest extends SpringAndroidSpiceRequest<ErasResponse> {

	/**
	 * 
	 */
	public ErasRequest() {
		super(ErasResponse.class);
		return;
	}

	/* (non-Javadoc)
	 * @see com.octo.android.robospice.request.SpiceRequest#loadDataFromNetwork()
	 */
	@Override
	public ErasResponse loadDataFromNetwork() throws Exception {
		return getRestTemplate().getForObject("http://phish.in/api/v1/eras", ErasResponse.class);
	}

	public String createCacheKey() {
	    return "eras";
	}
}
