package org.wordpress.biowiki.functional.nux;

import org.wordpress.biowiki.ActivityRobotiumTestCase;
import org.wordpress.biowiki.R;
import org.wordpress.biowiki.RobotiumUtils;
import org.wordpress.biowiki.mocks.RestClientFactoryTest;
import kr.kdev.dg1s.biowiki.ui.posts.PostsActivity;
import kr.kdev.dg1s.biowiki.ui.prefs.PreferencesActivity;
import kr.kdev.dg1s.biowiki.util.EditTextUtils;

public class NewBlogTest  extends ActivityRobotiumTestCase<PostsActivity> {
    public NewBlogTest() {
        super(PostsActivity.class);
    }

    public void testCreateAccountSuccess() throws Exception {
        login();
        mSolo.clickOnActionBarItem(R.id.menu_settings);
        mSolo.clickOnText(mSolo.getString(R.string.create_new_blog_wpcom));
        mSolo.enterText(0, "Site name");
        String siteUrl = EditTextUtils.getText(mSolo.getEditText(1));
        assertEquals(siteUrl, "sitename");
        RobotiumUtils.clickOnId(mSolo, "signup_button");
        mSolo.assertCurrentActivity("Should display PreferencesActivity", PreferencesActivity.class);
    }

    public void testCreateAccountSiteReserved() throws Exception {
        login();
        RestClientFactoryTest.setPrefixAllInstances("site-reserved");
        mSolo.clickOnActionBarItem(R.id.menu_settings);
        mSolo.clickOnText(mSolo.getString(R.string.create_new_blog_wpcom));
        mSolo.enterText(0, "Site reserved");
        RobotiumUtils.clickOnId(mSolo, "signup_button");
        assertTrue(mSolo.searchText(mSolo.getString(R.string.blog_name_reserved)));
    }

    public void testCreateAccountTimeout() throws Exception {
        login();
        RestClientFactoryTest.setPrefixAllInstances("timeout");
        mSolo.clickOnActionBarItem(R.id.menu_settings);
        mSolo.clickOnText(mSolo.getString(R.string.create_new_blog_wpcom));
        mSolo.enterText(0, "timeout");
        RobotiumUtils.clickOnId(mSolo, "signup_button");
        assertTrue(mSolo.searchText(mSolo.getString(R.string.error)));
    }
}

