package org.xmlrpc.android;

import java.net.URI;

import kr.kdev.dg1s.biowiki.util.AppLog;
import kr.kdev.dg1s.biowiki.util.AppLog.T;

public class XMLRPCFactory {
    public static XMLRPCFactoryAbstract sFactory;

    public static XMLRPCClientInterface instantiate(URI uri, String httpUser, String httpPassword) {
        if (sFactory == null) {
            sFactory = new XMLRPCFactoryDefault();
        }
        AppLog.v(T.UTILS, "instantiate XMLRPCClient using sFactory: " + sFactory.getClass());
        return sFactory.make(uri, httpUser, httpPassword);
    }
}
