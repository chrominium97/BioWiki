package kr.kdev.dg1s.biowiki.util;

import android.content.Context;

public class SystemServiceFactory {
    public static SystemServiceFactoryAbstract sFactory;

    public static Object get(Context context, String name) {
        if (sFactory == null) {
            sFactory = new SystemServiceFactoryDefault();
        }
        AppLog.v(AppLog.T.UTILS, "instantiate SystemService using sFactory: " + sFactory.getClass());
        return sFactory.get(context, name);
    }
}
