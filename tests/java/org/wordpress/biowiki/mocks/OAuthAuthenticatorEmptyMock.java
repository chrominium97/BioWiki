package org.wordpress.biowiki.mocks;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import kr.kdev.dg1s.biowiki.BioWiki;
import kr.kdev.dg1s.biowiki.networking.AuthenticatorRequest;
import kr.kdev.dg1s.biowiki.networking.OAuthAuthenticator;

public class OAuthAuthenticatorEmptyMock extends OAuthAuthenticator {
    public void authenticate(AuthenticatorRequest request) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(BioWiki.getContext());
        settings.edit().putString(BioWiki.ACCESS_TOKEN_PREFERENCE, "dead-parrot").commit();
    }
}
