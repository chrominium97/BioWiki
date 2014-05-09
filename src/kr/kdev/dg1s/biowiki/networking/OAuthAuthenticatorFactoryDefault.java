package kr.kdev.dg1s.biowiki.networking;

public class OAuthAuthenticatorFactoryDefault implements OAuthAuthenticatorFactoryAbstract {
    public OAuthAuthenticator make() {
        return new OAuthAuthenticator();
    }
}
