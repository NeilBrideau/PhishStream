package in.phish.stream.phishin;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class ShowsRequest extends SpringAndroidSpiceRequest<ShowsResponse> {
    private final int year;

    public ShowsRequest(int year) {
        super(ShowsResponse.class);
        this.year = year;
        return;
    }

    @Override
    public ShowsResponse loadDataFromNetwork() throws Exception {
        return getRestTemplate().getForObject("http://phish.in/api/v1/years/" + year, ShowsResponse.class);
    }

    public String createCacheKey() {
        return "years/" + year;
    }
}
