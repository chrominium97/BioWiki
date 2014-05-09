package kr.kdev.dg1s.biowiki.util;

import android.content.Context;

public class SystemServiceFactoryDefault implements SystemServiceFactoryAbstract {
    public Object get(Context context, String name) {
        return context.getSystemService(name);
    }
}
