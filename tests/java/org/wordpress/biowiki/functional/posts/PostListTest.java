package org.wordpress.biowiki.functional.posts;

import org.wordpress.biowiki.ActivityRobotiumTestCase;
import org.wordpress.biowiki.R;
import kr.kdev.dg1s.biowiki.ui.posts.PostsActivity;

public class PostListTest extends ActivityRobotiumTestCase<PostsActivity> {
    public PostListTest() {
        super(PostsActivity.class);
    }

    public void testLoadPosts() throws Exception {
        login();
        mSolo.clickOnText(mSolo.getString(R.string.posts));
        mSolo.scrollDown();
    }

    public void testCreateNewPost() throws Exception {
        login();
        mSolo.clickOnText(mSolo.getString(R.string.posts));
        mSolo.clickOnActionBarItem(R.id.menu_new_post);
        mSolo.clickOnActionBarItem(R.id.menu_save_post);
    }
}
