package org.wordpress.biowiki;

import org.wordpress.biowiki.mocks.OAuthAuthenticatorFactoryTest;
import org.wordpress.biowiki.mocks.RestClientFactoryTest;
import org.wordpress.biowiki.mocks.SystemServiceFactoryTest;
import org.wordpress.biowiki.mocks.XMLRPCFactoryTest;
import kr.kdev.dg1s.biowiki.networking.OAuthAuthenticatorFactory;
import kr.kdev.dg1s.biowiki.networking.RestClientFactory;
import kr.kdev.dg1s.biowiki.util.AppLog;
import kr.kdev.dg1s.biowiki.util.AppLog.T;
import kr.kdev.dg1s.biowiki.util.SystemServiceFactory;
import org.xmlrpc.android.XMLRPCFactory;

public class FactoryUtils {
    public static void initWithTestFactories() {
        // create test factories
        XMLRPCFactory.sFactory = new XMLRPCFactoryTest();
        RestClientFactory.sFactory = new RestClientFactoryTest();
        OAuthAuthenticatorFactory.sFactory = new OAuthAuthenticatorFactoryTest();
        SystemServiceFactory.sFactory = new SystemServiceFactoryTest();
        AppLog.v(T.TESTS, "Mocks factories instantiated");
    }
}
