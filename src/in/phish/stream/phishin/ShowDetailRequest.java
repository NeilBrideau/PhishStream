package in.phish.stream.phishin;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class ShowDetailRequest extends SpringAndroidSpiceRequest<ShowDetailResponse> {
	private final int id;

	public ShowDetailRequest(int id) {
		super(ShowDetailResponse.class);
		this.id = id;
		return;
	}

	@Override
	public ShowDetailResponse loadDataFromNetwork() throws Exception {
		return getRestTemplate().getForObject("http://phish.in/api/v1/shows/" + id, ShowDetailResponse.class);
	}

	public String createCacheKey() {
		return "shows/" + id;
	}
}
