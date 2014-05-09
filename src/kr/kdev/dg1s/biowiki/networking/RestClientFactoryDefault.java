package kr.kdev.dg1s.biowiki.networking;

import com.android.volley.RequestQueue;
import com.wordpress.rest.RestClient;

public class RestClientFactoryDefault implements RestClientFactoryAbstract {
    public RestClient make(RequestQueue queue) {
        return new RestClient(queue);
    }
}
