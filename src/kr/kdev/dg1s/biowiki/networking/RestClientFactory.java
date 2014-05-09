package kr.kdev.dg1s.biowiki.networking;

import com.android.volley.RequestQueue;
import com.wordpress.rest.RestClient;

import kr.kdev.dg1s.biowiki.util.AppLog;

public class RestClientFactory {
    public static RestClientFactoryAbstract sFactory;

    public static RestClient instantiate(RequestQueue queue) {
        if (sFactory == null) {
            sFactory = new RestClientFactoryDefault();
        }
        AppLog.v(AppLog.T.UTILS, "instantiate RestClient using sFactory: " + sFactory.getClass());
        return sFactory.make(queue);
    }
}
