package org.wordpress.biowiki.models;

import android.test.InstrumentationTestCase;

import kr.kdev.dg1s.biowiki.BioWikiDB;
import kr.kdev.dg1s.biowiki.models.Post;

/**
 * Created by roundhill on 3/12/14.
 */
public class PostTest extends InstrumentationTestCase {
    private BioWikiDB mDB;

    @Override
    protected void setUp() throws Exception {
        mDB = new BioWikiDB(getInstrumentation().getContext());

        super.setUp();
    }

    public void testInvalidPostIdLoad() {
        Post post = mDB.getPostForLocalTablePostId(-1);

        assertNull(post);
    }

    public void testPostSaveAndLoad() {
        Post post = new Post(1, false);
        post.setTitle("test-post");
        mDB.savePost(post);

        Post loadedPost = mDB.getPostForLocalTablePostId(post.getLocalTablePostId());

        assertNotNull(loadedPost);
        assertEquals(loadedPost.getTitle(), post.getTitle());
    }


}
