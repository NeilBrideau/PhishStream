package in.phish.stream.phishin;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

/**
 * @author corn
 *
 */
public class ShowsRequest extends SpringAndroidSpiceRequest<ShowsResponse> {
    private final int year;

    /**
     * @param year
     */
    public ShowsRequest(int year) {
        super(ShowsResponse.class);
        this.year = year;
        return;
    }

    /* (non-Javadoc)
     * @see com.octo.android.robospice.request.SpiceRequest#loadDataFromNetwork()
     */
    @Override
    public ShowsResponse loadDataFromNetwork() throws Exception {		  
        return getRestTemplate().getForObject("http://phish.in/api/v1/years/" + year, ShowsResponse.class);
    }

    /**
     * @return
     */
    public String createCacheKey() {
        return "years/" + year;
    }
}
