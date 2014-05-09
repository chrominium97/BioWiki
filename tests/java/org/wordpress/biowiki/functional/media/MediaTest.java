package org.wordpress.biowiki.functional.media;

import com.robotium.solo.Solo;

import org.wordpress.biowiki.ActivityRobotiumTestCase;
import org.wordpress.biowiki.R;
import org.wordpress.biowiki.RobotiumUtils;
import kr.kdev.dg1s.biowiki.ui.media.MediaBrowserActivity;
import kr.kdev.dg1s.biowiki.ui.posts.EditPostActivity;
import kr.kdev.dg1s.biowiki.ui.posts.PostsActivity;

public class MediaTest extends ActivityRobotiumTestCase<PostsActivity> {
    public MediaTest() {
        super(PostsActivity.class);
    }

    public void testLoadMediaLandscape() throws Exception {
        mSolo.setActivityOrientation(Solo.LANDSCAPE);
        login();
        mSolo.waitForText(mSolo.getString(R.string.media));
        mSolo.clickOnText(mSolo.getString(R.string.media));
        mSolo.setActivityOrientation(Solo.PORTRAIT);
        mSolo.waitForText("wpid-pony.jpg", 1 , 10000);
        mSolo.setActivityOrientation(Solo.LANDSCAPE);
        mSolo.waitForText("wpid-pony.jpg", 1 , 10000);
    }


    public void testDeleteMedia() throws Exception {
        login();
        mSolo.waitForText(mSolo.getString(R.string.media));
        mSolo.clickOnText(mSolo.getString(R.string.media));
        // wait for reloading
        mSolo.waitForText("wpid-pony.jpg", 1 , 10000);
        mSolo.clickLongOnText("wpid-pony.jpg");
        // seems not to work on CAB
        // mSolo.clickOnActionBarItem(R.id.media_multiselect_actionbar_trash);
        RobotiumUtils.clickOnId(mSolo, "media_multiselect_actionbar_trash");
    }

    public void testDeleteMediaCancel() throws Exception {
        login();
        mSolo.waitForText(mSolo.getString(R.string.media));
        mSolo.clickOnText(mSolo.getString(R.string.media));
        // wait for reloading
        mSolo.waitForText("wpid-Urbanhero", 1 , 10000);
        mSolo.clickLongOnText("wpid-Urbanhero");
        RobotiumUtils.clickOnId(mSolo, "media_multiselect_actionbar_trash");
        mSolo.clickOnText(mSolo.getString(R.string.cancel));
        assertTrue("Urbanhero shouldn't be deleted", mSolo.searchText("wpid-Urbanhero"));
    }

    public void testDeleteMediaConfirm() throws Exception {
        login();
        mSolo.waitForText(mSolo.getString(R.string.media));
        mSolo.clickOnText(mSolo.getString(R.string.media));
        // wait for reloading
        mSolo.waitForText("wpid-Urbanhero", 1 , 10000);
        mSolo.clickLongOnText("wpid-Urbanhero");
        RobotiumUtils.clickOnId(mSolo, "media_multiselect_actionbar_trash");
        mSolo.clickOnText(mSolo.getString(R.string.delete));
        assertFalse("Urbanhero should be deleted", mSolo.searchText("wpid-Urbanhero"));
    }

    public void testDeleteMultipleMedias() throws Exception {
        login();
        mSolo.waitForText(mSolo.getString(R.string.media));
        mSolo.clickOnText(mSolo.getString(R.string.media));
        // wait for reloading
        mSolo.waitForText("wpid-pony.jpg", 1 , 10000);
        mSolo.clickLongOnText("wpid-pony.jpg");
        mSolo.clickOnImage(3);
        mSolo.clickOnImage(2);
        mSolo.clickOnImage(0);
        RobotiumUtils.clickOnId(mSolo, "media_multiselect_actionbar_trash");
        mSolo.clickOnText(mSolo.getString(R.string.delete));
        assertFalse("Urbanhero should be deleted", mSolo.searchText("wpid-Urbanhero", true));
    }

    public void testCreateGalleryCancel() throws Exception {
        login();
        mSolo.waitForText(mSolo.getString(R.string.media));
        mSolo.clickOnText(mSolo.getString(R.string.media));
        // wait for reloading
        mSolo.waitForText("wpid-pony.jpg", 1, 10000);
        mSolo.clickLongOnText("wpid-pony.jpg");
        mSolo.clickOnImage(0);
        RobotiumUtils.clickOnId(mSolo, "media_multiselect_actionbar_gallery");
        mSolo.goBack();
        mSolo.assertCurrentActivity("Should be back on MediaBrowserActivity", MediaBrowserActivity.class);
    }

    public void testCreateGalleryConfirm() throws Exception {
        login();
        mSolo.waitForText(mSolo.getString(R.string.media));
        mSolo.clickOnText(mSolo.getString(R.string.media));
        // wait for reloading
        mSolo.waitForText("wpid-pony.jpg", 1, 10000);
        mSolo.clickLongOnText("wpid-pony.jpg");
        mSolo.clickOnImage(0);
        RobotiumUtils.clickOnId(mSolo, "media_multiselect_actionbar_gallery");
        RobotiumUtils.clickOnId(mSolo, "menu_save");
        mSolo.assertCurrentActivity("Should display EditPostActivity", EditPostActivity.class);
    }
}
