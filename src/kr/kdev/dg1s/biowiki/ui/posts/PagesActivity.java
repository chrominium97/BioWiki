package kr.kdev.dg1s.biowiki.ui.posts;

import kr.kdev.dg1s.biowiki.util.BWMobileStatsUtil;

public class PagesActivity extends PostsActivity {
    // Exists to distinguish pages from posts in menu drawer

    @Override
    protected String statEventForViewOpening() {
        return BWMobileStatsUtil.StatsEventPagesOpened;
    }

    @Override
    protected String statEventForViewClosing() {
        return BWMobileStatsUtil.StatsEventPagesClosed;
    }

    @Override
    protected String statEventForNewPost() {
        return BWMobileStatsUtil.StatsEventPagesClickedNewPage;
    }
}
