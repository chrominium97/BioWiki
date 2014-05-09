package kr.kdev.dg1s.biowiki.models;

import java.util.HashSet;

/**
 * Created by nbradbury on 7/18/13.
 */
public class ReaderUserIdList extends HashSet<Long> {
    /*
     * returns true if passed list contains the same userIds as this list
     */
    public boolean isSameList(ReaderUserIdList compareIds) {
        if (compareIds==null || compareIds.size()!=this.size())
            return false;
        return this.containsAll(compareIds);
    }
}
