package kr.kdev.dg1s.biowiki.networking;

import kr.kdev.dg1s.biowiki.util.AppLog;

public class OAuthAuthenticatorFactory {
    public static OAuthAuthenticatorFactoryAbstract sFactory;

    public static OAuthAuthenticator instantiate() {
        if (sFactory == null) {
            sFactory = new OAuthAuthenticatorFactoryDefault();
        }
        AppLog.v(AppLog.T.UTILS, "instantiate OAuth using sFactory: " + sFactory.getClass());
        return sFactory.make();
    }
}
