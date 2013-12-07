package in.phish.stream.phishin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.app.Application;

import com.octo.android.robospice.SpringAndroidSpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.springandroid.json.gson.GsonObjectPersisterFactory;

/**
 * A {@link SpringAndroidSpiceService} dedicated to json web services via gson.
 * Provides caching.
 * Added media type text/html because phish.in does not return correct 
 * json content-type.
 */
public class PhishInGsonSpringAndroidSpiceService extends SpringAndroidSpiceService {
	
    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        CacheManager cacheManager = new CacheManager();
        cacheManager.addPersister(new GsonObjectPersisterFactory(application));
        return cacheManager;
    }

    @Override
    public RestTemplate createRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // web services support json responses
        GsonHttpMessageConverter jsonConverter = new GsonHttpMessageConverter();

        List<MediaType> mediaTypes = new ArrayList<MediaType>();
        mediaTypes.add(MediaType.TEXT_HTML);
        jsonConverter.setSupportedMediaTypes(mediaTypes);
        
        final List<HttpMessageConverter<?>> listHttpMessageConverters = restTemplate.getMessageConverters();
        listHttpMessageConverters.add(jsonConverter);
        restTemplate.setMessageConverters(listHttpMessageConverters);
        return restTemplate;
    }
}
