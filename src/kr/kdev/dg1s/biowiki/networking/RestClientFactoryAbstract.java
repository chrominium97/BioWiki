package kr.kdev.dg1s.biowiki.networking;

import com.android.volley.RequestQueue;
import com.wordpress.rest.RestClient;

public interface RestClientFactoryAbstract {
    public RestClient make(RequestQueue queue);
}
