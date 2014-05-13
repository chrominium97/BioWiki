package kr.kdev.dg1s.biowiki.models;

import java.util.HashSet;

import kr.kdev.dg1s.biowiki.util.UrlUtils;

/**
 * Created by nbradbury on 9/3/13.
 * URLs are normalized before being added and during comparison to ensure better comparison
 * of URLs that may be different strings but point to the same URL
 */
public class ReaderUrlList extends HashSet<String> {

    @Override
    public boolean add(String url) {
        return super.add(UrlUtils.normalizeUrl(url));
    }

    @Override
    public boolean remove(Object object) {
        if (object instanceof String) {
            return super.remove(UrlUtils.normalizeUrl((String) object));
        } else {
            return super.remove(object);
        }
    }

    @Override
    public boolean contains(Object object) {
        if (object instanceof String) {
            return super.contains(UrlUtils.normalizeUrl((String) object));
        } else {
            return super.contains(object);
        }
    }


}
