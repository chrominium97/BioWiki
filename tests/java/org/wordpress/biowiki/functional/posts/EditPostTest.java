package org.wordpress.biowiki.functional.posts;

import org.wordpress.biowiki.ActivityRobotiumTestCase;
import org.wordpress.biowiki.R;
import kr.kdev.dg1s.biowiki.BioWiki;
import kr.kdev.dg1s.biowiki.BioWikiDB;

import org.wordpress.biowiki.mocks.XMLRPCFactoryTest;
import kr.kdev.dg1s.biowiki.models.Post;
import kr.kdev.dg1s.biowiki.ui.posts.PostsActivity;

public class EditPostTest extends ActivityRobotiumTestCase<PostsActivity> {
    public EditPostTest() {
        super(PostsActivity.class);
    }

    public void testEditNullPostId() throws Exception {
        XMLRPCFactoryTest.setPrefixAllInstances("malformed-null-postid");
        login();
        mSolo.clickOnText(mSolo.getString(R.string.posts));
        BioWikiDB wpdb = BioWiki.wpDB;
        Post post = new Post(59073674, false);
        post.setRemotePostId(null);
        wpdb.savePost(post);
        mSolo.clickOnText("null postid");
    }
}
